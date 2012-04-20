package org.motechproject.provider.registration.parser;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.motechproject.provider.registration.domain.Provider;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationException;
import org.motechproject.provider.registration.utils.RegistrationMapper;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class RegistrationParser<T> {
    RegistrationMapper<T> domainMapper;
    private String xmlDocument;

    public RegistrationParser(Class<T> clazz, String xmlDocument) {
        domainMapper = new RegistrationMapper<T>(clazz);
        this.xmlDocument = xmlDocument;
    }

    public T parseProvider() throws OpenRosaRegistrationException {
        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDocument));
        Provider provider;
        try {
            parser.parse(inputSource);
            provider = parseProvider(parser.getDocument());
        } catch (Exception ex) {
            throw new OpenRosaRegistrationException(ex, "Exception while trying to parse caseXml", HttpStatus.BAD_REQUEST);
        }

        return domainMapper.mapToDomainObject(provider);

    }

    private Provider parseProvider(Document document) {
        Element item = (Element) document.getElementsByTagName("Registration").item(0);
        Provider provider = createProvider(item);
        return provider;
    }

    private Provider createProvider(Element item) {
        Provider provider = new Provider();
        Element element = (Element) getMatchingNode(item, "user_data");
        NodeList childNodes = element.getElementsByTagName("data");

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("data")) {
                String key = childNode.getAttributes().getNamedItem("key").getNodeValue();
                provider.AddFieldvalue(key, childNode.getTextContent());
            }


        }
        return provider;
    }

    private Node getMatchingNode(Element ele, String tagName) {
        Node element = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            element = nl.item(0);
        }
        return element;
    }


}
