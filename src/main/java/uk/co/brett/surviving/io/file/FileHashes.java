package uk.co.brett.surviving.io.file;

import uk.co.brett.surviving.enums.GameVariant;

import java.util.Map;

public class FileHashes {

    private final Map<GameVariant, InputFile> map;


    public FileHashes(Map<GameVariant, InputFile> map) {
        this.map = map;
    }

    public Map<GameVariant, InputFile> getMap() {
        return map;
    }


}
