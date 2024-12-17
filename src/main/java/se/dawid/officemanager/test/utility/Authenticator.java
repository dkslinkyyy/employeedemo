package se.dawid.officemanager.test.utility;

import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Account;

public class Authenticator {

    private final ServiceController serviceController;

    public Authenticator(ServiceController serviceController) {
        this.serviceController = serviceController;
    }


    public Account authenticate(String username, String password) {
        Account account = serviceController.getAccountService().getAccountByUsername(username);

        if (account == null) {
            System.out.println("Account not found for username: " + username);
            return null;
        }

        if (serviceController.getAccountService().verifyPassword(account, password)) {
            System.out.println("Authentication successful for username: " + username);
            return account;
        } else {
            System.out.println("Invalid password for username: " + username);
            return null;
        }
    }
}
