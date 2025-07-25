package edu.dsa.ass.clinicmanagementsystem.entity;

/**
 * A person seeking advice or treatment from {@link Doctor}.
 *
 * @author TODO
 */
public class Patient extends IdentifiableEntity {
    private String name;
    private Gender gender;
    private String identification;
    private String contactNumber;
}
