package edu.dsa.ass.clinicmanagementsystem.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * The medicine to be prescribed to {@link Patient} for treatments.
 *
 * @author TODO
 */
public class Medicine extends IdentifiableEntity {
    private String name;
    private MedicineType type;
    private String brand;
    private BigDecimal cost;
    private BigDecimal price;
    private List<Medicine> substitutes;
    private List<Medicine> substitutesFor;
    private List<Stock> stocks;
}
