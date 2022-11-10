package uk.co.brett.surviving.about;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableAbout.class)
public abstract class About {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MM yyyy")
    public abstract Date date();

    public abstract List<String> items();

    public String formatted() {
        return new SimpleDateFormat("dd LLLL yyyy").format(date());
    }

}
