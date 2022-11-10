package uk.co.brett.surviving.io;

import uk.co.brett.surviving.enums.GameVariant;

import java.util.Map;

//@Service
public class InputService {

    private final Map<GameVariant, InputFile> map;
    private final String ingestCheck;

    public InputService(Map<GameVariant, InputFile> map, String ingestCheck) {
        this.map = map;
        this.ingestCheck = ingestCheck;
    }

    public Map<GameVariant, InputFile> getMap() {
        return map;
    }

    public String getIngestCheck() {
        return ingestCheck;
    }
}
