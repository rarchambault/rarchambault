package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Shift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Shift Attributes
	private LocalTime startTime;
	private LocalTime endTime;

	// Shift Associations
	@ManyToOne(optional = false)
	private Employee employee;
	@ManyToOne(optional = false)
	private Day day;
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Shift() {
	}

	public Shift(LocalTime aStartTime, LocalTime aEndTime, Employee aEmployee, Day aDay, Museum aMuseum) {
		this.startTime = aStartTime;
		this.endTime = aEndTime;
		this.employee = aEmployee;
		this.day = aDay;
		this.museum = aMuseum;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setStartTime(LocalTime aStartTime) {
		boolean wasSet = false;
		startTime = aStartTime;
		wasSet = true;
		return wasSet;
	}

	public boolean setEndTime(LocalTime aEndTime) {
		boolean wasSet = false;
		endTime = aEndTime;
		wasSet = true;
		return wasSet;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public Employee getEmployee() {
		return employee;
	}

	public Day getDay() {
		return day;
	}

	public int getId() {
		return id;
	}

	public boolean setEmployee(Employee aEmployee) {
		boolean wasSet = false;
		if (aEmployee == null) {
			return wasSet;
		}
		employee = aEmployee;
		wasSet = true;
		return wasSet;
	}

	public boolean setDay(Day aDay) {
		boolean wasSet = false;
		if (aDay == null) {
			return wasSet;
		}
		day = aDay;
		wasSet = true;
		return wasSet;
	}

	public Museum getMuseum() {
		return museum;
	}

	public boolean setMuseum(Museum aMuseum) {
		boolean wasSet = false;
		if (aMuseum == null) {
			return wasSet;
		}

		museum = aMuseum;
		wasSet = true;
		return wasSet;
	}

	public boolean setId(int aId) {
		id = aId;
		return true;
	}
}
