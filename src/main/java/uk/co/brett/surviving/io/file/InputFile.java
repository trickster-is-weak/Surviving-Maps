package uk.co.brett.surviving.io.file;

import com.google.common.base.Objects;

public class InputFile {
    private String resourceLocation;

    private String hash;

    public InputFile() {
    }

    public InputFile(String resourceLocation, String hash) {
        this.resourceLocation = resourceLocation;
        this.hash = hash;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputFile inputFile = (InputFile) o;
        return Objects.equal(resourceLocation, inputFile.resourceLocation) && Objects.equal(hash, inputFile.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceLocation, hash);
    }
}
