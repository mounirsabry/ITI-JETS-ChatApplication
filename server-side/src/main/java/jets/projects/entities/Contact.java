package jets.projects.entities;

import java.io.Serializable;
import java.sql.Blob;

public class Contact implements Serializable{
    private int userID;
    private int contactID;
    private ContactGroup contactGroup;
    private String name ;
    private Blob picture;

    public Contact() {
        userID = -1;
        contactID = -1;
        contactGroup = ContactGroup.OTHER;
        name = "";  
        picture = null;
     
    }

    public Contact(int userID, int contactID, ContactGroup contactGroup , String name ,Blob picture) {
        this.userID = userID;
        this.contactID = contactID;
        this.contactGroup = contactGroup;
        this.name =name;
        this.picture=picture;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }
    public String getName() {
        return name;  
    }

    public Blob getPicture() {
        return picture;  
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }
    public void setName(String name) {
        this.name = name;  
    }

    public void setPicture(Blob picture) {
        this.picture = picture;  
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(Contact.class.getName());
        builder.append('{');
        
        builder.append("userID=");
        builder.append(userID);
        
        builder.append(", contactID=");
        builder.append(contactID);
        
        builder.append(", contactGroup=");
        builder.append(contactGroup.toString());

        builder.append(", name=");
        builder.append(name);  
        
        builder.append(", picture=");
        builder.append(picture);  


        builder.append('}');
        return builder.toString();
    }
}
