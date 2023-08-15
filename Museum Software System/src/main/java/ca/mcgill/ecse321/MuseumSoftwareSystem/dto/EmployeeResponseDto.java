package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

public class EmployeeResponseDto {

    private String name;
    private String emailAddress;
    private String password;
    private MuseumResponseDto museum;

    public EmployeeResponseDto(Employee employee) {
        this.name = employee.getName();
        this.emailAddress = employee.getEmailAddress();
        this.password = employee.getPassword();
        this.museum = new MuseumResponseDto(employee.getMuseum());
    }

    public EmployeeResponseDto() { }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getName() {
        return name;
    }

    public String getPassword() { return password; }

    public MuseumResponseDto getMuseum() {
        return museum;
    }
}
