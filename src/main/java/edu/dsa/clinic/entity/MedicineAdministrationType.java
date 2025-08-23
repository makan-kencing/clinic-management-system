package edu.dsa.clinic.entity;

/**
 * The type of administration method of a {@link Product} for the specified {@link Medicine}
 *
 * @author makan-kencing
 */
// https://en.wikipedia.org/wiki/Medication#By_Administration
public enum MedicineAdministrationType {
    // Oral and Mucosal
    ORAL("Oral", "Taken by mouth; the most common route of administration"),
    SUBLINGUAL("Sublingual", "Placed under the tongue for rapid absorption into the bloodstream"),
    BUCCAL("Buccal", "Placed between the gum and cheek; absorbed through oral mucosa"),
    // Local Application
    TOPICAL("Topical", "Applied directly to the skin or mucous membranes to treat localized conditions"),
    OPHTHALMIC("Ophthalmic", "Applied to the eyes for local treatment "),
    OTIC("Otic", "Applied into the ear canal for local treatment "),
    NASAL("Nasal", "Sprayed or instilled into the nose for local or systemic absorption"),
    // Inhalation
    INHALERS("Inhalers", "Delivers medication directly to the lungs, most often used for respiratory conditions"),
    NEBULIZED("Nebulized", "Delivered as fine mist via nebuilizer especially for children or severe respiratory cases"),
    // Parenteral (injectable)
    INTRAMUSCULAR("Intramuscular", "Injected into a muscle, typically the deltoid or gluteus. Commonly used for vaccines and fast-acting medications"),
    SUBCUTANEOUS("Subcutaneous", "Injected into the fatty tissue beneath the skin. Commonly used for biologics, hormones, and insulin."),
    INTRAVENOUS("Intravenous", "Injected directly into a vein for immediate systemic effect. Commonly used for emergency medications, fluids and chemotherapy"),
    INTRADERMAL("Intradermal", "Injected into the dermis (just under the skin surface). Commonly used for allergy tests, tuberculosis screening");

    public final String type;
    public final String description;

    MedicineAdministrationType(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
