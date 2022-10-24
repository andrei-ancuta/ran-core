package ro.uti.ran.core.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.service.security.RanUserDetailsService;

/**
 * Created by Stanciu Neculai on 26.Oct.2015.
 */
@Component("templatePathFtpRootDir")
public class TemplatePathFtpRootDir implements FtpServerRootDirStrategy {

    private Sistem sistemUser;

    private Collection<? extends GrantedAuthority> authorities;

    public static final Logger LOG = LoggerFactory.getLogger(TemplatePathFtpRootDir.class);

    private static final String ROOT_DIR_KEY ="ftp.homeDirRoot";
    private static final String TEMPLATE_PATH ="ftp.templatePathRootDir";

    private static final String COD_SIRUTA_KEY = "SIRUTA_UAT";
    private static final String PATH_NOT_AUTHORIZED_MESSAGE = "Nu aveti drepturi pe aceasta cale!";

    @Autowired
    private Environment env;

    public TemplatePathFtpRootDir() {
    }

    @Override
    public void setSistemUser(Sistem sistemUser) {
        this.sistemUser = sistemUser;
    }

    @Override
    public void setUserDetailsAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getRootDirPath() throws FtpPathNotAuthorizedException {
        StringBuilder rootDir = new StringBuilder();
        rootDir.append(env.getProperty(ROOT_DIR_KEY));
        String templatePath = env.getProperty(TEMPLATE_PATH);
        if(templatePath!= null && !templatePath.isEmpty()){
            String contextPath = extractPathFromTemplate(templatePath);
            rootDir.append(contextPath);
        }
        return rootDir.toString();
    }

    @Override
    public String getRootDirPathNoData() {
        StringBuilder rootDir = new StringBuilder(30);
        rootDir.append(env.getProperty(ROOT_DIR_KEY));
        rootDir.append("nodata");
        return rootDir.toString();
    }

    private String extractPathFromTemplate(String templatePath) throws FtpPathNotAuthorizedException {
        StringBuilder contextPath = new StringBuilder();
        String[] elements = templatePath.split("/");
        if(elements != null && elements.length >= 1){
            for(int i=0;i<elements.length;i++){
                String element = elements[i];
                String elementPath = extractElementPath(element);
                if(elementPath != null && !elementPath.isEmpty()){
                    contextPath.append(elementPath);
                    if(i< (elements.length -1)){
                        contextPath.append("/");
                    }
                }
            }
        }
        return contextPath.toString();
    }

    public String extractElementPath(String element) throws FtpPathNotAuthorizedException {
        if(isInRole(RanUserDetailsService.ROLE_UAT) || isInRole(RanUserDetailsService.ROLE_JUDET)) {
            if (element.equals(COD_SIRUTA_KEY)) {
                String codSiruta = null;
                if(sistemUser.getUat() != null) {
                    UAT uat = sistemUser.getUat();
                    codSiruta = String.valueOf(uat.getCodSiruta());
                } else if(sistemUser.getJudet() != null) {
                    Judet judet = sistemUser.getJudet();
                    codSiruta = String.valueOf(judet.getCodSiruta());
                }
                if(codSiruta != null){
                    return codSiruta;
                }
            }
            return null;
        } else {
            throw new FtpPathNotAuthorizedException(PATH_NOT_AUTHORIZED_MESSAGE);
        }
    }

    private boolean isInRole(String role){
        if(role == null || role.isEmpty()){
            return  false;
        }
        if(authorities == null || authorities.isEmpty()){
            return false;
        }
        for(GrantedAuthority authority : authorities){
            if(authority.getAuthority().matches(role+"_.*")){
                return true;
            }
        }
        return false;
    }


}
