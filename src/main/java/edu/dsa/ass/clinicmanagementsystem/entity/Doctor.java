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
    private List<String> specializations;
    private Schedule schedule;
}
