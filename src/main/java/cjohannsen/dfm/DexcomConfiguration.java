package cjohannsen.dfm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DexcomConfiguration {
    public static final String GRANT_TYPE = "authorization_code";

    @Value("#{environment.DEXCOM_BASE_URL}")
    private String dexcomBaseUrl;

    @Value("#{environment.DEXCOM_OAUTH_CLIENT_ID}")
    private String dexcomOauthClientId;

    @Value("#{environment.DEXCOM_OAUTH_CLIENT_SECRET}")
    private String dexcomOauthClientSecret;

    @Value("${environment.DEXCOM_OAUTH_ACCESS_TOKEN_URI:#{'/v2/oauth2/token'}}")
    private String dexcomOauthAccessTokenUri;

    @Bean
    protected ClientCredentialsResourceDetails oAuthDetails() {
        final var details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(dexcomBaseUrl + dexcomOauthAccessTokenUri);
        details.setClientId(dexcomOauthClientId);
        details.setClientSecret(dexcomOauthClientSecret);
        details.setGrantType(GRANT_TYPE);
        return details;
    }

    @Bean
    protected RestTemplate restTemplate(ClientCredentialsResourceDetails details) {
        return new OAuth2RestTemplate(details);
    }

}
