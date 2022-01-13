package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.jsaps.util.date.DateSeed;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PensionReviewDateFactoryTest {

    private static final PensionDetail PENSION_DETAIL = new PensionDetailBuilder().create();
    private static final LocalDate DATE_OF_CLAIM = LocalDate.of(2016, 10, 10);
    private static final LocalDate START_DATE_WITH_DIFFERENT_MONTH = LocalDate.of(2016, 11, 10);
    private static final LocalDate START_DATE_WITH_SAME_MONTH = LocalDate.of(2016, 10, 12);
    private static final LocalDate NOW = LocalDate.now();

    @Mock
    private DateSeed dateSeed;

    private PensionReviewDateFactory factory;
    private LocalDate reviewDate;

    @Before
    public void beforeEachTest() {
        initMocks(this);
    }

    @Test
    public void reviewDateIsNowPlus30DaysForCurrent() {
        givenAFactory();
        whenICallCreateWith(PENSION_DETAIL, DATE_OF_CLAIM, TypePension.CURRENT);
        thenTheReviewDateIs(NOW.plusDays(30));
    }

    @Test
    public void reviewDateIsNowPlus30DaysForDeferred() {
        givenAFactory();
        whenICallCreateWith(PENSION_DETAIL, DATE_OF_CLAIM, TypePension.DEFERRED);
        thenTheReviewDateIs(NOW.plusDays(30));
    }

    private void givenAFactory() {
        factory = new PensionReviewDateFactory(dateSeed);
        when(dateSeed.now()).thenReturn(NOW);
    }

    private void whenICallCreateWith(
            final PensionDetail pensionDetail,
            final LocalDate dateOfClaim,
            final TypePension typePension) {
        reviewDate = factory.create(pensionDetail, dateOfClaim, typePension);
    }

    private void thenTheReviewDateIs(final LocalDate expectedReviewDate) {
        assertThat(reviewDate, is(expectedReviewDate));
    }
    
    
}
