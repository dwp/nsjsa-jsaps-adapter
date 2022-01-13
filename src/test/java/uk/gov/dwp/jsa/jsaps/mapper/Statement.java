package uk.gov.dwp.jsa.jsaps.mapper;

import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Statement {

    private LocalDate dateOfPrint;
    private final Claimant claimant;
    private final BankDetails bankDetails;
    private final Circumstances circumstances;

    public Statement(final LocalDate dateOfPrint,
                     final Claimant claimant,
                     final Circumstances circumstances,
                     final BankDetails bankDetails) {
        this.dateOfPrint = dateOfPrint;
        this.claimant = claimant;
        this.circumstances = circumstances;
        this.bankDetails = bankDetails;
    }

    public LocalDate getDateOfPrint() {
        return dateOfPrint;
    }

    public Claimant getClaimant() {
        return claimant;
    }

    public Circumstances getCircumstances() {
        return circumstances;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public String getLanguage() {
        return getLocale().getDisplayLanguage();
    }

    public Boolean hasOtherBenefits() {
        return !Objects.isNull(circumstances.getOtherBenefit());
    }

    public boolean hasHadJuryService() {
        return !Objects.isNull(circumstances.getJuryService());
    }

    public boolean hasEducation() {
        return !Objects.isNull(circumstances.getEducation());
    }

    public boolean hasEmailAddress() {
        return !Objects.isNull(claimant.getContactDetails())
                && !Objects.isNull(claimant.getContactDetails().getEmail());
    }

    public boolean hasAnotherPostalAddress() {
        return !Objects.isNull(claimant.getPostalAddress());
    }


    public String getFormattedNino() {
        String value = claimant.getNino();
        if (value == null) {
            return null;
        }
        final String nino = value.replaceAll("\\s", "");

        return IntStream.range(0, nino.length())
            .mapToObj((int i) -> {
                if ((i % 2) != 0) {
                    return String.format("%c ", nino.charAt(i));
                } else {
                    return String.format("%c", nino.charAt(i));
                }
            }).collect(Collectors.joining(""));
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(circumstances.getLocale());
    }
}


