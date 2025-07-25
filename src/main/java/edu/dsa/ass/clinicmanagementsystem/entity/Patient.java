package edu.dsa.ass.clinicmanagementsystem.entity;

/**
 * A person seeking advice or treatment from {@link Doctor}.
 *
 * @author TODO
 */
public class Patient extends IdentifiableEntity {
    private String name;
    private Gender gender;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", identification='" + identification + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    private String identification;
    private String contactNumber;
}
