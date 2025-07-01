package edu.dsa.ass.clinicmanagementsystem.entity;

/**
 * The amount of {@link Medicine} to be prescribed for a {@link Treatment}.
 *
 * @author TODO
 * @see Medicine
 * @see Treatment
 */
public class Prescription {
    private Medicine medicine;
    private int quantity;  // TODO: deal with multiple kinds of uom, like strips, boxes, litres, and etc
    private String notes;
}
