package edu.dsa.clinic.entity;

/**
 * A person seeking advice or treatment from {@link Doctor}.
 *
 * @author daren
 */
public class Patient extends IdentifiableEntity {
    private String name;
    private Gender gender;
    private String identification;
    private String contactNumber;

    public String getName() {
        return name;
    }

    public Patient setName(String name) {
        this.name = name;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Patient setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getIdentification() {
        return identification;
    }

    public Patient setIdentification(String identification) {
        this.identification = identification;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Patient setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", identification='" + identification + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
