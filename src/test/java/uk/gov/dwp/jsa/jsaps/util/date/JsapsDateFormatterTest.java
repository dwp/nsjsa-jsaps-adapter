package uk.gov.dwp.jsa.jsaps.util.date;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class JsapsDateFormatterTest {

    private static final LocalDate DATE = LocalDate.of(2018, 12, 31);
    private static final String FORMATTED_DATE = "311218";
    private JsapsDateFormatter formatter;
    private String formattedDate;
    private LocalDate parsedDate;

    @Test
    public void formatsDate() {
        givenAFormatter();
        whenIFormatTheDate();
        thenTheDateIsFormatted();
    }

    @Test
    public void nullDate() {
        givenAFormatter();
        whenIFormatANullDate();
        thenTheFormattedDateIsNull();
    }

    @Test
    public void parsesNullDate(){
        givenAFormatter();
        whenDateStringIsNull();
        theTheParsedDateIsNull();
    }

    @Test
    public void parsesEmptyDate(){
        givenAFormatter();
        whenDateStringIsEmpty();
        theTheParsedDateIsNull();
    }

    @Test
    public void parsesInvalidDate(){
        givenAFormatter();
        whenDateStringIsInvalid();
        theTheParsedDateIsNull();
    }

    private void theTheParsedDateIsSet() {
        assertThat(parsedDate, is(DATE));
    }

    private void theTheParsedDateIsNull() {
        assertNull(parsedDate);
    }

    private void whenDateStringIsNull() {
        formattedDate = null;
    }

    private void whenDateStringIsEmpty() {
        formattedDate = "";
    }

    private void whenDateStringIsInvalid() {
        formattedDate = "asdfasdfasdf";
    }

    private void whenIFormatANullDate() {
        formattedDate = formatter.format(null);
    }

    private void givenAFormatter() {
        formatter = new JsapsDateFormatter();
    }

    private void whenIFormatTheDate() {
        formattedDate = formatter.format(DATE);
    }

    private void thenTheDateIsFormatted() {
        assertThat(formattedDate, is(FORMATTED_DATE));
    }

    private void thenTheFormattedDateIsNull() { assertNull(formattedDate); }
}
