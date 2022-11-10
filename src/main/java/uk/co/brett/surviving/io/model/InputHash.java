package uk.co.brett.surviving.io.model;

import uk.co.brett.surviving.enums.GameVariant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InputHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GameVariant variant;

    private String hash;

    public InputHash() {
    }

    public InputHash(GameVariant variant, String hash) {
        this.variant = variant;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public GameVariant getVariant() {
        return variant;
    }

    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
