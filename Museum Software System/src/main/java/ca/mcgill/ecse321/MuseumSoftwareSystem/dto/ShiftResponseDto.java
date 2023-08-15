package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import java.time.LocalTime;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;

public class ShiftResponseDto {
	public LocalTime startTime;
	public LocalTime endTime;
	public int id;
	public EmployeeResponseDto employee;
	public DayResponseDto day;
	public MuseumResponseDto museum;

	public ShiftResponseDto(Shift shift) {
		this.startTime = shift.getStartTime();
		this.endTime = shift.getEndTime();
		this.id = shift.getId();
		this.employee = new EmployeeResponseDto(shift.getEmployee());
		this.day = new DayResponseDto(shift.getDay());
		this.museum = new MuseumResponseDto(shift.getMuseum());
	}

	public ShiftResponseDto() {
	}

	public LocalTime getStartTime() {
		return this.startTime;
	}

	public LocalTime getEndTime() {
		return this.endTime;
	}

	public int getId() {
		return this.id;
	}

	public MuseumResponseDto getMuseum() {
		return this.museum;
	}

	public DayResponseDto getDay() {
		return this.day;
	}

	public EmployeeResponseDto getEmployee() {
		return this.employee;
	}

}
