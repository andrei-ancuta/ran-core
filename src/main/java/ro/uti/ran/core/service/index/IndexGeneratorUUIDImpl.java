package ro.uti.ran.core.service.index;

import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-31
 */
@Component
public class IndexGeneratorUUIDImpl implements IndexGenerator {

    @Override
    public String getNextIndex(Index index) {
        return UUID.randomUUID().toString();
    }
}
