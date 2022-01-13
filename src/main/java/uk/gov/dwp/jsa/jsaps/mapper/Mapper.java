package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class Mapper {


    static final String YES = "Y";
    static final String NO = "N";

    void setIfNotNull(final Consumer<String> setter, final String value) {
        if (StringUtils.isNotBlank(value)) {
            setter.accept(value);
        }
    }
}
