package ke.co.example.weaversoft.crbclientregister.model;

import java.util.Date;

/**
 * Created by weaversoft on 2/17/2015.
 */
public class ClientDetails {
    private Long clientId;
    private String nationalId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String nationality;
    private String physicalAddress;
    private Date dateOfBirth;
    private String occupation;
    private String nationalIdPhoto;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {

        this.nationalId = nationalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNationalIdPhoto() {
        return nationalIdPhoto;
    }

    public void setNationalIdPhoto(String nationalIdPhoto) {
        this.nationalIdPhoto = nationalIdPhoto;
    }




}
