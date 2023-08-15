package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	EmployeeRepository employeeRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	EmployeeService employeeService;

	@Mock
	MuseumService museumService;

	/**
	 * Test that creates an employee, mock saves it, then compares the saved employee with original employee
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testCreateEmployee() {
		final Owner owner = new Owner("test@employee.com", "password", "Test Owner");
		final Museum museum = new Museum(owner, "Test Museum");
		final Employee employee = new Employee("test@employee.com","abc@123456" , "Test Visitor", museum);
		museum.setId(1);

		when(museumService.getMuseumById(employee.getMuseum().getId())).thenAnswer((InvocationOnMock invocation) -> museum);
		when(employeeRepo.save(any(Employee.class))).thenAnswer((InvocationOnMock invocation) -> employee);

		EmployeeRequestDto request = new EmployeeRequestDto(employee.getName(), employee.getEmailAddress(), employee.getPassword(), employee.getMuseum().getId());
		EmployeeResponseDto returnedEmployee = employeeService.createEmployee(request);

		assertNotNull(returnedEmployee);
		assertEquals(employee.getName(), returnedEmployee.getName());
		assertEquals(employee.getEmailAddress(), returnedEmployee.getEmailAddress());
		assertEquals(employee.getPassword(), returnedEmployee.getPassword());
		assertEquals(employee.getMuseum().getId(), returnedEmployee.getMuseum().getId());

		verify(employeeRepo, times(1)).save(any(Employee.class));
	}

	/**
	 * Creates an employee, then fetches for it by emailAddress, then compares it with original employee
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetEmployeeByEmailAddress() {

		final Employee employee = new Employee("test@visitor.com", "abc@123456", "Test Visitor", new Museum());

		when(employeeRepo.findEmployeeByEmailAddress(employee.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> employee);

		Employee returnedEmployee = employeeService.getEmployeeByEmailAddress(employee.getEmailAddress());

		assertNotNull(returnedEmployee);
		assertEquals(employee.getName(), returnedEmployee.getName());
		assertEquals(employee.getEmailAddress(), returnedEmployee.getEmailAddress());
		assertEquals(employee.getPassword(), returnedEmployee.getPassword());
	}

	/**
	 * Test that fetches for a nonexistent employee, and validates exception behaviour
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetEmployeeByInvalidEmailAddress() {

		final String employeeEmail = "test@fake.com";

		when(employeeRepo.findEmployeeByEmailAddress(employeeEmail))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> employeeService.getEmployeeByEmailAddress(employeeEmail));

		assertEquals(HttpStatus.OK, ex.getStatus());
	}

}
