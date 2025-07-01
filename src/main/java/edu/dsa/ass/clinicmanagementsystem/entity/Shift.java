package edu.dsa.ass.clinicmanagementsystem.entity;

import edu.dsa.ass.clinicmanagementsystem.utils.Range;

import java.time.LocalTime;
import java.util.List;

/**
 * The working hours defined for a {@link Doctor}.
 *
 * @author TODO
 * @see Doctor
 */
public class Shift {
    private Doctor doctor;
    private Range<LocalTime> shift;
    private List<Range<LocalTime>> breaks;
}
