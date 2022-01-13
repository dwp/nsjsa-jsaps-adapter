package uk.gov.dwp.jsa.jsaps.mapper;

import uk.gov.dwp.jsa.adaptors.dto.claim.Address;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.ContactDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Name;

import java.time.LocalDate;
import java.util.UUID;

public class ClaimantBuilder {
    private UUID claimantId;

    private String nino;

    private Name name;

    private LocalDate dateOfBirth;

    private Address address;

    private Address postalAddress;

    private ContactDetails contactDetails;

    public ClaimantBuilder withClaimantId(final UUID claimantId) {
        this.claimantId = claimantId;
        return this;
    }

    public ClaimantBuilder withNino(final String nino) {
        this.nino = nino;
        return this;
    }

    public ClaimantBuilder withName(final Name name) {
        this.name = name;
        return this;
    }

    public ClaimantBuilder withDOB(final LocalDate dob) {
        this.dateOfBirth = dob;
        return this;
    }

    public ClaimantBuilder withContactDetails(final ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
        return this;
    }

    public ClaimantBuilder withAddress(final Address address) {
        this.address = address;
        return this;
    }

    public ClaimantBuilder withPostalAddress(final Address postalAddress) {
        this.postalAddress = postalAddress;
        return this;
    }

    public Claimant build() {
        Claimant claimant = new Claimant();
        claimant.setAddress(address);
        claimant.setClaimantId(claimantId);
        claimant.setContactDetails(contactDetails);
        claimant.setDateOfBirth(dateOfBirth);
        claimant.setName(name);
        claimant.setNino(nino);
        claimant.setPostalAddress(postalAddress);
        return claimant;
    }
}
