package ro.uti.ran.core.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.portal.JudetRepository;
import ro.uti.ran.core.repository.portal.SistemRepository;
import ro.uti.ran.core.repository.portal.UatRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanciu Neculai on 15.Oct.2015.
 */
@Service
public class RanUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(RanUserDetailsService.class);
    public static final String USERNAME_NOT_FOUND_MESSAGE = "Nu exista utilizator pentru numele specificat!";
    public static final String ROLE_INSTITUTIE = "ROLE_INSTITUTIE";
    public static final String ROLE_JUDET = "ROLE_JUDET";
    public static final String ROLE_UAT = "ROLE_UAT";


    @Autowired
    private SistemRepository sistemRepository;
    @Autowired
    private UatRepository uatRepository;
    @Autowired
    private InstitutieRepository institutieRepository;
    @Autowired
    private JudetRepository judetRepository;

    private UserDetails createUserDetailsFrom(Sistem sistemUser) {
        List<GrantedAuthority> authorityList = createUserDetailsRolesFrom(sistemUser);
        //UserDetails userDetails = new User(sistemUser.getNumeUtilizator(), sistemUser.getParola(), sistemUser.getActiv(), true, true, true, authorityList);
        // c-m modificari model din 24.11.2015
        UserDetails userDetails = new User(sistemUser.getCod(), sistemUser.getLicenta(), sistemUser.getActiv(), true, true, true, authorityList);
        return userDetails;
    }

    private List<GrantedAuthority> createUserDetailsRolesFrom(Sistem sistemUser) {
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        if (sistemUser.getInstitutie() != null) {
            Long idInstitutie = sistemUser.getInstitutie().getId();
            if (idInstitutie.intValue() == NomInstitutieEnum.UAT.getValue()) {
                if (sistemUser.getUat() != null) {
                    SimpleGrantedAuthority rolUat = new SimpleGrantedAuthority(ROLE_UAT + "_" + sistemUser.getUat().getId());
                    roles.add(rolUat);
                }
            } else if (idInstitutie.intValue() == NomInstitutieEnum.JUDET.getValue()) {
                if (sistemUser.getJudet() != null) {
                    SimpleGrantedAuthority rolJudet = new SimpleGrantedAuthority(ROLE_JUDET + "_" + sistemUser.getJudet().getId());
                    roles.add(rolJudet);
                }
            } else {
                SimpleGrantedAuthority rolInstitutie = new SimpleGrantedAuthority(ROLE_INSTITUTIE + "_" + idInstitutie);
                roles.add(rolInstitutie);
            }
        }

        return roles;
    }

    public Sistem loadSistemUserByUsername(String sistemUsername) {
        boolean userFound = false;
        if (sistemUsername == null || sistemUsername.isEmpty()) {
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND_MESSAGE);
        }
        try {
            Sistem sistemUser = getSistemUserFromUat(sistemUsername);
            if (sistemUser == null) {
                sistemUser = getSistemUserFromJudet(sistemUsername);
                if (sistemUser == null) {
                    sistemUser = getSistemUserFromInstitutie(sistemUsername);
                    if (sistemUser != null) {
                        userFound = true;
                    }
                } else {
                    userFound = true;
                }
            } else {
                userFound = true;
            }
            if (userFound) {
                return sistemUser;
            } else {
                return null;
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private Sistem getSistemUserFromJudet(String sistemUsername) {
        Long codSiruta = null;
        try {
            codSiruta = Long.parseLong(sistemUsername);
        } catch (NumberFormatException e) {
            //ignore
        }
        if (codSiruta != null) {
            Judet judet = judetRepository.findByCodSiruta(codSiruta);
            if (judet != null) {
                return sistemRepository.findByJudet_Id(judet.getId());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        boolean userFound = false;
        if (s == null || s.isEmpty()) {
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND_MESSAGE);
        }
        s = s.trim();
        Sistem sistemUser = loadSistemUserByUsername(s);
        if (sistemUser != null) {
            userFound = true;
        }
        if (!userFound) {
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND_MESSAGE);
        }
        sistemUser.setCod(s);
        return createUserDetailsFrom(sistemUser);
    }

    private Sistem getSistemUserFromUat(String s) {
        Integer codSiruta = null;
        try {
            codSiruta = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            //ignore
        }
        if (codSiruta != null) {
            UAT uat = uatRepository.findByCodSiruta(codSiruta);
            if (uat != null) {
                return sistemRepository.findByUat_Id(uat.getId());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean isFromUatOrInstitutie(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        Sistem sistemUser = getSistemUserFromUat(s);
        if (sistemUser == null) {
            sistemUser = getSistemUserFromJudet(s);
            if (sistemUser == null) {
                sistemUser = getSistemUserFromInstitutie(s);
                if (sistemUser != null) {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    private Sistem getSistemUserFromInstitutie(String s) {
        Institutie institutie = institutieRepository.findByCod(s);
        if (institutie != null) {
            return sistemRepository.findByInstitutie_Id(institutie.getId());
        } else {
            return null;
        }
    }
}
