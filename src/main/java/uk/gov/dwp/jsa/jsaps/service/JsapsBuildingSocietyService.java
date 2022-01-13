package uk.gov.dwp.jsa.jsaps.service;

import org.springframework.stereotype.Service;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class JsapsBuildingSocietyService {
    private static final String BUILDING_SOCIETY_DEFAULT_CODE = "AA";
    private static final Map<String, String> BUILDING_SOCIETY_CODES  = new HashMap<>();

    public Map<String, String> getBuildingSocietyCodes() {
        return BUILDING_SOCIETY_CODES;
    }

    public String getBuildingSocietyCode(final JsapsRequest key) {
        final String sortCode = key.getBankDetails().getSortCode().replaceAll("\\s+", "");
        final String accountNumber = key.getBankDetails().getAccountNumber();
        if (BUILDING_SOCIETY_CODES.get(sortCode) != null) {
            return BUILDING_SOCIETY_CODES.get(sortCode);
        } else if (BUILDING_SOCIETY_CODES.get(accountNumber) != null) {
            return BUILDING_SOCIETY_CODES.get(accountNumber);
        } else {
            return BUILDING_SOCIETY_DEFAULT_CODE;
        }
    }

    /**
     * 6 digit codes are building society sort codes.
     * "-" have been removed in sort code to compare against submitted details.
     * 8 digit codes are account numbers.
     */
    static {
        BUILDING_SOCIETY_CODES.put("151000", "FU");
        BUILDING_SOCIETY_CODES.put("200206", "BU");
        BUILDING_SOCIETY_CODES.put("300120", "HE");
        BUILDING_SOCIETY_CODES.put("204916", "MH");
        BUILDING_SOCIETY_CODES.put("070093", "NI");
        BUILDING_SOCIETY_CODES.put("083090", "SM");
        BUILDING_SOCIETY_CODES.put("083120", "NX");
        BUILDING_SOCIETY_CODES.put("083130", "NZ");
        BUILDING_SOCIETY_CODES.put("083135", "NY");
        BUILDING_SOCIETY_CODES.put("086115", "NR");
        BUILDING_SOCIETY_CODES.put("089072", "EC");
        BUILDING_SOCIETY_CODES.put("090000", "AN");
        BUILDING_SOCIETY_CODES.put("161622", "CD");
        BUILDING_SOCIETY_CODES.put("200000", "SY");
        BUILDING_SOCIETY_CODES.put("201755", "CA");
        BUILDING_SOCIETY_CODES.put("201823", "PI");
        BUILDING_SOCIETY_CODES.put("202733", "TC");
        BUILDING_SOCIETY_CODES.put("203253", "BD");
        BUILDING_SOCIETY_CODES.put("205570", "MN");
        BUILDING_SOCIETY_CODES.put("207405", "SX");
        BUILDING_SOCIETY_CODES.put("207891", "SK");
        BUILDING_SOCIETY_CODES.put("209778", "BQ");
        BUILDING_SOCIETY_CODES.put("209786", "BH");
        BUILDING_SOCIETY_CODES.put("234448", "IP");
        BUILDING_SOCIETY_CODES.put("235954", "NC");
        BUILDING_SOCIETY_CODES.put("237448", "PG");
        BUILDING_SOCIETY_CODES.put("300080", "LB");
        BUILDING_SOCIETY_CODES.put("301525", "FL");
        BUILDING_SOCIETY_CODES.put("400713", "CB");
        BUILDING_SOCIETY_CODES.put("402024", "NL");
        BUILDING_SOCIETY_CODES.put("402311", "HN");
        BUILDING_SOCIETY_CODES.put("402715", "LH");
        BUILDING_SOCIETY_CODES.put("403427", "MU");
        BUILDING_SOCIETY_CODES.put("404303", "SF");
        BUILDING_SOCIETY_CODES.put("404613", "WB");
        BUILDING_SOCIETY_CODES.put("571184", "BN");
        BUILDING_SOCIETY_CODES.put("571194", "LM");
        BUILDING_SOCIETY_CODES.put("571327", "BR");
        BUILDING_SOCIETY_CODES.put("600235", "DU");
        BUILDING_SOCIETY_CODES.put("601621", "PE");
        BUILDING_SOCIETY_CODES.put("601727", "HO");
        BUILDING_SOCIETY_CODES.put("602443", "TE");
        BUILDING_SOCIETY_CODES.put("608009", "NG");
        BUILDING_SOCIETY_CODES.put("609474", "SH");
        BUILDING_SOCIETY_CODES.put("621719", "BP");
        BUILDING_SOCIETY_CODES.put("622453", "BZ");
        BUILDING_SOCIETY_CODES.put("622497", "KR");
        BUILDING_SOCIETY_CODES.put("622871", "ES");
        BUILDING_SOCIETY_CODES.put("622874", "NB");
        BUILDING_SOCIETY_CODES.put("622890", "BV");
        BUILDING_SOCIETY_CODES.put("623023", "BW");
        BUILDING_SOCIETY_CODES.put("623045", "BA");
        BUILDING_SOCIETY_CODES.put("830608", "SD");
        BUILDING_SOCIETY_CODES.put("831824", "CY");
        BUILDING_SOCIETY_CODES.put("839207", "DN");
        BUILDING_SOCIETY_CODES.put("10920320", "VE");
        BUILDING_SOCIETY_CODES.put("14575881", "DA");
        BUILDING_SOCIETY_CODES.put("60310646", "MA");
        BUILDING_SOCIETY_CODES.put("74574915", "MM");
        BUILDING_SOCIETY_CODES.put("74575938", "HK");
        BUILDING_SOCIETY_CODES.put("74578031", "LU");
    }
}
