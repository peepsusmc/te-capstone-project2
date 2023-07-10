package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import static org.junit.Assert.*;


public class TransferServiceTest {
    private static final Transfer[] expectedTransfers = {new Transfer(1,1,1,1,1, BigDecimal.valueOf(10.00)),
            new Transfer(2,2,2,2,2, BigDecimal.valueOf(20.00))};

    private static ProcessBuilder processBuilder;
    private static Process process;

    private static TransferService sut;

    @BeforeClass
    public static void setup() {

        processBuilder = new ProcessBuilder();
        // Windows: node server\\node_modules\\json-server\\lib\\cli\\bin.js server\\data-test.js --port=3001
        String command = "node";
        String cli = "server" + File.separator + "node_modules" + File.separator + "json-server" +
                File.separator + "lib" + File.separator + "cli" + File.separator + "bin.js";
        String data = "server" + File.separator + "data-test.js";
        String port = "--port=3001";
        processBuilder.command(command, cli, data, port);
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TransferService.baseUrl = "http://localhost:8080/";
        sut = new TransferService();

        if (!pingServer(TransferService.baseUrl + "transfer", 10, 500)) {
            throw new RuntimeException("Unable to connect to server: " + TransferService.baseUrl + System.lineSeparator() + "Make sure you've run `npm install` in the `server` folder and can run json-server.");
        }
    }

    @AfterClass
    public static void tearDown() {

        process.destroyForcibly();
    }

    @Test
    public void makeTransferTest() {
        String urlHold = TransferService.baseUrl;
        try {
            Transfer newTransfer = new Transfer(1, 1, 1, 1, 1, BigDecimal.valueOf(100.00));
            Transfer actualTransfer = sut.makeTransfer(1, BigDecimal.valueOf(100.00));
            assertNotNull("New transfer returned is null.", actualTransfer);
            assertTrue("New transfer id is still 0.", actualTransfer.getAccountTo() > 0);
            // Add() experiencing communication failure (ResourceAccessException) should return null
            TransferService.baseUrl = "http://localhost:3002/transfers/";   // Set failing API base url
            actualTransfer = sut.makeTransfer(0, null);
            assertNull("makeTransfer() did not return null after experiencing ResourceAccessException.",actualTransfer);
        } finally {
            TransferService.baseUrl = urlHold;  // Reestablish correct API base url
        }
    }


    private static boolean pingServer(String baseApiUrl, int maxTries, long waitInterval) {

        RestTemplate restTemplate = new RestTemplate();

        int tryCount = 0;
        while (tryCount < maxTries) {
            try {
                tryCount++;
                try {
                    restTemplate.headForHeaders(baseApiUrl);
                    return true;
                } catch (RestClientException e) {
                    // Just eat the exception and try the request again.
                }
                Thread.sleep(waitInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return  false;
    }
}
