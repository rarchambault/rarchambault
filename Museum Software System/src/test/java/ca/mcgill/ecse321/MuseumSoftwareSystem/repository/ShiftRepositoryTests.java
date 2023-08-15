package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShiftRepositoryTests {
	@Autowired
	private ShiftRepository shiftRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private DayRepository dayRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * Method that clears database entries after each test
	 * 
	 * @author Mohammed Elsayed
	 */
	@AfterEach
	public void clearDatabase() {
		shiftRepository.deleteAll(); // delete shift entry
		dayRepository.deleteAll(); // delete day entry
		employeeRepository.deleteAll(); // delete employee entry
		museumRepository.deleteAll(); // delete museum entry
		ownerRepository.deleteAll(); // delete owner entry

	}

	/**
	 * Test method that creates a shift entry (and its associated entries) and then
	 * saves it in the database. It then fetches it from the database, and checks
	 * whether its data has been saved.
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testPersistAndLoadShift() {
		// create an owner object
		Owner owner = new Owner();
		String emailAddressOwner = "owner@gmail.com";
		String passwordOwner = "passowner1234";
		String nameOwner = "somerandomowner";
		owner.setEmailAddress(emailAddressOwner);
		owner.setPassword(passwordOwner);
		owner.setName(nameOwner);
		ownerRepository.save(owner); // save the owner entry before saving the shift

		// create a museum object
		Museum museum = new Museum();
		museum.setOwner(owner);
		museumRepository.save(museum); // save the museum entry before saving the shift

		// create an employee object
		Employee employee = new Employee();
		String emailAddressEmployee = "employee@gmail.com";
		String passwordEmployee = "passemployee1234";
		String nameEmployee = "somerandomemployee";
		employee.setEmailAddress(emailAddressEmployee);
		employee.setPassword(passwordEmployee);
		employee.setName(nameEmployee);
		employee.setMuseum(museum);
		employeeRepository.save(employee); // save the employee entry before saving the shift

		// create a day object
		Day day = new Day();
		int openingHour = 9;
		int closingHour = 15;
		Date date = Date.valueOf("2022-11-10");
		boolean isHoliday = false;
		day.setDate(date);
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);
		dayRepository.save(day); // save the day entry before saving the shift

		// create a shift object
		Shift shift = new Shift();
		LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		LocalTime endTime = LocalTime.of(15, 0, 0, 000000000);
		shift.setStartTime(startTime);
		shift.setEndTime(endTime);
		shift.setEmployee(employee);
		shift.setDay(day);
		shift.setMuseum(museum);

		// save the shift entry
		Shift newShift = shiftRepository.save(shift);
		int id = newShift.getId();

		// fetch the saved shift object from the database
		Shift fetchedShift = shiftRepository.findById(id).get();

		// assert that all saved entries have the correct attributes
		assertNotNull(fetchedShift);
		assertEquals(id, fetchedShift.getId());
		assertEquals(newShift.getStartTime(), fetchedShift.getStartTime());
		assertEquals(newShift.getEndTime(), fetchedShift.getEndTime());
		assertEquals(newShift.getMuseum().getId(), fetchedShift.getMuseum().getId());
		assertEquals(newShift.getMuseum().getOwner().getEmailAddress(),
				fetchedShift.getMuseum().getOwner().getEmailAddress());
		assertEquals(newShift.getMuseum().getOwner().getName(), fetchedShift.getMuseum().getOwner().getName());
		assertEquals(newShift.getMuseum().getOwner().getPassword(), fetchedShift.getMuseum().getOwner().getPassword());
		assertEquals(newShift.getDay().getOpeningHour(), fetchedShift.getDay().getOpeningHour());
		assertEquals(newShift.getDay().getClosingHour(), fetchedShift.getDay().getClosingHour());
		assertEquals(newShift.getDay().getDate(), fetchedShift.getDay().getDate());
		assertEquals(newShift.getDay().getIsHoliday(), fetchedShift.getDay().getIsHoliday());

	}
}
