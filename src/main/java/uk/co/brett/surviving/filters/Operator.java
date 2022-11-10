package uk.co.brett.surviving.filters;

public enum Operator {

    NO_PREFERENCE("No Preference"),
    AT_LEAST(" At Least"),
    AT_MOST("At Most"),
    EQUAL_TO("Equal To");

    private final String formatted;

    Operator(String formatted) {
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }
}
