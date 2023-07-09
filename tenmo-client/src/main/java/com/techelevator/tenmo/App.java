package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            String token = currentUser.getToken();
            accountService.setAuthToken(token);
            transferService.setAuthToken(token);
            userService.setAuthToken(token);


        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        double balance = accountService.getBalance();
        System.out.println("Your current account balance is: " + balance);
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        transferService.displayTransfers();
        int transferId = consoleService.promptForInt("Enter Transfer ID to view details: (0 to cancel): ");
        if (transferId == 0) {
            System.out.println("Returning to main menu...");
            mainMenu();

        } else {
            transferService.displayTransferDetails(transferId);
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stu
        transferService.displayRequests();
        int request = consoleService.promptForInt("Please select a transfer ID to respond to: ");
        String response = consoleService.promptForString("Please enter Y to confirm or N to deny request : ");
        int statusId = 0;
        if (Objects.equals(response, "Y")) {
            statusId = 2;
        } else if (Objects.equals(response, "N")) {
            statusId = 3;
        } else {
            System.out.println("Invalid selection");
        }
        transferService.updateTransfer(request, statusId);
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        userService.displayUsers();
        int receiver = consoleService.promptForInt("Please select a user ID to send bucks to (0 to cancel): ");
        if (receiver == 0) {
            mainMenu();
        } else {
            BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of money to send in decimal format: ");
            double amount2 = accountService.getBalance();
            int comparison = amount.compareTo(BigDecimal.valueOf(amount2));
            if (comparison > 0 || comparison == 0) {
                System.out.println("Not enough funds");
                mainMenu();
            } else {

                transferService.makeTransfer(receiver, amount);
            }
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        userService.displayUsers();
        int receiver = consoleService.promptForInt("Please select a user ID to ask for money: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of money you are requesting in decimal format: ");
        transferService.requestTransfer(receiver, amount);
    }

}

