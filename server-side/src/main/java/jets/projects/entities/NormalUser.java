package jets.projects.entities;

import java.io.Serializable;
import java.util.Date;

public class NormalUser implements Serializable{
    private int userID;
    private String displayName;
    private String phoneNumber;
    private String email;
    private byte[] pic;
    private String password;
    private Gender gender;
    private String country;
    private Date birthDate;
    private Date createdAt;
    private NormalUserStatus status;
    private String bio;
    private boolean isAdminCreated;
    private boolean isPasswordValid;

    public NormalUser() {
        userID = -1;
        displayName = "Not Specified";
        phoneNumber = "Not Specified";
        email = "Not Specified";
        pic = null;
        password = "";
        gender = Gender.MALE;
        country = "Not Specified";
        birthDate = null;
        createdAt = null;
        status = NormalUserStatus.OFFLINE;
        bio = "";
        isAdminCreated = false;
        isPasswordValid = true;
    }

    public int getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPic() {
        return pic;
    }

    public String getPassword() {
        return password;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public NormalUserStatus getStatus() {
        return status;
    }

    public String getBio() {
        return bio;
    }

    public boolean isIsAdminCreated() {
        return isAdminCreated;
    }

    public boolean isIsPasswordValid() {
        return isPasswordValid;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(NormalUserStatus status) {
        this.status = status;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setIsAdminCreated(boolean isAdminCreated) {
        this.isAdminCreated = isAdminCreated;
    }

    public void setIsPasswordValid(boolean isPasswordValid) {
        this.isPasswordValid = isPasswordValid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("NormalUser");
        builder.append('{');
        
        builder.append("userID=");
        builder.append(userID);
        
        builder.append(", displayName=");
        builder.append(displayName);
        
        builder.append(", phoneNumber=");
        builder.append(phoneNumber);
        
        builder.append(", email=");
        builder.append(email);
        
        builder.append(", pic=");
        builder.append(pic == null ? "null" : "Cannot be displayed here."); 
        
        builder.append(", password=");
        builder.append(password);
        
        builder.append(", gender=");
        builder.append(gender);
        
        builder.append(", country=");
        builder.append(country);
        
        builder.append(", birthDate=");
        builder.append(birthDate);
        
        builder.append(", createdAt=");
        builder.append(createdAt);
        
        builder.append(", status=");
        builder.append(status);
        builder.append('.');
        
        builder.append("\nbio=");
        builder.append(bio);
        
        builder.append(", isAdminCreated=");
        builder.append(isAdminCreated);
        
        builder.append(", isPasswordValid=");
        builder.append(isPasswordValid);
        
        builder.append('}');
        return builder.toString();
    }
}
