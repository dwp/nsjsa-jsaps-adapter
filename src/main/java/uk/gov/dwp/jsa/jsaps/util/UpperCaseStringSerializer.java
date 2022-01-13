package uk.gov.dwp.jsa.jsaps.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

public class UpperCaseStringSerializer extends JsonSerializer<String> {


    @Override
    public void serialize(
            final String s,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(s.toUpperCase(Locale.getDefault()));
    }
}
