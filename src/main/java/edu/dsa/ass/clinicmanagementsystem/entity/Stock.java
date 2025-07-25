package edu.dsa.ass.clinicmanagementsystem.entity;

import java.time.LocalDateTime;

public class Stock extends IdentifiableEntity {
    private Medicine medicine;
    private int stockInQuantity;
    private LocalDateTime stockInDate;
    private String location;
    private int quantityLeft;
}
