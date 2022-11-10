package uk.co.brett.surviving.about;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;


@Service
public class AboutService {
    private static final Logger LOGGER = LogManager.getLogger(AboutService.class);

    private static final String FILE = "/ChangeLog.json";

    private List<About> aboutHistory;

    @PostConstruct
    public void parse() {
        try {
            InputStream fis = getInputStream();
            ObjectMapper mapper = new ObjectMapper().registerModule(new GuavaModule());
            CollectionType t = mapper.getTypeFactory().constructCollectionType(List.class, About.class);

            readStream(fis, mapper, t);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    void readStream(InputStream fis, ObjectMapper mapper, CollectionType t) throws IOException {
        aboutHistory = mapper.readValue(fis, t);
        Comparator<About> comp = Comparator.comparing(About::date).reversed();
        aboutHistory = aboutHistory.stream().sorted(comp).toList();
    }

    InputStream getInputStream() throws IOException {
        return AboutService.class.getResource(FILE).openStream();
    }

    public List<About> getAboutHistory() {
        return aboutHistory;
    }
}
