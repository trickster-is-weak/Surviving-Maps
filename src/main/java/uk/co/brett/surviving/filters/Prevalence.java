package uk.co.brett.surviving.filters;

import java.util.function.Predicate;

public enum Prevalence {

    NA(0, "No Preference"),
    LOW(1, "Low"),
    MED(2, "Medium"),
    HIGH(3, "High");

    private final int rating;

    private final String formatted;

    Prevalence(int i, String string) {
        rating = i;
        formatted = string;
    }

    public static Predicate<Prevalence> atMost(Prevalence prevalence) {
        return prevalence1 -> prevalence1.getRating() <= prevalence.getRating();
    }

    public static Predicate<Prevalence> equalTo(Prevalence prevalence) {
        return prevalence1 -> prevalence.getRating() == prevalence1.getRating();
    }

    public static Predicate<Prevalence> atLeast(Prevalence prevalence) {
        return prevalence1 -> prevalence1.getRating() >= prevalence.getRating();
    }

    public static Predicate<Prevalence> all() {
        return prevalence1 -> prevalence1.getRating() > 0;
    }

    public String getFormatted() {
        return formatted;
    }

    public int getRating() {
        return rating;
    }


}
