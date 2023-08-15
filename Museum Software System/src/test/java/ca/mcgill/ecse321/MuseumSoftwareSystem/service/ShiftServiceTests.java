package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ShiftRepository;

@ExtendWith(MockitoExtension.class)
public class ShiftServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	ShiftRepository shiftRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	ShiftService shiftService;

	@Mock
	EmployeeService employeeService;

	@Mock
	DayService dayService;

	@Mock
	MuseumService museumService;

	/**
	 * Test method that creates a shift
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testCreateShift() {
		// Create parameters
		final int id = 1;
		final LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		final LocalTime endTime = LocalTime.of(16, 0, 0, 000000000);
		final String employeeEmail = "employee@yahoo.com";
		final String employeePassword = "somepassword";
		final String employeeName = "Mr.Employee";
		final String ownerEmail = "owner@yahoo.com";
		final String ownerPassword = "somepassword";
		final String ownerName = "Mr.Owner";

		// Create necessary objects to be returned
		final Owner owner = new Owner(ownerEmail, ownerPassword, ownerName);

		Museum museum = new Museum(owner);
		museum.setId(id);

		final Employee employee = new Employee(employeeEmail, employeePassword, employeeName, museum);

		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final Day day = new Day(date, openingHour, closingHour, isHoliday, museum);

		Shift shift = new Shift(startTime, endTime, employee, day, museum);
		shift.setId(id);

		ShiftRequestDto request = new ShiftRequestDto(startTime, endTime, employeeEmail, date, id);

		when(dayService.getDayByDate(date)).thenAnswer((InvocationOnMock invocation) -> day);
		when(museumService.getMuseumById(id)).thenAnswer((InvocationOnMock invocation) -> museum);
		when(employeeService.getEmployeeByEmailAddress(employeeEmail))
				.thenAnswer((InvocationOnMock invocation) -> employee);
		when(shiftRepo.save(any(Shift.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		ShiftResponseDto returnedShift = shiftService.createShift(request);

		assertNotNull(returnedShift);
		assertEquals(startTime, returnedShift.getStartTime());
		assertEquals(endTime, returnedShift.getEndTime());
		assertEquals(employeeEmail, returnedShift.getEmployee().getEmailAddress());
		assertEquals(date, returnedShift.getDay().getDate());

		// Check that the service actually saved the shift
		verify(shiftRepo, times(1)).save(any(Shift.class));
	}

	/**
	 * Test method that attempts to retrieve a shift by its ID
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftById() {
		// Tell the mocked repository how to behave
		final int id = 29;
		final LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		final LocalTime endTime = LocalTime.of(16, 0, 0, 000000000);
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final String ownerEmail = "owner@yahoo.com";
		final String ownerPassword = "somepassword";
		final String ownerName = "Mr.Owner";
		final Owner owner = new Owner();
		owner.setEmailAddress(ownerEmail);
		owner.setPassword(ownerPassword);
		owner.setName(ownerName);
		final Museum museum = new Museum(owner);
		final Day day = new Day();
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setDate(date);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);
		final Employee employee = new Employee();
		final String employeeEmail = "employee@yahoo.com";
		final String employeePassword = "somepassword";
		final String employeeName = "Mr.Employee";
		employee.setEmailAddress(employeeEmail);
		employee.setPassword(employeePassword);
		employee.setName(employeeName);
		employee.setMuseum(museum);
		final Shift shift = new Shift();
		shift.setStartTime(startTime);
		shift.setEndTime(endTime);
		shift.setDay(day);
		shift.setMuseum(museum);
		shift.setEmployee(employee);

		when(shiftRepo.findShiftById(id)).thenAnswer((InvocationOnMock invocation) -> shift);

		// Test that the service behaves properly
		Shift fetchedShift = shiftService.getShiftById(id);

		assertNotNull(fetchedShift);
		assertEquals(startTime, fetchedShift.getStartTime());
		assertEquals(endTime, fetchedShift.getEndTime());
		assertEquals(employeeEmail, fetchedShift.getEmployee().getEmailAddress());
		assertEquals(date, fetchedShift.getDay().getDate());
	}

	/**
	 * Test method that attempts to retrieve a shift by the email address of its
	 * employee
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftsByEmployeeEmailAddress() {
		// Tell the mocked repository how to behave
		final LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		final LocalTime endTime = LocalTime.of(16, 0, 0, 000000000);
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final String ownerEmail = "owner@yahoo.com";
		final String ownerPassword = "somepassword";
		final String ownerName = "Mr.Owner";
		final Owner owner = new Owner();
		owner.setEmailAddress(ownerEmail);
		owner.setPassword(ownerPassword);
		owner.setName(ownerName);
		final Museum museum = new Museum(owner);
		final Day day = new Day();
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setDate(date);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);
		final Employee employee = new Employee();
		final String employeeEmail = "employee@yahoo.com";
		final String employeePassword = "somepassword";
		final String employeeName = "Mr.Employee";
		employee.setEmailAddress(employeeEmail);
		employee.setPassword(employeePassword);
		employee.setName(employeeName);
		employee.setMuseum(museum);
		final Shift shift = new Shift();
		shift.setStartTime(startTime);
		shift.setEndTime(endTime);
		shift.setDay(day);
		shift.setMuseum(museum);
		shift.setEmployee(employee);

		ArrayList<Shift> mockedResponse = new ArrayList<Shift>();
		mockedResponse.add(shift);

		when(shiftRepo.findShiftsByEmployeeEmailAddress(employeeEmail))
				.thenAnswer((InvocationOnMock invocation) -> mockedResponse);

		// Test that the service behaves properly
		Shift fetchedShift = shiftService.getShiftsByEmployeeEmailAddress(employeeEmail).get(0);

		assertNotNull(fetchedShift);
		assertEquals(startTime, fetchedShift.getStartTime());
		assertEquals(endTime, fetchedShift.getEndTime());
		assertEquals(employeeEmail, fetchedShift.getEmployee().getEmailAddress());
		assertEquals(date, fetchedShift.getDay().getDate());
	}

	/**
	 * Test method that attempts to retrieve a shift object by its date
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftByDayDate() {
		// Tell the mocked repository how to behave
		final LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		final LocalTime endTime = LocalTime.of(16, 0, 0, 000000000);
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final String ownerEmail = "owner@yahoo.com";
		final String ownerPassword = "somepassword";
		final String ownerName = "Mr.Owner";
		final Owner owner = new Owner();
		owner.setEmailAddress(ownerEmail);
		owner.setPassword(ownerPassword);
		owner.setName(ownerName);
		final Museum museum = new Museum(owner);
		final Day day = new Day();
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setDate(date);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);
		final Employee employee = new Employee();
		final String employeeEmail = "employee@yahoo.com";
		final String employeePassword = "somepassword";
		final String employeeName = "Mr.Employee";
		employee.setEmailAddress(employeeEmail);
		employee.setPassword(employeePassword);
		employee.setName(employeeName);
		employee.setMuseum(museum);
		final Shift shift = new Shift();
		shift.setStartTime(startTime);
		shift.setEndTime(endTime);
		shift.setDay(day);
		shift.setMuseum(museum);
		shift.setEmployee(employee);

		when(shiftRepo.findShiftByDayDate(date)).thenAnswer((InvocationOnMock invocation) -> shift);

		// Test that the service behaves properly
		Shift fetchedShift = shiftService.getShiftByDayDate(date);

		assertNotNull(fetchedShift);
		assertEquals(startTime, fetchedShift.getStartTime());
		assertEquals(endTime, fetchedShift.getEndTime());
		assertEquals(employeeEmail, fetchedShift.getEmployee().getEmailAddress());
		assertEquals(date, fetchedShift.getDay().getDate());

	}

	/**
	 * Test method that attempts to retrieve a shift with an invalid ID
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftByInvalidId() {
		final int invalidId = 35;
		when(shiftRepo.findShiftById(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> shiftService.getShiftById(invalidId));

		assertEquals("Shift not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a shift by an invalid email address
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftByInvalidEmployeeEmailAddress() {
		final String invalidEmployeeEmail = "SomeRandomLoser@yahoo.com";
		when(shiftRepo.findShiftsByEmployeeEmailAddress(invalidEmployeeEmail))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> shiftService.getShiftsByEmployeeEmailAddress(invalidEmployeeEmail));

		assertEquals("Shifts not found.", ex.getMessage());
		assertEquals(HttpStatus.OK, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a shift with an invalid date
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testFindShiftByInvalidDate() {

		final Date invalidDate = new Date(13 - 13 - 2022);
		when(shiftRepo.findShiftByDayDate(invalidDate)).thenAnswer((InvocationOnMock innvocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> shiftService.getShiftByDayDate(invalidDate));

		assertEquals("Shift not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that deletes a shift and checks if it was deleted
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void deleteShift() {
		// create a shift
		final LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		final LocalTime endTime = LocalTime.of(16, 0, 0, 000000000);
		final String employeeEmail = "employee@yahoo.com";
		final String employeePassword = "somepassword";
		final String employeeName = "Mr.Employee";
		final String ownerEmail = "owner@yahoo.com";
		final String ownerPassword = "somepassword";
		final String ownerName = "Mr.Owner";
		final Owner owner = new Owner();
		owner.setEmailAddress(ownerEmail);
		owner.setPassword(ownerPassword);
		owner.setName(ownerName);
		final Museum museum = new Museum(owner);
		final Employee employee = new Employee();
		employee.setEmailAddress(employeeEmail);
		employee.setPassword(employeePassword);
		employee.setName(employeeName);
		employee.setMuseum(museum);
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final Day day = new Day();
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setDate(date);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);
		final Shift shift = new Shift();
		shift.setStartTime(startTime);
		shift.setEndTime(endTime);
		shift.setEmployee(employee);
		shift.setDay(day);

		shiftRepo.delete(shift);

		when(shiftRepo.findShiftsByEmployeeEmailAddress(employeeEmail))
				.thenAnswer((InvocationOnMock invocation) -> null);
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> shiftService.getShiftsByEmployeeEmailAddress(employeeEmail));

		when(shiftRepo.findShiftByDayDate(date)).thenAnswer((InvocationOnMock invocation) -> null);
		MuseumSoftwareSystemException ex2 = assertThrows(MuseumSoftwareSystemException.class,
				() -> shiftService.getShiftByDayDate(date));

		assertEquals("Shifts not found.", ex.getMessage());
		assertEquals(HttpStatus.OK, ex.getStatus());
		assertEquals("Shift not found.", ex2.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex2.getStatus());
	}
}
