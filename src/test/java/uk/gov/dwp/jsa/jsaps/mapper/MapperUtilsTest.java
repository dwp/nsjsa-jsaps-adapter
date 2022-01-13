package uk.gov.dwp.jsa.jsaps.mapper;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class MapperUtilsTest {


    private static final String NINO = "QQ1234%sD";
    private MapperUtils classToBeTested = new MapperUtils();

    @Test
    @Parameters({
            "05,MON",
            "25,TUE",
            "45,WED",
            "65,THU",
            "85,FRI"})
    public  void givenNinoWhenCalculateDayOfWeekBasedOnNinoThenDateMatches(String number, String date){
        String formattedNino = String.format(NINO,number);
        assertThat(classToBeTested.calculateDayOfWeekBasedOnNino(formattedNino),is(date));
    }

    @Test
    @Parameters({
            "02,P",
            "03,R"})
    public void givenNinoWhenCalculateCycleBasedOnNinoThenCycleMatches(String number, String cycle){


        String formattedNino = String.format(NINO,number);
        assertThat(classToBeTested.calculateCycleBasedOnNino(formattedNino),is(cycle));
    }
}
