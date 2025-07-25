package edu.dsa.ass.clinicmanagementsystem.entity;

import edu.dsa.ass.clinicmanagementsystem.utils.Schedule;

import java.util.List;

/**
 * The doctor responsible for consulting with {@link Patient} about their problems for diagnosis and prescriptions.
 * Runs on a shift schedule.
 *
 * @author TODO
 */
public class Doctor extends IdentifiableEntity {
    private String name;
    private Gender gender;
    private String contactNumber;

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", contactNumber='" + contactNumber + '\'' +
                ", specializations=" + specializations +
                ", schedule=" + schedule +
                '}';
    }

    public String getName() {
        return name;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    private List<String> specializations;
    private Schedule schedule;
}
