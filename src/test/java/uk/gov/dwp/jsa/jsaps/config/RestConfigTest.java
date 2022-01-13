package uk.gov.dwp.jsa.jsaps.config;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RestConfigTest {

    private RestConfig config;
    private RestTemplate restTemplate;

    @Test
    public void createsRestTemplate() {
        givenAConfig();
        whenICreateTheRestTemplate();
        thenTheRestTemplateIsCreated();
    }

    private void givenAConfig() {
        config = new RestConfig();
    }

    private void whenICreateTheRestTemplate() {
        restTemplate = config.restTemplate();
    }

    private void thenTheRestTemplateIsCreated() {
        assertThat(restTemplate, is(notNullValue()));
    }
}
