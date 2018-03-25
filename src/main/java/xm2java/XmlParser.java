/*
 * To change this license header, choose License Headers in Project RootEntity.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xm2java;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author turczyt
 */
public class XmlParser {

    public static RootEntity getProperties(String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RootEntity.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        RootEntity root = (RootEntity) jaxbUnmarshaller.unmarshal(new File(path));
        return root;
    }
}
