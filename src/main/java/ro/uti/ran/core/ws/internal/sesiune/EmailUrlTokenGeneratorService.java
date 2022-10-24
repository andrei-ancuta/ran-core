package ro.uti.ran.core.ws.internal.sesiune;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;

/**
 * Created by Stanciu Neculai on 11-Apr-16.
 */
@Service
public class EmailUrlTokenGeneratorService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    /**
     * @param utilizator
     * @return tokenString or null
     */
    public String generateTokenAndSaveToUser(Utilizator utilizator) {
        if (utilizator != null) {
            String tokenStringData = utilizator.getNumeUtilizator() + " " + utilizator.getId();
            String encodedToken = passwordEncoder.encode(tokenStringData);
            utilizator.setTokenUtilizator(encodedToken);
            utilizatorRepository.save(utilizator);
            return encodedToken;
        } else {
            return null;
        }
    }

    public Boolean tokenMatches(String token, String userToken) {
        if (userToken != null && token != null) {
            return userToken.equals(token);
        } else {
            return null;
        }
    }
}
