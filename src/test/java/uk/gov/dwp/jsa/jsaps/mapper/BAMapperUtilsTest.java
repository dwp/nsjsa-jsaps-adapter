package uk.gov.dwp.jsa.jsaps.mapper;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class BAMapperUtilsTest {

    private BAMapperUtils classToBeTested;

    @Before
    public void setup(){
        classToBeTested = new BAMapperUtils();
    }

    @Test
    @Parameters({
            "5,05",
            "10,10",
            "12,12"})
    public void givenHourWithDigitWhenHoursPerWeekThenValueHasTwoDigits(Integer numberHours, String expectedResult){
        String result = classToBeTested.formatHoursPerWeek(numberHours);
        assertThat(result,is(expectedResult));
    }

    @Test
    @Parameters({
            "0,0.00",
            "5,5.00",
            "12.3,12.30",
            "22.33,22.33"})
    public void givenBigDecimalWhenFormat2DecimalsThenStringHasTwoDecimals(BigDecimal amountToFormat, String expectedResult){
        String result = classToBeTested.format2Decimals(amountToFormat);
        assertThat(result,is(expectedResult));
    }

    @Test
    @Parameters({
            "null,2",
            "weekly,2",
            "fortnightly,3",
            "fourweekly,4",
            "monthly,5",
            "quarterly,6",
            "annually,7",
            "biannually,8"})
    public void givenPaymentPeriodsWhenTranslateToJsapsThenValuesMatch(String dwp, String expected){
        String result = classToBeTested.translatePaymentFrequency(dwp);
        assertThat(result,is(expected));
    }

    @Test
    @Parameters({
            "null,1000",
            "weekly,1000",
            "fortnightly,500.00",
            "fourweekly,250.00",
            "monthly,230.77",
            "quarterly,76.92",
            "annually,19.23",
            "biannually,38.46"})
    public void givenPensionPaymentsOfVariousFrequenciesThenConvertsToWeeklyAmounts(String frequency, BigDecimal expected) {
        BigDecimal grossPayment = new BigDecimal(1000);
        BigDecimal result = classToBeTested.toWeeklyAmount(grossPayment, frequency);
        assertThat(result, is(expected));
    }
}
