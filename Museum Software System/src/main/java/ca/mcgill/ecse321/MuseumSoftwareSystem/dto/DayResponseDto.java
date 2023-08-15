package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import java.sql.Date;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;

public class DayResponseDto {
	public MuseumResponseDto museum;
	public int openingHour;
	public int closingHour;
	public Date date;
	public boolean isHoliday;
	
	public DayResponseDto(Day day) {
//		this.museum = day.getMuseum();
		this.museum = new MuseumResponseDto(day.getMuseum());
		this.openingHour = day.getOpeningHour();
		this.closingHour = day.getClosingHour();
		this.date = day.getDate();
		this.isHoliday =day.getIsHoliday();
	}
	
	public DayResponseDto() {
	}
	
	public MuseumResponseDto getMuseum() {
		return this.museum;
	}
//	public Museum getMuseum() {
//		return this.museum;
//	}
	
	public int getOpeningHour() {
		return this.openingHour;
	}
	
	public int getClosingHour() {
		return this.closingHour;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public boolean getIsHoliday() {
		return this.isHoliday;
	}
}
