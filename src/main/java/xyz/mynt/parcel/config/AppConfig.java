package xyz.mynt.parcel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import xyz.mynt.parcel.client.VoucherClient;

import javax.sql.DataSource;

/**
 * @author Julian Jupiter
 */
@Configuration
@EnableRetry
public class AppConfig {
    @Bean
    JdbcClient jdbcClient(DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }

    @Bean
    RestClient restClient(@Value("${mynt-custom-api.base-url}") String voucherUrl) {
        return RestClient.create(voucherUrl);
    }

    @Bean
    VoucherClient voucherClient(RestClient restClient) {
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return httpServiceProxyFactory.createClient(VoucherClient.class);
    }
}
