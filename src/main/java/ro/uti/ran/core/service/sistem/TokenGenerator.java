package ro.uti.ran.core.service.sistem;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-07 12:17
 */
@Component
public class TokenGenerator {

    public String generateToken(){
        return RandomStringUtils.randomAlphanumeric(20);
    }

    public static void main(String[] args) {
        System.out.println(RandomStringUtils.randomAlphanumeric(20));
    }
}
