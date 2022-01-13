package uk.gov.dwp.jsa.jsaps.acceptance.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

import static uk.gov.dwp.jsa.security.JWTFilter.AUTHORIZATION_HEADER;
import static uk.gov.dwp.jsa.security.JWTFilter.AUTH_PREFIX;

public final class SecurityTestHelper {

    private SecurityTestHelper() {

    }

    private static final String JWT = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZ" +
            "SI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJmdWxsTmFtZSI6IkpvaG4gRG9lIiwic3RhZmZOdW1iZXIiOiIxMjMzN" +
            "DU0NTc2NzUiLCJncm91cHMiOlsibnNqc2FfYWRtaW4iXX0.Wlg1a6zDr4-CPYKnEf9YhdxWLcKrvs_swL9_iJRlUuivoUEAV" +
            "MB9KuZLJKkSIt-_IRyzaUMOr5sq4WTjDaIWW3sCswTsTT2X_Gi2OQ0IppYm_K2unFcn0456rcyRn_8Wh0C67hq7I0ZUpxH62" +
            "e9mcrabv69c08JJfRG3gRNOtm-ZqXVZYTomN1oKZEBSIeNE22C3Uk_MFPr30x8uGhdzrymk-N93fQEu6qc-UNT2o0jzVlK_D" +
            "-JztwF4RM1uYmsCOpXk0U-9MY_yjNkychL63CHQxxJqwRQKI9Uc_MKV9XZqQfjRANhG8a5b_Hm5MbrLuZk6dlTvddPoVGEot" +
            "z0L50ESG-wgVI-rQEus4fp85J0QCojtPbaWDyTkWxZsjok99bKuso-9rdWezX5x4DldjF41lHOItSlui2OA22MxJYKDfhWSO" +
            "vhnKcybdbib-TFKyuNbQJb-MtkwezmmDYKofQHiiHCGWPT9ZeniXHzYaWSzsT7rpjcePVLeLyn_p6Tw6-lsz1IU_WStUIzWa" +
            "5QMzaarmEhvmK1cKcijns83FmFWkEAX8RM-8N-tU5zjzPXtjIFj62i1myq3c5x86qifdLKSJMok9Fh8HSiJcs0Tuk8asiQWK" +
            "mepRz_16gejvQADkCZnIhN8pzb8-HI78Sl4ehLu_jEbOB7oaEkE4gA8rzc";

    public static HttpEntity createWithJsonHeaders() {
        HttpHeaders headers = createHeaders();
        return new HttpEntity<>("parameters", headers);
    }

    public static HttpEntity createWithJsonHeaders(final Object payload) {
        HttpHeaders headers = createHeaders();
        return new HttpEntity<>(payload, headers);
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER, AUTH_PREFIX + JWT);
        return headers;
    }
}
