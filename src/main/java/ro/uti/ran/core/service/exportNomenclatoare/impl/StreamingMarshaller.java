package ro.uti.ran.core.service.exportNomenclatoare.impl;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExport;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportMetadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA. User: mala
 */
public class StreamingMarshaller<T> {

    private final Class<T> type;
    private XMLStreamWriter xmlOut;
    private Marshaller marshaller;
    private NomenclatorExportMetadata nomenclatorExportMetadata;

    public StreamingMarshaller(Class<T> type, NomenclatorExportMetadata nomenclatorExportMetadata) throws JAXBException {
        this.type = type;
        this.nomenclatorExportMetadata = nomenclatorExportMetadata;
        JAXBContext context = JAXBContext.newInstance(type, NomenclatorExport.class);
        this.marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    }

    public void open(String filename) throws XMLStreamException, IOException, JAXBException {
        xmlOut = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(filename));
        try {
            xmlOut = new IndentingXMLStreamWriter(xmlOut);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        xmlOut.writeStartDocument();
        xmlOut.writeStartElement("nomenclator");

        // marshall metadata
        marshaller.marshal(nomenclatorExportMetadata, xmlOut);
        // prepare to write nomenclator values
        xmlOut.writeStartElement("valori");
    }

    public void writeJaxbElement(T t) throws JAXBException {
        JAXBElement<T> element = new JAXBElement<T>(QName.valueOf(type.getSimpleName()), type, t);
        marshaller.marshal(element, xmlOut);
    }

    public void write(T t) throws JAXBException {
        marshaller.marshal(t, xmlOut);
    }

    public void close() throws XMLStreamException {
        xmlOut.writeEndDocument();
        xmlOut.flush();
        xmlOut.close();
    }

}
