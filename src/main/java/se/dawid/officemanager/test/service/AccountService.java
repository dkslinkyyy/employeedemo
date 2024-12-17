package se.dawid.officemanager.test.service;

import se.dawid.officemanager.test.object.Account;
import se.dawid.officemanager.test.repository.AccountRepository;

import java.util.List;

public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByIdentifier(username);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public boolean create(Account account) {
       return this.accountRepository.create(account);
    }

    public boolean delete(Account account) {
        return this.accountRepository.delete(account);
    }

    public boolean save(Account account) {
        return this.accountRepository.update(account);
    }

    public boolean verifyPassword(Account account, String password) {
        return account.getPassword().equals(password);
    }
}
