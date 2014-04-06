/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author Catalina
 */
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JAXBXMLHandler {

    // Import: Unmarshalling
    public static Document unmarshal(File importFile) throws JAXBException {
        Document document = null;
        JAXBContext context;

        context = JAXBContext.newInstance(Document.class);
        Unmarshaller um = context.createUnmarshaller();
        document = (Document) um.unmarshal(importFile);

        return document;
    }
}
