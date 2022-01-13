package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MoneyFormatterTest {
    private static final String ZERO_POUNDS = "0.00";
    private static final String ONE_POUNDS = "1.00";
    private static final BigDecimal VALUE = BigDecimal.ONE;

    private MoneyFormatter formatter;
    private String formattedGrossAmount;

    @Test
    public void nullDefaultsToZero() {
        givenAFormatter();
        whenIFormatTheAmount(null);
        thenTheFormattedValueIs(ZERO_POUNDS);
    }

    @Test
    public void nonNullIsFormatted() {
        givenAFormatter();
        whenIFormatTheAmount(VALUE);
        thenTheFormattedValueIs(ONE_POUNDS);
    }

    private void givenAFormatter() {
        formatter = new MoneyFormatter(new BAMapperUtils());
    }

    private void whenIFormatTheAmount(final BigDecimal value) {
        formattedGrossAmount = formatter.format(value);
    }

    private void thenTheFormattedValueIs(String expectedValue) {
        assertThat(formattedGrossAmount, is(expectedValue));
    }
}
