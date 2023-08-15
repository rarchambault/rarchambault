package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.EmployeeService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    /**
     * Gets all Employee objects from the database
     *
     * @return List of all employee objects from the database
     *
     * @author Roxanne Archambault
     */
    @GetMapping()
    public ResponseEntity<Iterable<EmployeeResponseDto>> getAllEmployees() {
        Iterable<Employee> employees = employeeService.getAllEmployees();

        ArrayList<EmployeeResponseDto> employeeResponses = new ArrayList<EmployeeResponseDto>();

        for (var employee : employees) {
            employeeResponses.add(new EmployeeResponseDto(employee));
        }

        return new ResponseEntity<Iterable<EmployeeResponseDto>>(employeeResponses, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto request) {
        EmployeeResponseDto employee = employeeService.createEmployee(request);
        return new ResponseEntity<EmployeeResponseDto>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/emailAddress/{emailAddress}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmailAddress(@PathVariable String emailAddress) {
        Employee employee = employeeService.getEmployeeByEmailAddress(emailAddress);
        return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employee), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("emailAddress/{emailAddress}")
    public ResponseEntity<EmployeeResponseDto> deleteEmployeeByEmailAddress(@PathVariable String emailAddress) {
        Employee employee = employeeService.deleteEmployeeByEmailAddress(emailAddress);
        return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employee), HttpStatus.OK);
    }

}
