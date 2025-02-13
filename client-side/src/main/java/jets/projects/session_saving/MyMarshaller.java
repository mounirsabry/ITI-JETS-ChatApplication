package jets.projects.session_saving;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import jakarta.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyMarshaller {
    public boolean marshal(String fileName,
            NormalUserSavedSession session) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(
                    NormalUserSavedSession.class);
        } catch (JAXBException ex) {
            System.err.println("Failed at generating the context "
                    + "object for marshalling.");
            System.err.println(ex.getMessage());
            return false;
        }
        
        Marshaller marshaller;
        try {
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            System.err.println("Could not create the marshaller.");
            System.err.println(ex.getMessage());
            return false;
        }
        
        try {
            marshaller.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException ex) {
            System.err.println(
                    "Could not set the formatted output property.");
            System.err.println(ex.getMessage());
            return false;
        }
        
        try (FileWriter writer = new FileWriter(fileName);) {
            marshaller.marshal(session, writer);
        } catch (IOException ex) {
            System.err.println("Denied access to the output file.");
            System.err.println(ex.getMessage());
            return false;
        } catch (JAXBException ex) {
            System.err.println(
                    "Could not write/marshal the object to the file.");
            System.err.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public NormalUserSavedSession unmarshal(String fileName) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(
                    NormalUserSavedSession.class);
        } catch (JAXBException ex) {
            System.err.println("Failed at generating the context "
                    + "object for unmarshalling.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        Unmarshaller unMarshaller;
        try {
            unMarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            System.err.println("Could not create the unMarshaller.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        NormalUserSavedSession session;
        try (FileReader reader = new FileReader(fileName);) {
            session = (NormalUserSavedSession) unMarshaller.unmarshal(
            reader);
        } catch (FileNotFoundException ex) {
            System.err.println("Could not access the file for reading.");
            System.err.println(ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.err.println("Error while reading the file.");
            System.err.println(ex.getMessage());
            return null;
        } catch (JAXBException ex) {
            System.err.println("Could not read/unmarshel the file.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        return session;
    }
}
