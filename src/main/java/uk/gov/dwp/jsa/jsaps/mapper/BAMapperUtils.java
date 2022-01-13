package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Component
public class BAMapperUtils {


    public static final String FORTNIGHTLY = "fortnightly";
    public static final String FOURWEEKLY = "fourweekly";
    public static final String MONTHLY = "monthly";
    public static final String WEEKLY = "weekly";
    public static final String QUARTERLY = "quarterly";
    public static final String BIANNUALLY = "biannually";
    public static final String ANNUALLY = "annually";
    private static final int MORE_THAN_TWO_DIGITS = 10;
    private static final String WEEKLY_PXP = "2";
    private static final String FORTNIGHTLY_PXP = "3";
    private static final String WEEKLY_4_PXP = "4";
    private static final String MONTHLY_PXP = "5";
    private static final String QUARTERLY_PXP = "6";
    private static final String ANNUALLY_PXP = "7";
    private static final String BI_ANNUALLY_PXP = "8";

    private static final BigDecimal WEEKS_IN_YEAR = new BigDecimal(52);
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal FOUR = new BigDecimal(4);
    private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal(12);

    public String formatHoursPerWeek(final int hoursPerWeek) {
        String format = "0%d";
        if (hoursPerWeek >= MORE_THAN_TWO_DIGITS) {
            format = "%d";
        }
        return String.format(format, hoursPerWeek);

    }

    public String format2Decimals(final BigDecimal bd) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);
        return df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));
    }

    public void addIfNotBlank(final List<String> listToAdd, final String value) {
        if (StringUtils.isNotBlank(value)) {
            listToAdd.add(value);
        }
    }

    public String translatePaymentFrequency(final String paymentFrequency) {
        String result = WEEKLY_PXP;
        if (paymentFrequency != null) {
            String lowerCaseFrequency = paymentFrequency.toLowerCase(Locale.getDefault());
            if (lowerCaseFrequency.equals(FORTNIGHTLY)) {
                result = FORTNIGHTLY_PXP;
            } else if (lowerCaseFrequency.equals(FOURWEEKLY)) {
                result = WEEKLY_4_PXP;
            } else if (lowerCaseFrequency.equals(MONTHLY)) {
                result = MONTHLY_PXP;
            } else if (lowerCaseFrequency.equals(QUARTERLY)) {
                result = QUARTERLY_PXP;
            } else if (lowerCaseFrequency.equals(ANNUALLY)) {
                result = ANNUALLY_PXP;
            } else if (lowerCaseFrequency.equals(BIANNUALLY)) {
                result = BI_ANNUALLY_PXP;
            }
        }
        return result;
    }

    public BigDecimal toWeeklyAmount(final BigDecimal grossPay, final String paymentFrequency) {
        BigDecimal result = grossPay;
        String lowerCaseFrequency = paymentFrequency.toLowerCase(Locale.getDefault());
        if (lowerCaseFrequency.equals(FORTNIGHTLY)) {
            result = grossPay.divide(TWO, 2, BigDecimal.ROUND_HALF_DOWN);
        } else if (lowerCaseFrequency.equals(FOURWEEKLY)) {
            result = grossPay.divide(FOUR, 2, BigDecimal.ROUND_HALF_DOWN);
        } else if (lowerCaseFrequency.equals(MONTHLY)) {
            result = grossPay.multiply(MONTHS_IN_YEAR).divide(WEEKS_IN_YEAR, 2, BigDecimal.ROUND_HALF_DOWN);
        } else if (lowerCaseFrequency.equals(QUARTERLY)) {
            result = grossPay.multiply(FOUR).divide(WEEKS_IN_YEAR, 2, BigDecimal.ROUND_HALF_DOWN);
        } else if (lowerCaseFrequency.equals(ANNUALLY)) {
            result = grossPay.divide(WEEKS_IN_YEAR, 2, BigDecimal.ROUND_HALF_DOWN);
        } else if (lowerCaseFrequency.equals(BIANNUALLY)) {
            result = grossPay.multiply(TWO).divide(WEEKS_IN_YEAR, 2, BigDecimal.ROUND_HALF_DOWN);
        }
        return result;
    }
}
