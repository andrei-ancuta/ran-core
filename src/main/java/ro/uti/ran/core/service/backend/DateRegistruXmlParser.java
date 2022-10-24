package ro.uti.ran.core.service.backend;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.StructuralValidationException;
import ro.uti.ran.core.exception.codes.StructuralValidationCodes;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.service.backend.utils.RanValidationEventCollector;
import ro.uti.ran.core.xml.model.RanDoc;

import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anastasia cea micuta on 10/21/2015.
 */
@Component
public class DateRegistruXmlParser {
    static final String CVC_TYPE_3_1_3 = "cvc-type.3.1.3";
    static final String CVC_MINLENGTH_VALID = "cvc-minLength-valid";
    static final String CVC_ATTRIBUTE_3 = "cvc-attribute.3";
    static final String CVC_COMPLEX_TYPE_2_4_A = "cvc-complex-type.2.4.a";
    static final String CVC_PATTERN_VALID = "cvc-pattern-valid";
    static final String CVC_DATATYPE_VALID_1_2_1 = "cvc-datatype-valid.1.2.1";
    static final String CVC_COMPLEX_TYPE_2_4_B = "cvc-complex-type.2.4.b";
    static final String CVC_COMPLEX_TYPE_4 = "cvc-complex-type.4";
    static final String CVC_COMPLEX_TYPE_2_4_D = "cvc-complex-type.2.4.d";
    static final String CVC_MININCLUSIVE_VALID = "cvc-minInclusive-valid";
    static final String CVC_MINEXCLUSIVE_VALID = "cvc-minExclusive-valid";
    static final String CVC_MAXINCLUSIVE_VALID = "cvc-maxInclusive-valid";
    static final String CVC_FRACTIONDIGITS_VALID = "cvc-fractionDigits-valid";
    static final String CVC_COMPLEX_TYPE_3_2_2 = "cvc-complex-type.3.2.2";
    static final String CVC_COMPLEX_TYPE_2_1 = "cvc-complex-type.2.1";
    static final String CVC_TYPE_3_1_1 = "cvc-type.3.1.1";
    static final String CVC_COMPLEX_TYPE_2_3 = "cvc-complex-type.2.3";
    static final String CVC_ENUMERATION_VALID = "cvc-enumeration-valid";

    private static final Logger logger = LoggerFactory.getLogger(DateRegistruXmlParser.class);
    private static final JAXBContext jaxbContext = initContext();

