package uk.co.brett.surviving.enums;

public enum ResourceType {

    METAL("Metal"),
    RARE_METAL("Rare Metal"),
    WATER("Water"),
    CONCRETE("Concrete");

    private final String formatted;

    ResourceType(String formatted) {
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }
}