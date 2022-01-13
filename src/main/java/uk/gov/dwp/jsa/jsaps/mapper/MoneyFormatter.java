package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyFormatter {
    private static final String ZERO_POUNDS = "0.00";

    private final BAMapperUtils baMapperUtils;


    public MoneyFormatter(final BAMapperUtils baMapperUtils) {
        this.baMapperUtils = baMapperUtils;
    }

    public String format(final BigDecimal grossAmount) {
        return getGrossAmountORDefaultToZero(grossAmount);
    }

    private String getGrossAmountORDefaultToZero(final BigDecimal grossAmount) {
        String formattedAmount = ZERO_POUNDS;
        if (grossAmount != null) {
            formattedAmount = baMapperUtils.format2Decimals(grossAmount);
        }
        return formattedAmount;
    }
}