    private static JAXBContext initContext() {
        try {
            return JAXBContext.newInstance(RanDoc.class);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param xmlString datele in format xml
     * @return datele extrase din xml
     */
    public RanDoc getPojoFromXML(String xmlString) throws SyntaxXmlException, StructuralValidationException {
        /*daca mesajul este "citibil" aici se regaseste informatia bruta din mesaj*/
        RanDoc schemaRanDoc;

        /*colecteaza erorile la validare conform schema xsd; merge pana la capat nu se opreste la prima eroare;*/
        RanValidationEventCollector validationEventCollector = new RanValidationEventCollector();

        try {
            InputStream xmlStream = IOUtils.toInputStream(xmlString.trim(), "UTF-8");
            //initializari si configurari specifice JAXB
            SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Resource resource = getRanDocXsdSchema();
            Schema schema = schemaFactory.newSchema(resource.getURL());
            //
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(validationEventCollector);
            schemaRanDoc = (RanDoc) unmarshaller.unmarshal(xmlStream);

        } catch (Throwable t) {
            throw new StructuralValidationException(StructuralValidationCodes.XML_INVALID_STRUCTURAL, t, null != t.getMessage() ? t.getMessage() : (t.getCause() != null ? t
                    .getCause()
                    .getMessage() : ""));
        }

        if (schemaRanDoc != null && validationEventCollector.hasEvents()) {
            //mesajul este "citibil" dar nu este valid xsd
            throw
                    impacheteazaExceptia(validationEventCollector);
        } else {
            StringBuilder errorDescription = new StringBuilder();
            if (validationEventCollector.getEvents() != null) {
                for (ValidationEvent event : validationEventCollector.getEvents()) {
                    ValidationEventLocator locator = event.getLocator();
                    errorDescription
                            .append(event.getMessage() + " at row: " + locator.getLineNumber() + " column: " + locator.getColumnNumber())
                            .append("\n");
                }
            }
            if (errorDescription.toString().length() > 0) {
                throw new StructuralValidationException(StructuralValidationCodes.XML_INVALID_STRUCTURAL, errorDescription.toString());
            }
        }

        return schemaRanDoc;
    }

    /**
     * @param ranDoc datele in format xml
     * @return datele extrase din xml
     */
    public String getXMLFromPojo(RanDoc ranDoc) throws SyntaxXmlException, DateRegistruValidationException, StructuralValidationException, IOException {
        /*aici se gaseste xml-ul*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        /*colecteaza erorile la validare conform schema xsd; merge pana la capat nu se opreste la prima eroare;*/
        RanValidationEventCollector validationEventCollector = new RanValidationEventCollector();

        try {
            //initializari si configurari specifice JAXB
            SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Resource resource = getRanDocXsdSchema();
            Schema schema = schemaFactory.newSchema(resource.getURL());
            // Marshal the object to a StringWriter
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(schema);
            marshaller.setEventHandler(validationEventCollector);
            //
            marshaller.marshal(ranDoc, out);
        } catch (Throwable t) {
            throw new StructuralValidationException(StructuralValidationCodes.XML_INVALID_STRUCTURAL, t, null != t.getMessage() ? t.getMessage() : t
                    .getCause()
                    .getMessage());
        }
        //
        if (validationEventCollector.hasEvents()) {
            throw impacheteazaExceptia(validationEventCollector);
        }
        return IOUtils.toString(out.toByteArray(), "UTF-8").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    public Resource getRanDocXsdSchema() {
        return new ClassPathResource("schema1.xsd");
    }

    /**
     * @param ranValidationEventCollector erorile la validare conform schema xsd
     * @return exceptie ce contine descrierea erorii ce va fi utilizat la recipisa de răspuns
     */
    private SyntaxXmlException impacheteazaExceptia(RanValidationEventCollector ranValidationEventCollector) {
        SyntaxXmlException syntaxXmlException = new SyntaxXmlException();
        if (ranValidationEventCollector == null || ranValidationEventCollector.getEvents() == null) {
            return syntaxXmlException;
        }

        for (ValidationEvent event : ranValidationEventCollector.getEvents()) {
            ValidationEventLocator locator = event.getLocator();
            String initialErrorMessage = event.getMessage();
            int index = initialErrorMessage.indexOf(":");
            String errorType = null;
            String errorMessage;
            if (index != -1) {
                errorType = initialErrorMessage.substring(0, index);
            }

            if (errorType == null) {
                logger.error(String.format("Eroarea pentru mesajul de eroare nu a putut fi catalogata. Messajul de eroare: %s", initialErrorMessage));
                continue;
            }

            String extractedErrorMessage = initialErrorMessage.substring(index + 1);

            switch (errorType) {
                case CVC_TYPE_3_1_3:
                    errorMessage = generateCvcTypeErrorMessage(extractedErrorMessage);
                    break;
                case CVC_MINLENGTH_VALID:
                    errorMessage = generateMinLengthErrorMessage(extractedErrorMessage);
                    break;
                case CVC_ATTRIBUTE_3:
                    errorMessage = generateAttributeErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_2_4_A:
                    errorMessage = generateComplexType24aErrorMessage(extractedErrorMessage);
                    break;
                case CVC_PATTERN_VALID:
                    errorMessage = generatePatternValidErrorMessage(extractedErrorMessage);
                    break;
                case CVC_DATATYPE_VALID_1_2_1:
                    errorMessage = generateDataTypeValidErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_2_4_B:
                    errorMessage = generateComplexType24bErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_4:
                    errorMessage = generateComplexType4ErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_2_4_D:
                    errorMessage = generateComplexType24dErrorMeessage(extractedErrorMessage);
                    break;
                case CVC_MININCLUSIVE_VALID:
                    errorMessage = generateMinInclusiveErrorMessage(extractedErrorMessage);
                    break;
                case CVC_MINEXCLUSIVE_VALID:
                    errorMessage = generateMinExclusiveErrorMessage(extractedErrorMessage);
                    break;
                case CVC_MAXINCLUSIVE_VALID:
                    errorMessage = generateMaxInclusiveErrorMessage(extractedErrorMessage);
                    break;
                case CVC_FRACTIONDIGITS_VALID:
                    errorMessage = generateFractionDigitsErrorMesssage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_3_2_2:
                    errorMessage = generateComplexType322ErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_2_1:
                    errorMessage = generateComplexType21ErrorMessage(extractedErrorMessage);
                    break;
                case CVC_TYPE_3_1_1:
                    errorMessage = generateType311ErrorMessage(extractedErrorMessage);
                    break;
                case CVC_COMPLEX_TYPE_2_3:
                    errorMessage = generateComplexType23ErrorMesssage(extractedErrorMessage);
                    break;
                case CVC_ENUMERATION_VALID:
                    errorMessage = generateEnumerationValidErrorMessage(extractedErrorMessage);
                    break;
                default:
                    syntaxXmlException.addErrorDescription(String.format("%s at row: %s column: %s",event.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
                    continue;
            }

            syntaxXmlException.addErrorDescription(String.format("%s La randul: %s coloana: %s din fisierul XML.", errorMessage, locator.getLineNumber(), locator.getColumnNumber()));

        }

        return syntaxXmlException;

    }

    private String generateCvcTypeErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String fieldName = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("Valoarea '%s' a elementului '%s' nu este valida!", fieldValue, fieldName);
    }

    private String generateMinLengthErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String fieldLenght = message.substring(indexList.get(2) + 1, indexList.get(3));
        String fieldMinLength = message.substring(indexList.get(4) + 1, indexList.get(5));

        return String.format("Valoarea '%s' cu lungimea de '%s' caractere nu respecta lungimea minima de '%s' caractere!", fieldValue, fieldLenght, fieldMinLength);
    }

    private String generateAttributeErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String attributeName = message.substring(indexList.get(2) + 1, indexList.get(3));
        String elementName = message.substring(indexList.get(4) + 1, indexList.get(5));

        return String.format("Valoarea '%s' a atributului '%s' pe elementul '%s' nu este valida!", fieldValue, attributeName, elementName);
    }

    private String generateComplexType24aErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));
        String expectedElements = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("A fost gasit continut invalid incepand cu elementul '%s'. Unul din elementele '%s' este asteptat inainte de elementul '%s'!", elementName, expectedElements, elementName);
    }

    private String generatePatternValidErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String pattern = message.substring(indexList.get(2) + 1, indexList.get(3));
        String type = message.substring(indexList.get(4) + 1, indexList.get(5));
        String typeElement = type.substring(type.indexOf('_') + 1);

        return String.format("Valoarea '%s' nu este valida in ceea ce priveste tiparul '%s' pentru modelul '%s'!", fieldValue, pattern, typeElement);
    }

    private String generateDataTypeValidErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String dataType = message.substring(indexList.get(2) + 1, indexList.get(3));
        switch (dataType) {
            case "integer":
                dataType = "numar intreg";
                break;
            case "NString":
                dataType = "sir de caractere";
                break;
            case "dateTime":
                dataType = "data-timp";
                break;
            case "float":
                dataType = "numar real";
                break;
            default:
        }

        return String.format("Valoarea '%s' nu este valida pentru tipul de date cerut '%s'!", fieldValue, dataType);
    }

    private String generateComplexType24bErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));
        String expectedElements = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("Continutul elementului '%s' este incomplet. Unul din elementele '%s' este asteptat!", elementName, expectedElements, elementName);
    }

    private String generateComplexType4ErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String attributeName = message.substring(indexList.get(0) + 1, indexList.get(1));
        String elementName = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("Atributul '%s' trebuie sa apara in elementul '%s'! Un exemplu gresit este: <%s>123</%s>. Un exemplu corect este: <%s %s=\"123\"/>.", attributeName, elementName, elementName, elementName, elementName, attributeName);
    }

    //Function to take all the words in a string and return them in a list


    private String generateComplexType24dErrorMeessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));

        return String.format("A fost gasit continut invalid incepand cu elementul '%s'. Niciun element copil nu este asteptat in acest punct!", elementName);
    }

    private String generateMinInclusiveErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String minimumValue = message.substring(indexList.get(2) + 1, indexList.get(3));
        String type = message.substring(indexList.get(4) + 1, indexList.get(5));
        String typeElement = type.substring(type.indexOf('_') + 1);

        return String.format("Valoarea '%s' nu respecta valoarea minima '%s' pentru tipul '%s'!", fieldValue, minimumValue, typeElement);
    }

    private String generateMinExclusiveErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String thresholdValue = message.substring(indexList.get(2) + 1, indexList.get(3));
        String type = message.substring(indexList.get(4) + 1, indexList.get(5));
        String typeElement = type.substring(type.indexOf('_') + 1);

        return String.format("Valoarea '%s' nu respecta valoarea de prag '%s' pentru tipul '%s'! Valoare trecuta trebuie sa fie mai mare deact valoarea de prag!", fieldValue, thresholdValue, typeElement);
    }

    private String generateMaxInclusiveErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String minimumValue = message.substring(indexList.get(2) + 1, indexList.get(3));
        String type = message.substring(indexList.get(4) + 1, indexList.get(5));
        String typeElement = type.substring(type.indexOf('_') + 1);

        return String.format("Valoarea '%s' nu respecta valoarea maxima '%s' pentru tipul '%s'!", fieldValue, minimumValue, typeElement);
    }

    private String generateFractionDigitsErrorMesssage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));

        return String.format("Valoarea '%s' are prea multe zecimale. Numarul permis de zecimale a fost limitat la 2!", fieldValue);
    }

    private String generateComplexType322ErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String attributeName = message.substring(indexList.get(0) + 1, indexList.get(1));
        String elementName = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("Atributului '%s' nu-i este permis sa apara in elementul '%s'!", attributeName, elementName);
    }

    private String generateComplexType21ErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));

        return String.format("Elementul '%s' trebuie sa nu aiba informatii de tipul <%s>123</%s>!  Un exemplu corect este <%s value=\"123\"/>", elementName, elementName, elementName, elementName);
    }

    private String generateComplexType23ErrorMesssage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));

        return String.format("Elementul '%s' nu trebuie sa aiba caractere copii, poate sa aiba doar elemente! De exemplu, un element arata in felul urmator: <Nume>Andrei</Nume>. Un exemplu gresit: Numele meu este <Nume>Andrei</Nume>.", elementName);
    }

    private String generateEnumerationValidErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String fieldValue = message.substring(indexList.get(0) + 1, indexList.get(1));
        String validEnumerationElements = message.substring(indexList.get(2) + 1, indexList.get(3));

        return String.format("Valoarea '%s' nu se afla printre valorile permise '%s' pentru acest camp!", fieldValue, validEnumerationElements);
    }

    private String generateType311ErrorMessage(String message) {
        List<Integer> indexList = new ArrayList<>();
        char ch = '\'';
        int index = message.indexOf(ch);
        indexList.add(index);

        while (index != -1) {
            index = message.indexOf(ch, index + 1);
            indexList.add(index);
        }

        String elementName = message.substring(indexList.get(0) + 1, indexList.get(1));

        return String.format("Elementul '%s' este un tip simplu, nu are voie sa aiba atribute! Exemplu corect: <%s>valoare</%s>. Exemplu gresit: <%s value=\"valoare\"/>.", elementName, elementName, elementName, elementName);
    }

}
