package ro.uti.ran.core.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.service.security.RanUserDetailsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanciu Neculai on 14.Oct.2015.
 */
@Component
public class RanUserManager implements UserManager {
    public static final Logger LOG = LoggerFactory.getLogger(RanUserManager.class);

    public static final String MAX_CONCURRENT_LOGINS_KEY = "ftp.maxConcurrentLogins";
    public static final String MAX_CONCURRENT_LOGINS_PER_IP_KEY = "ftp.maxConcurrentLoginsPerIP";

    @Autowired
    private RanUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("templatePathFtpRootDir")
    private FtpServerRootDirStrategy serverRootDir;

    private User createBaseUserFrom(UserDetails userDetails) {
        BaseUser user = new BaseUser();
        user.setName(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEnabled(userDetails.isEnabled());

        //pentru concurent login
        List<Authority> auths = new ArrayList<Authority>();
        Integer maxConcurrentLogins = Integer.valueOf(env.getProperty(MAX_CONCURRENT_LOGINS_KEY));
        Integer maxConcurrentLoginsPerIp = Integer.valueOf(env.getProperty(MAX_CONCURRENT_LOGINS_PER_IP_KEY));
        Authority concurrentLoginPermission = new ConcurrentLoginPermission(maxConcurrentLogins, maxConcurrentLoginsPerIp);
        auths.add(concurrentLoginPermission);
        user.setAuthorities(auths);

        //set rootdir
        Sistem sistemUser = userDetailsService.loadSistemUserByUsername(userDetails.getUsername());
        serverRootDir.setSistemUser(sistemUser);
        serverRootDir.setUserDetailsAuthorities(userDetails.getAuthorities());
        String rootDirStr = null;
        try {
            rootDirStr = serverRootDir.getRootDirPath();
        } catch (FtpPathNotAuthorizedException e) {
            return null;
        }

        File ftpDir = new File(rootDirStr);
        if (!ftpDir.exists()) {
            rootDirStr = serverRootDir.getRootDirPathNoData();
            ftpDir = new File(rootDirStr);
            try {
                FileUtils.forceMkdir(ftpDir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        user.setHomeDirectory(rootDirStr);
        return user;
    }

    @Override
    public User getUserByName(String s) throws FtpException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(s);
        return createBaseUserFrom(userDetails);
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[0];
    }

    @Override
    public void delete(String s) throws FtpException {
        throw new UnsupportedOperationException("RanUserManager is readOnly");
    }

    @Override
    public void save(User user) throws FtpException {
        throw new UnsupportedOperationException("RanUserManager is readOnly");
    }

    @Override
    public boolean doesExist(String s) throws FtpException {
        User user = getUserByName(s);
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication passwordAuthentication = (UsernamePasswordAuthentication) authentication;
            try {
                User user = getUserByName(passwordAuthentication.getUsername());
                if (encoder.matches(passwordAuthentication.getPassword(), user.getPassword())) {
                    return user;
                } else {
                    throw new AuthenticationFailedException("Credentialele nu sunt corecte!");
                }
            } catch (FtpException e) {
                LOG.error("Eroare", e);
                throw new AuthenticationFailedException("Nu s-a putut obtine utilizaotrul!");
            }
        } else if (authentication instanceof AnonymousAuthentication) {
            throw new AuthenticationFailedException("Anonymous nu este acceptat!");
        } else {
            throw new AuthenticationFailedException("Credentialele nu sunt corecte!");
        }
    }

    @Override
    public String getAdminName() throws FtpException {
        return "";
    }

    @Override
    public boolean isAdmin(String s) throws FtpException {
        return false;
    }


}
