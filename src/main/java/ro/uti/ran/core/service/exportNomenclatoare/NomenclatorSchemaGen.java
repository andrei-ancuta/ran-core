package ro.uti.ran.core.service.exportNomenclatoare;

import org.springframework.stereotype.Component;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExport;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Created by Anastasia cea micuta on 12/1/2015.
 */
@Component
public class NomenclatorSchemaGen {
    public String generateListaNomenclatoareXsdSchema() throws JAXBException, IOException {
        return generateXsdSchema(NomenclatorsSummary.class);
    }

    public String generateNomenclatorXsdSchema() throws JAXBException, IOException {
        return generateXsdSchema(NomenclatorExport.class);
    }

    private String generateXsdSchema(Class clazz) throws JAXBException, IOException {
        // grab the context
        JAXBContext context = JAXBContext.newInstance(clazz);

        final List<DOMResult> results = new ArrayList<DOMResult>();

        // generate the schema
        context.generateSchema(
                // need to define a SchemaOutputResolver to store to
                new SchemaOutputResolver() {
                    @Override
                    public Result createOutput(String ns, String file)
                            throws IOException {
                        // save the schema to the list
                        DOMResult result = new DOMResult();
                        result.setSystemId(file);
                        results.add(result);
                        return result;
                    }
                });

        // output schema via System.out
        DOMResult domResult = results.get(0);
        Document doc = (Document) domResult.getNode();
        /*OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(outputStream, format);
        serializer.serialize(doc);*/

        String subscrXML = null;
        StringWriter stringWriter = new StringWriter();
        try {
            //Get the implementations

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            DOMImplementationLS impls = (DOMImplementationLS) registry.getDOMImplementation("LS");


            //Prepare the output
            LSOutput domOutput = impls.createLSOutput();
            domOutput.setEncoding(java.nio.charset.Charset.defaultCharset().name());
            domOutput.setCharacterStream(stringWriter);
            domOutput.setEncoding("UTF-8");
            //Prepare the serializer
            LSSerializer domWriter = impls.createLSSerializer();
            DOMConfiguration domConfig = domWriter.getDomConfig();
            domConfig.setParameter("format-pretty-print", true);
            domConfig.setParameter("element-content-whitespace", true);
            domWriter.setNewLine("\r\n");
            domConfig.setParameter("cdata-sections", Boolean.TRUE);
            //And finaly, write
            domWriter.write(doc, domOutput);
            subscrXML = domOutput.getCharacterStream().toString();
            return subscrXML;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
