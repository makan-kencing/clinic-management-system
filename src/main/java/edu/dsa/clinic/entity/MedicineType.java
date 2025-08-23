package edu.dsa.clinic.entity;

/**
 * The general type of {@link Product}.
 *
 * @author makan-kencing
 */
// https://en.wikipedia.org/wiki/Medication#Classification_By_Function
public enum MedicineType {
    ANALGESICS("Analgesics", "Used to relieve pain"),
    ANTIBIOTICS("Antibiotics", "Used to kill or inhibit the growth of bacteria"),
    ANTIFUNGALS("Antifungals", "Used to kill or inhibit the growth of fungal pathogens"),
    ANTIHISTAMINES("Antihistamines", "Used to reduce allergy symptoms"),
    ANTIPYRETICS("Antipyretics", "Used to reduce fever"),
    ANTIVIRALS("Antivirals", "Used to kill or inhibit the growth of viral pathogens"),
    ANTICOAGULANTS("Anticoagulants", "Used to prevent blood clot formation"),
    ANTIDEPRESSANTS("Antidepressants", "Used to manage depression and related mental health conditions"),
    ANTIENOPLASTICS("Antienoplastics", "Used in the treatment of cancer"),
    ANTIPSYCHOTICS("Antipsychotics", "Used to manage severe mental health conditions (such as schizophrenia, treatment-resistant depression)"),
    BRONCHODILATORS("Bronchodilators", "Used to open the airways in the lungs"),
    CORTICOSTEROIDS("Corticosteroids", "Used to reduce inflammation throughout the body"),
    MOOD_STABILIZERS("Mood Stabilizers", "Used to treat bipolar disorder and other mood-related conditions"),
    STATINS("Statins", "Used to lower blood cholesterol levels"),
    ANTACIDS("Antacids", "Neutralize stomach acid to relieve heartburn and indigestion"),
    PROTON_PUMP_INHIBITORS ("Proton pump inhibitors (PPIs)", "Reduce stomach acid production by blocking proton pumps"),
    H2_RECEPTOR_ANTAGONISTS("H2 receptor antagonists", "Reduce stomach acid by blocking histamine H2 receptors"),
    ANTIEMETICS("Antiemetics", "Used to prevent or treat nausea and vomiting"),
    ANTICONVULSANTS("Anticonvulsants", "Used to manage or prevent seizures"),
    DIURETICS("Diuretics", "Promote urine production to reduce fluid overload and lower blood pressure"),
    BETA_BLOCKERS("Beta blockers", "Lower heart rate and blood pressure by blocking beta-adrenergic receptors"),
    CALCIUM_CHANNEL_BLOCKERS("Calcium channel blockers", "Relax blood vessels and reduce blood pressure"),
    ACE_INHIBITORS("ACE Inhibitors", "Lower blood pressure by inhibiting angiotensin-converting enzyme"),
    ANTIDIABETICS("Antidiabetics", "Used to manage blood glucose levels in diabetes"),
    THYROID_HORMONES("Thyroid hormones", "Replace or supplement thyroid hormone levels"),
    HORMONAL_CONTRACEPTIVES("Hormonal contraceptives", "Prevent pregnancy by altering hormonal regulation"),
    SEDATIVES("Sedatives", "Induce or maintain sleep, reduce anxiety"),
    STIMULANTS("Stimulants", "Enhance alertness, attention, and energy"),
    IMMUNOSUPPRESSANTS("Immunosuppressants", "Suppress immune response to prevent organ rejection or treat autoimmune disease"),
    VACCINES("Vaccines", "Stimulate the immune system to prevent infectious disease");

    public final String type;
    public final String description;

    MedicineType(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
