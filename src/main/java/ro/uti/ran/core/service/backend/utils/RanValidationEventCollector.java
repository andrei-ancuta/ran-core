package ro.uti.ran.core.service.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;

/**
 * Created by Dan on 12-Oct-15.
 */
public class RanValidationEventCollector extends ValidationEventCollector {

    private static final Logger logger = LoggerFactory.getLogger(RanValidationEventCollector.class);

    @Override
    public boolean handleEvent(ValidationEvent event) {
        super.handleEvent(event);
        return true;
    }
}