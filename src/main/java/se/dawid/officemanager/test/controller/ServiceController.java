package se.dawid.officemanager.test.controller;


import se.dawid.officemanager.test.object.Account;
import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.object.Occupation;
import se.dawid.officemanager.test.repository.AccountRepository;
import se.dawid.officemanager.test.repository.EmployeeRepository;
import se.dawid.officemanager.test.repository.OccupationRepository;
import se.dawid.officemanager.test.service.AccountService;
import se.dawid.officemanager.test.service.EmployeeService;
import se.dawid.officemanager.test.service.OccupationService;

public class ServiceController {

    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final OccupationService occupationService;

    public ServiceController() {
        this.accountService = new AccountService(new AccountRepository());
        this.employeeService = new EmployeeService(new EmployeeRepository());
        this.occupationService = new OccupationService(new OccupationRepository());
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public OccupationService getOccupationService() {
        return occupationService;
    }

    public void createAccountIdentity(String username, String password, String firstName, String lastName, boolean adminPrivilege, String role) {
        try {
            Occupation occupation = occupationService.getOccupationByTitle(role);
            if (occupation == null) {
                throw new IllegalArgumentException("Invalid role selected.");
            }

            Employee employee = new Employee(firstName, lastName, occupation);
            boolean employeeCreated = employeeService.create(employee);
            if (!employeeCreated) {
                throw new RuntimeException("Failed to create employee.");
            }

            employee = employeeService.findByName(firstName);

            Account account = new Account(username, password, adminPrivilege, employee);
            boolean accountCreated = accountService.create(account);
            if (!accountCreated) {
                throw new RuntimeException("Failed to create account.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
