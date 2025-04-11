package it.gov.pagopa.template.config.rest;

import it.gov.pagopa.template.performancelogger.RestInvokePerformanceLogger;
import it.gov.pagopa.template.utils.HttpUtils;
import it.gov.pagopa.template.utils.SecurityUtils;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

  @Bean
  @ConfigurationProperties(prefix = "rest.defaults")
  public HttpClientConfig defaultHttpClientConfig(){
    return new HttpClientConfig();
  }

  @Bean
  public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer, HttpClientConfig defaultHttpClientConfig, SslBundles sslBundles) {
    return configurer.configure(new RestTemplateBuilder())
      .additionalInterceptors(new RestInvokePerformanceLogger())
      .additionalInterceptors(new QueryParamsPlusEncoderInterceptor())
      .requestFactoryBuilder(HttpUtils.buildPooledConnection(defaultHttpClientConfig, DefaultClientTlsStrategy.createSystemDefault()));
  }

    public static ResponseErrorHandler bodyPrinterWhenError(String applicationName) {
        final Logger errorBodyLogger = LoggerFactory.getLogger("REST_INVOKE." + applicationName);
        return new DefaultResponseErrorHandler() {
            @Override
            protected void handleError(@Nonnull ClientHttpResponse response, @Nonnull HttpStatusCode statusCode,
                                       @Nullable URI url, @Nullable HttpMethod method) throws IOException {
                try {
                    super.handleError(response, statusCode, url, method);
                } catch (HttpStatusCodeException ex) {
                    errorBodyLogger.info("{} {} Returned status {} and resulted on exception {} - {}: {}",
                      method,
                      SecurityUtils.removePiiFromURI(url),
                      ex.getStatusCode(),
                      ex.getClass().getSimpleName(),
                      ex.getMessage(),
                      ex.getResponseBodyAsString());
                    throw ex;
                }
            }
        };
    }
}
