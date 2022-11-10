package uk.co.brett.surviving.io;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import uk.co.brett.surviving.enums.GameVariant;

import java.util.Map;

@Value.Immutable
@JsonDeserialize(as = ImmutableIngest.class)
@JsonSerialize (as = ImmutableIngest.class)
public interface Ingest {

    Map<GameVariant, InputFile> map();

    Boolean complete();

}
