package edu.dsa.clinic.entity;

/**
 * The types of {@link Consultation} that can be categorized as.
 *
 * @author tan
 * @see Consultation
 */
public enum ConsultationType {
    GENERAL,  // for walk-in diagnosis
    SPECIALIST,  // usually made with appointment to seek expert **advice**
    EMERGENCY,
    FOLLOW_UP  // usually made by doctors to check up on patient condition after treatment
}
