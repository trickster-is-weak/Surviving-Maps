package uk.co.brett.surviving.io.config;

import org.immutables.value.Value;

@Value.Immutable
public interface DatabaseSettings {

    static DatabaseSettings of(String directory, String name) {
        return ImmutableDatabaseSettings.builder().directory(directory).name(name).build();
    }

    String directory();

    String name();

}
