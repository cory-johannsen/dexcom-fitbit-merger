package cjohannsen.dfm;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DexcomService {

    @Value("#{environment.DEXCOM_BASE_URL}")
    private String dexcomBaseUrl;

    @Value("#{environment.DEXCOM_OAUTH_CLIENT_ID}")
    private String dexcomOauthClientId;

    @Value("#{environment.DEXCOM_OAUTH_CLIENT_SECRET}")
    private String dexcomOauthClientSecret;

    @Value("${environment.DEXCOM_OAUTH_LOGIN_URI:#{'/v2/oauth2/login'}}")
    private String dexcomOauthLoginUri;


    @Value("${environment.DEXCOM_OAUTH_ACCESS_TOKEN_URI:#{'/v2/oauth2/token'}}")
    private String dexcomOauthAccessTokenUri;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    ObjectMapper objectMapper;

    public String getDataRange() {
        final var redirectUri = "http://fake.uri";
        final var uri = dexcomBaseUrl + dexcomOauthLoginUri + "?client_id=" + dexcomOauthClientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=offline_access";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("cache-control", "no-cache")
                .build();

        try {
            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            final var body = response.body();
            return body;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
