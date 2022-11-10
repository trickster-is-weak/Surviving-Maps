package uk.co.brett.surviving.enums;

public enum DisasterType {

    DUST_DEVILS("Dust Devils", "dustDevils"),
    DUST_STORMS("Dust Storms", "dustStorms"),
    METEORS("Meteors", "meteors"),
    COLD_WAVES("Cold Waves", "coldWaves");

    private final String formatted;

    private final String camel;

    DisasterType(String formatted, String camel) {
        this.formatted = formatted;
        this.camel = camel;
    }

    public String getCamel() {
        return camel;
    }

    public String getFormatted() {
        return formatted;
    }
}
