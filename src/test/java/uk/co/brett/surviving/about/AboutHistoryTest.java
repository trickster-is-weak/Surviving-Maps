package uk.co.brett.surviving.about;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class AboutHistoryTest {


    @Test
    void test() throws IOException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String toParse = "22-06-2022";
        String toParse2 = "20-06-2022";
        Date date = df.parse(toParse);
        About about = ImmutableAbout.builder()
                .addItems("Added favicon",
                        "Added KoFi link",
                        "Added Content to homepage",
                        "Added about page",
                        "Updated Resource Management")
                .date(date)
                .build();

        About about2 = ImmutableAbout.builder()
                .addItems("Added Options for B&B and B&B+GP",
                        "Added temp content to other pages")
                .date(df.parse(toParse2))
                .build();

        List<About> history = List.of(about, about2);


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        ObjectWriter writer = objectMapper.writer(printer);
        System.out.println(writer.writeValueAsString(history));
        File file = new File("changelog2.json");
        writer.writeValue(file, history);


    }


}