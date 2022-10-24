package ro.uti.ran.core.rapoarte.task;

import org.springframework.stereotype.Component;
import ro.uti.ran.core.rapoarte.model.Rapoarte;
import ro.uti.ran.core.rapoarte.task.summary.RapoarteSummary;
import ro.uti.ran.core.rapoarte.task.summary.SumarRapoarte;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by miroslav.rusnac on 29/03/2016.
 */
@Component
public class RapoarteXmlGen {
    public String generateRapoarteXml(File[] listOfFiles) throws JAXBException, IOException {
        JAXBContext contextObj = JAXBContext.newInstance(SumarRapoarte.class);
        SumarRapoarte toXml = new SumarRapoarte();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Rapoarte toAdd =new Rapoarte(listOfFiles[i].getName());
                toXml.add(toAdd);

            }
        }
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter xml = new StringWriter();
        marshallerObj.marshal(toXml, xml);
        return xml.toString();
    }


}
