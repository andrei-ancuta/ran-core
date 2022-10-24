package ro.uti.ran.core.rapoarte.mappers;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

public class CaseInsensitivePathMatcher extends AntPathMatcher {
    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
        return super.doMatch(pattern.toLowerCase(), path.toLowerCase(), fullMatch, uriTemplateVariables);
    }
}
