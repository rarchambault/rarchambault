package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeRepositoryTests {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Method that clears database entries made by the test methods
	 * 
	 * @author Alec Tufenkjian
	 */
	@AfterEach
	public void clearDatabase() {
		employeeRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * Test method that creates an employee, persists it, loads it, and checks
	 * whether related data has been conserved.
	 * 
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testPersistAndLoadEmployee() {
		Owner owner = new Owner();
		owner.setEmailAddress("owner@test.com");
		owner.setName("Test Owner");
		owner.setPassword("abc@123456");
		ownerRepository.save(owner);

		Museum museum = new Museum();
		museum.setOwner(owner);
		museumRepository.save(museum);

		Employee testEmployee = new Employee("test@visitor.com", "abc@123456", "Visitor Test", museum);
		Employee savedEmployee = employeeRepository.save(testEmployee);
		Employee fetchedEmployee = employeeRepository.findEmployeeByEmailAddress(savedEmployee.getEmailAddress());

		assertEquals(savedEmployee.getEmailAddress(), fetchedEmployee.getEmailAddress());
		assertEquals(savedEmployee.getPassword(), fetchedEmployee.getPassword());
		assertEquals(savedEmployee.getName(), fetchedEmployee.getName());
		assertNotNull(fetchedEmployee.getMuseum());
	}
}
