package uk.co.brett.surviving.io;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CsvReader {
    public <T> List<T> read(Reader reader, Class<T> classType) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());

        CsvMapper csvMapper = CsvMapper.builder()
                .enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS)
//                .enable(CsvParser.Feature.EMPTY_STRING_AS_NULL)
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                .build();
        csvMapper.registerModule(new Jdk8Module());

        CsvSchema schema = csvMapper.typedSchemaFor(classType).withHeader();
        MappingIterator<T> csvItr = csvMapper.readerFor(classType).with(schema).readValues(reader);
        return csvItr.readAll();
    }
}
