package uk.co.brett.surviving.filters;

public enum Difficulties {

    D100(100),
    D120(120),
    D140(140),
    D160(160),
    D180(180),
    D200(200),
    D220(220),
    D240(240);

    private final int value;

    Difficulties(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
