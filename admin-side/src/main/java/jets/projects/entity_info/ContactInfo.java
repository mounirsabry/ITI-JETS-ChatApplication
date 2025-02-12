package jets.projects.entity_info;

import jets.projects.entities.Contact;

import java.io.Serializable;

public class ContactInfo implements Serializable {
    private Contact contact;
    private String name;
    private byte[] pic;

    public ContactInfo() {
        contact = null;
        name = "Not Specified";
        pic = null;
    }

    public ContactInfo(Contact contact, String name, byte[] pic) {
        this.contact = contact;
        this.name = name;
        this.pic = pic;
    }

    public Contact getContact() {
        return contact;
    }
    
    public String getName() {
        return name;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("ContactInfo");
        builder.append('{');
        
        builder.append(contact);
        
        builder.append(", name=");
        builder.append(name);
        
        builder.append(", pic=");
        builder.append(pic == null ? "null" : "Cannot be displayed here.");
        
        builder.append('}');
        return builder.toString();
    }
}
