package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.stereotype.Component;

@Component
class MapperUtils {


    private static final int NINO_DIGIT_POSITION_6 = 6;
    private static final int NINO_DIGIT_POSITION_7 = 6;
    private static final int NINO_DIGIT_POSITION_8 = 8;
    private static final Integer NUM_19 = 19;
    private static final Integer NUM_39 = 39;
    private static final Integer NUM_59 = 59;
    private static final Integer NUM_79 = 79;


    public String calculateDayOfWeekBasedOnNino(final String nino) {
        String lastTwoDigits = nino.substring(NINO_DIGIT_POSITION_6, NINO_DIGIT_POSITION_8);
        Integer number = Integer.valueOf(lastTwoDigits);
        String result = "FRI";
        if (number <= NUM_19) {
            result = "MON";
        } else if (number <= NUM_39) {
            result = "TUE";
        } else if (number <= NUM_59) {
            result = "WED";
        } else if (number <= NUM_79) {
            result = "THU";
        }
        return result;
    }


    public String calculateCycleBasedOnNino(final String nino) {
        String lastDigit = nino.substring(NINO_DIGIT_POSITION_7, NINO_DIGIT_POSITION_8);
        Integer number = Integer.valueOf(lastDigit);
        return number % 2 == 0 ? "P" : "R";
    }


}
