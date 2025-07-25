package edu.dsa.clinic.entity;

/**
 * The types of {@link Consultation} that can be categorized as.
 *
 * @author TODO
 * @see Consultation
 */
// im actually not quite which types we gonna be using so yea......
public enum ConsultationType {
    GENERAL,  // for walk-in diagnosis
    SPECIALIST,  // usually made with appointment to seek expert **advice**
    EMERGENCY,
    FOLLOW_UP  // usually made by doctors to check up on patient condition after treatment
}
