package uk.co.brett.surviving.about;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AboutServiceTest {

    private AboutService aboutService;

    @BeforeEach
    public void before() {
        aboutService = new AboutService();
    }

    @Test
    void parse() {

        assertThat(aboutService.getAboutHistory()).isNull();
        aboutService.parse();
        assertThat(aboutService.getAboutHistory()).isNotEmpty();

    }

    @Test
    void getAboutHistory() {
        aboutService.parse();
        List<About> a = aboutService.getAboutHistory();
        List<Date> d = a.stream().map(About::date).toList();
        Comparator<Date> comp = Comparator.reverseOrder();
        assertThat(d).isSortedAccordingTo(comp);
    }

    @Test
    void getInputStream() throws IOException {
        assertThat(aboutService.getInputStream()).isNotNull();
    }

    @Test
    void readStream() throws IOException, ParseException {
        InputStream fis = IOUtils.toInputStream("""
                [  {
                    "date": "19 06 2022",
                    "items": [
                      "Added Options for B&B and B&B+GP",
                      "Added temp content to other pages"
                    ]
                  }]""", Charset.defaultCharset());


        Date date = new SimpleDateFormat("dd MM yyyy").parse("19 06 2022");
        About exp = ImmutableAbout.builder()
                .date(date)
                .addItems("Added Options for B&B and B&B+GP",
                        "Added temp content to other pages")
                .build();
        ObjectMapper mapper = new ObjectMapper().registerModule(new GuavaModule());
        CollectionType t = mapper.getTypeFactory().constructCollectionType(List.class, About.class);

        aboutService.readStream(fis, mapper, t);
        List<About> a = aboutService.getAboutHistory();
        assertThat(a).isNotEmpty();
        assertThat(a.get(0).items()).isEqualTo(exp.items());
        assertThat(a.get(0).date()).isEqualToIgnoringHours(exp.date());
    }
}