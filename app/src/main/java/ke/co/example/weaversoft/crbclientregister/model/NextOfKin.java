package ke.co.example.weaversoft.crbclientregister.model;

import java.io.Serializable;

/**
 * Created by weaversoft on 2/24/2015.
 */
public class NextOfKin implements Serializable {
    private Long nextOfKinId;
    private String firstName;
    private String lastName;
    private String relationship;
    private String phoneNumber;
    private String emailAddress;
    private String nationalId;
    private Long customerId;

    public Long getNextOfKinId() {
        return nextOfKinId;
    }

    public void setNextOfKinId(Long nextOfKinId) {
        this.nextOfKinId = nextOfKinId;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
