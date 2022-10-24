package ro.uti.ran.core.service.parametru;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.portal.Parametru;

import java.util.*;

import static ro.uti.ran.core.config.Profiles.DEV;
import static ro.uti.ran.core.config.Profiles.TEST;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:21
 */
@Component
@Profile({DEV, TEST})
public class ParametruServiceDevImpl implements ParametruService {

    private final Map<String, Parametru> parametrii = new HashMap<String, Parametru>();

    public ParametruServiceDevImpl() {

        Properties properties = new Properties();
        try {
            properties.load(ParametruServiceDevImpl.class.getResourceAsStream("/parametri/dev-parametri-env.properties"));

            for (Object key : properties.keySet()) {
                String _key = key.toString();

                Parametru p = new Parametru();
                p.setDenumire(_key);
                p.setValoare(properties.getProperty(_key));

                parametrii.put(_key, p);
            }

        } catch (Throwable e) {
            throw new RuntimeException("Eroare la incarcare parametri", e);
        }
    }

    @Override
    public Parametru getParametru(String _parametru) {
        Parametru parametru = parametrii.get(_parametru);

        if (parametru != null) {
            return parametru;
        }

        for (String cod : parametrii.keySet()) {
            if (parametrii.get(cod).getDenumire().equals(_parametru)) {
                return parametrii.get(cod);
            }
        }

        return null;
    }

    @Override
    public List<Parametru> getListaParametri() {
        return new ArrayList<Parametru>(parametrii.values());
    }

    @Override
    public Parametru salveazaParametru(Parametru parametru) {
        if (parametru == null) {
            throw new IllegalArgumentException("Parametru nedefinit");
        }
        return parametru;
    }
}
