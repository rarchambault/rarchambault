package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ShiftRepository;

@Service
public class ShiftService {

	@Autowired
	ShiftRepository shiftRepo;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	DayService dayService;

	@Autowired
	MuseumService museumService;

	/**
	 * Gets all shift objects from the database
	 * @return List of all shifts from the database
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Iterable<Shift> getAllShifts() {

		Iterable<Shift> shifts = shiftRepo.findAll();

		if (shifts == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Shifts not found.");
		}

		return shifts;
	}

	/**
	 * gets a shift object given its id
	 *
	 * @param id
	 * @return Fetched shift object
	 *
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public Shift getShiftById(int id) {
		Shift shift = shiftRepo.findShiftById(id);
		if (shift == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Shift not found.");
		}
		return shift;
	}

	/**
	 * Gets a list of shift objects given an employee's email address
	 *
	 * @param employeeEmailAddress Email
	 * @return Fetched shift object
	 *
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public List<Shift> getShiftsByEmployeeEmailAddress(String employeeEmailAddress) {
		List<Shift> shifts = shiftRepo.findShiftsByEmployeeEmailAddress(employeeEmailAddress);
		if (shifts == null || shifts.isEmpty()) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK, "Shifts not found.");
		}
		return shifts;
	}

	/**
	 * gets a shift object given its date
	 *
	 * @param date
	 * @return Fetched shift object
	 *
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public Shift getShiftByDayDate(Date date) {
		Shift shift = shiftRepo.findShiftByDayDate(date);
		if (shift == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Shift not found.");
		}
		return shift;
	}

	/**
	 * Creates a shift object
	 *
	 * @param shiftRequest
	 * @return saved shift object
	 *
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public ShiftResponseDto createShift(ShiftRequestDto shiftRequest) {
		Employee employee = employeeService.getEmployeeByEmailAddress(shiftRequest.getEmployeeEmailAddress());
		Day day = dayService.getDayByDate(shiftRequest.getDayDate());
		Museum museum = museumService.getMuseumById(shiftRequest.getMuseumId());

		LocalTime startTime = shiftRequest.getStartTime();
		LocalTime endTime = shiftRequest.getEndTime();

		Shift shift = new Shift(startTime, endTime, employee, day, museum);

		shift = shiftRepo.save(shift);
		return new ShiftResponseDto(shift);
	}

	/**
	 * deletes an existing shift object
	 *
	 * @param shiftId
	 * @return shiftRepo that has no shift
	 *
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public Shift deleteShift(int shiftId) {
		Shift shift = shiftRepo.findShiftById(shiftId);
		if (shift != null) {
			shiftRepo.delete(shift);
		}
		return shift;
	}
}
