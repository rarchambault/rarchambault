package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepo;

	@Autowired
	MuseumService museumService;

	/**
	 * Gets all employee records from the database
	 *
	 * @return List of all employee records from the database
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Iterable<Employee> getAllEmployees() {

		Iterable<Employee> employees = employeeRepo.findAll();

		if (employees == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Employees not found.");
		}

		return employees;
	}

	/**
	 * Method that retrieves employee by email address
	 *
	 * @param emailAddress
	 * @return employee
	 *
	 * @author Alec Tufenkjian
	 */
	@Transactional
	public Employee getEmployeeByEmailAddress(String emailAddress) {
		Employee employee = employeeRepo.findEmployeeByEmailAddress(emailAddress);

		if (employee == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK, "Employee not found.");
		}

		return employee;
	}

	/**
	 * Method that creates an employee from request
	 *
	 * @param employeeRequest
	 * @return Employee DTO Object
	 *
	 * @author Alec Tufenkjian
	 */
	@Transactional
	public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequest) {
		Museum museum = museumService.getMuseumById(employeeRequest.getMuseumId());

		Employee employee = new Employee(employeeRequest.getEmailAddress(), employeeRequest.getPassword(),
				employeeRequest.getName(), museum);

		employee = employeeRepo.save(employee);
		return new EmployeeResponseDto(employee);
	}

	@Transactional
	public Employee deleteEmployeeByEmailAddress(String emailAddress) {
		Employee employee = employeeRepo.findEmployeeByEmailAddress(emailAddress);
		employeeRepo.deleteById(emailAddress);

		return employee;
	}
}
