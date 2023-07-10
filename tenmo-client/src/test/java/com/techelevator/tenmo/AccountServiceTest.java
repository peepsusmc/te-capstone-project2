package com.techelevator.tenmo;

import com.techelevator.tenmo.services.AccountService;
import io.cucumber.java.en_old.Ac;
import org.hamcrest.core.StringStartsWith;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class AccountServiceTest {
    private static final String API_URL = "http://localhost:8080/balance";


    @Test
    public void getBalanceTest() {
        AccountService sut = new AccountService();
        RestTemplate restTemplate = new RestTemplate();
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        server.expect(requestTo(API_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer")))
                .andRespond(withSuccess("100", MediaType.APPLICATION_JSON));

        double balance = sut.getBalance();

        Assert.assertEquals(100.00, balance, 0.01);

        server.verify();
    }
}
