package ro.uti.ran.core.service.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Stanciu Neculai on 29.Oct.2015.
 */
public interface AuthorizationService {
    boolean isAuthorized(UserDetails userDetails);
}
