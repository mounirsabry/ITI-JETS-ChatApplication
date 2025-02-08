package jets.projects.entities;

public class Contact {
    private int firstID;
    private int secondID;
    private ContactGroup contactGroup;

    public Contact() {
        firstID = -1;
        secondID = -1;
        contactGroup = ContactGroup.OTHER;
    }

    public Contact(int firstID, int secondID, ContactGroup contactGroup) {
        this.firstID = firstID;
        this.secondID = secondID;
        this.contactGroup = contactGroup;
    }

    public int getFirstID() {
        return firstID;
    }

    public int getSecondID() {
        return secondID;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    public void setFirstID(int firstID) {
        this.firstID = firstID;
    }

    public void setSecondID(int secondID) {
        this.secondID = secondID;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Contact");
        builder.append('{');
        
        builder.append("firstID=");
        builder.append(firstID);
        
        builder.append(", secondID=");
        builder.append(secondID);
        
        builder.append(", contactGroup=");
        builder.append(contactGroup.toString());
        
        builder.append('}');
        return builder.toString();
    }
}
