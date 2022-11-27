package uk.co.brett.surviving.io.model;

import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.file.InputFile;

import javax.persistence.*;

@Entity
@Table(name = "InputHash")
public class InputHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameVariant variant;

    private String filename;
    private String hash;

    public InputHash() {
    }

    public InputHash(GameVariant variant, InputFile inputFile) {
        this.variant = variant;
        this.filename = inputFile.getResourceLocation();
        this.hash = inputFile.getHash();
    }

    public InputHash(GameVariant variant, String filename, String hash) {
        this.variant = variant;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
