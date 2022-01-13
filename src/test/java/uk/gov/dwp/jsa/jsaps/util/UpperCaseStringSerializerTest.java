package uk.gov.dwp.jsa.jsaps.util;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpperCaseStringSerializerTest {

    private static final String LOWER_CASE_HELLO = "hello";
    private static final String UPPEER_CASE_HELLO = LOWER_CASE_HELLO.toUpperCase();

    @Mock
    private JsonGenerator jsonGenerator;

    private UpperCaseStringSerializer serializer;

    @Before
    public void beforeEachTest() {
        initMocks(this);
    }

    @Test
    public void serializesStringToUpperCase() throws IOException {
        givenASerializer();
        whenISerializeAString();
        thenTheStringIsWrittenInUppercase();
    }

    private void givenASerializer() {
        serializer = new UpperCaseStringSerializer();
    }

    private void whenISerializeAString() throws IOException {
        serializer.serialize(LOWER_CASE_HELLO, jsonGenerator, null);
    }

    private void thenTheStringIsWrittenInUppercase() throws IOException {
        verify(jsonGenerator).writeString(UPPEER_CASE_HELLO);
    }
}
