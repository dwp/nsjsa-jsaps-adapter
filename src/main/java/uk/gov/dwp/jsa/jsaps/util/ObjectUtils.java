package uk.gov.dwp.jsa.jsaps.util;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public final class ObjectUtils {

    private ObjectUtils() {
    }

    @SuppressWarnings("squid:S1696") //Skip "NullPointerException" should not be caught
    public static <T> Optional<T> resolve(final Supplier<T> resolver) {
        try {
            return ofNullable(resolver.get());
        } catch (NullPointerException e) {
            return empty();
        }
    }
}
