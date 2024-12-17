package se.dawid.officemanager.test.service;


import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.repository.EmployeeRepository;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean save(Employee employee) {
        return this.employeeRepository.update(employee);
    }


    public Employee findByName(String name) {
        return employeeRepository.findByIdentifier(name);
    }

    public boolean create(Employee employee) {
       return this.employeeRepository.create(employee);
    }

}
