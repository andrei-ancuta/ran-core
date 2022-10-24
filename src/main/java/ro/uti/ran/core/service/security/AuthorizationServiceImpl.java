package ro.uti.ran.core.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Created by Stanciu Neculai on 29.Oct.2015.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    private final ExternWsContextEnum wsContext;

    public AuthorizationServiceImpl(ExternWsContextEnum wsContext) {
        this.wsContext = wsContext;
    }

    private static final Map<ExternWsContextEnum, List<? extends GrantedAuthority>> permisionMap;

    // TODO: daca e singletone ce rost are blocul static?...variabilele statice intr-un singleton nu sunt thread-safe! (de corectat)
    static {
        Map<ExternWsContextEnum, List<? extends GrantedAuthority>> map = new HashMap<ExternWsContextEnum, List<? extends GrantedAuthority>>();
        map.put(ExternWsContextEnum.WS_TRANSMITERE_DATE,
                Arrays.asList(
                        new SimpleGrantedAuthority(RanUserDetailsService.ROLE_INSTITUTIE),
                        new SimpleGrantedAuthority(RanUserDetailsService.ROLE_UAT)
                ));
        map.put(ExternWsContextEnum.WS_INTEROGARE_DATE,
                Arrays.asList(
                        new SimpleGrantedAuthority(RanUserDetailsService.ROLE_UAT)
                ));
        permisionMap = Collections.unmodifiableMap(map);
    }

    public GrantedAuthority getUserDetailsRole(UserDetails userDetails){
        if(userDetails.getAuthorities().size() > 1 || userDetails.getAuthorities().isEmpty()){
            return null;
        } else {
            return userDetails.getAuthorities().iterator().next();
        }
    }

    private boolean isInRole(GrantedAuthority role){
        List<? extends GrantedAuthority> authorities = permisionMap.get(wsContext);
        if(role == null){
            return  false;
        }
        for(GrantedAuthority authority : authorities){
            if(role.getAuthority().matches(authority.getAuthority()+"_.*")){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAuthorized(UserDetails userDetails) {
        List<? extends GrantedAuthority> contextRoles = permisionMap.get(wsContext);
        GrantedAuthority userRole = getUserDetailsRole(userDetails);
        if(userRole == null){
            return false;
        } else {
            return isInRole(userRole);
        }
    }
}
