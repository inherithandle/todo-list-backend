package com.gtchoi.todolistbackend.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class JaxbUtil {

    public static String marshal(Object o, Class clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        m.marshal(o, sw);
        return sw.toString();
    }

    public static boolean canMarshal(Object o, Class clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller m = context.createMarshaller();
            StringWriter sw = new StringWriter();
            m.marshal(o, sw);
        } catch (JAXBException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Object unmarshal(String xmlString, Class clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller um = context.createUnmarshaller();
        Object unmarshal = um.unmarshal(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
        return unmarshal;
    }

    public static boolean canUnmarshal(String xmlString, Class clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            um.unmarshal(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
