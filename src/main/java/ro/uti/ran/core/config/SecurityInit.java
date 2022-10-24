package ro.uti.ran.core.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
* Created by Stanciu Neculai on 15.Oct.2015.
*/
public class SecurityInit extends AbstractSecurityWebApplicationInitializer {

  public SecurityInit() {
        super(SecurityConfig.class);
  }
}
