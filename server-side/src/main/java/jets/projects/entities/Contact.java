package jets.projects.entities;

public class Contact {
    private int userID;
    private int contactID;
    private ContactGroup contactGroup;

    public Contact() {
        userID = -1;
        contactID = -1;
        contactGroup = ContactGroup.OTHER;
    }

    public Contact(int userID, int contactID, ContactGroup contactGroup) {
        this.userID = userID;
        this.contactID = contactID;
        this.contactGroup = contactGroup;
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

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
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

        builder.append('}');
        return builder.toString();
    }
}
