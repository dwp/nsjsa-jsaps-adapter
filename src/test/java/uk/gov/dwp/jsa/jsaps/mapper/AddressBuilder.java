package uk.gov.dwp.jsa.jsaps.mapper;

import uk.gov.dwp.jsa.adaptors.dto.claim.Address;

public class AddressBuilder {
    private String firstLine;
    private String secondLine;
    private String postCode;
    private String town;
    private String country;

    public Address build() {
        return new Address(firstLine, secondLine, postCode, town, country);
    }

    public AddressBuilder withCountry(final String country) {
        this.country = country;
        return this;
    }

    public AddressBuilder withFirstLine(final String firstLine) {
        this.firstLine = firstLine;
        return this;
    }

    public AddressBuilder withPostCode(final String postCode) {
        this.postCode = postCode;
        return this;
    }

    public AddressBuilder withSecondLine(final String secondLine) {
        this.secondLine = secondLine;
        return this;
    }

    public AddressBuilder withTown(final String town) {
        this.town = town;
        return this;
    }
}



