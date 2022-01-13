package uk.gov.dwp.jsa.jsaps.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.jsa.jsaps.service.RestTemplateResponseErrorHandler;
import uk.gov.dwp.jsa.jsaps.util.UpperCaseStringSerializer;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        configureObjectMapper(restTemplate);

        return restTemplate;
    }

    private void configureObjectMapper(final RestTemplate restTemplate) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new UpperCaseStringSerializer());

        MappingJackson2HttpMessageConverter messageConverter = restTemplate.getMessageConverters().stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .findFirst().orElseThrow(() -> new RuntimeException("MappingJackson2HttpMessageConverter not found"));
        messageConverter.getObjectMapper().registerModule(module);
        messageConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


}
