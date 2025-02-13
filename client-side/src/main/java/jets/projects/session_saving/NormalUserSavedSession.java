package jets.projects.session_saving;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"phoneNumber", "password"})
public class NormalUserSavedSession {
    private String phoneNumber;
    private String password;
    
    public NormalUserSavedSession() {
        phoneNumber = "Not Specified";
        password = "Not Specified";
    }

    public NormalUserSavedSession(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NormalUserSavedSession");
        builder.append('{');
        
        builder.append("phoneNumber=");
        builder.append(phoneNumber);
        
        builder.append(", password=");
        builder.append(password);
        
        builder.append('}');
        return builder.toString();
    }
}
