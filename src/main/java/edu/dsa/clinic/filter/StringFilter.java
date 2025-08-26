package edu.dsa.clinic.filter;

import edu.dsa.clinic.lambda.Filter;

public interface StringFilter {
    static Filter<String> like(String target, boolean ignoreCase) {
        return source -> {
            if (ignoreCase)
                return source.toLowerCase().contains(target.toLowerCase());
            return source.contains(target);
        };
    }
}
