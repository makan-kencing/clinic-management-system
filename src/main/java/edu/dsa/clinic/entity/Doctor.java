package edu.dsa.clinic.entity;

import edu.dsa.clinic.utils.Schedule;

/**
 * The doctor responsible for consulting with {@link Patient} about their problems for {@link Diagnosis} and {@link Prescription}.
 * Runs on a shift schedule.
 *
 * @author Daren is very handsome
 */
public class Doctor extends IdentifiableEntity {
    private String name;
    private Gender gender;
    private String contactNumber;
    private Specialization specialization;
    private Schedule schedule;

    public String getName() {
        return name;
    }

    public Doctor setName(String name) {
        this.name = name;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Doctor setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Doctor setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Doctor setSpecialization(Specialization specialization) {
        this.specialization = specialization;
        return this;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Doctor setSchedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", contactNumber='" + contactNumber + '\'' +
                ", specialization=" + specialization +
                ", schedule=" + schedule +
                '}';
    }
}
