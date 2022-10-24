package ro.uti.ran.core.ftp;

import org.springframework.security.core.GrantedAuthority;
import ro.uti.ran.core.model.portal.Sistem;

import java.util.Collection;

/**
 * Created by Stanciu Neculai on 26.Oct.2015.
 */
public interface FtpServerRootDirStrategy {
    void setSistemUser(Sistem sistemUser);

    String getRootDirPath() throws FtpPathNotAuthorizedException;

    String getRootDirPathNoData();

    void setUserDetailsAuthorities(Collection<? extends GrantedAuthority> authorities);
}
