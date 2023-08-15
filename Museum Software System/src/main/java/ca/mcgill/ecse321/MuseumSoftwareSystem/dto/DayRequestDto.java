package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import java.sql.Date;

public class DayRequestDto {

	public int museumId;
	public int openingHour;
	public int closingHour;
	public Date date;
	public boolean isHoliday;

	public DayRequestDto(int museumId, int openingHour, int closingHour, Date date, boolean isHoliday) {
		this.museumId = museumId;
		this.openingHour = openingHour;
		this.closingHour = closingHour;
		this.date = date;
		this.isHoliday = isHoliday;
	}

	public DayRequestDto() {
	}

	public int getMuseumId() {
		return museumId;
	}

	public boolean setMuseumId(int id) {
		boolean wasSet = false;
		museumId = id;
		wasSet = true;
		return wasSet;
	}

	public int getOpeningHour() {
		return openingHour;
	}

	public boolean setOpeningHour(int aOpeningHour) {
		boolean wasSet = false;
		openingHour = aOpeningHour;
		wasSet = true;
		return wasSet;
	}

	public int getClosingHour() {
		return closingHour;
	}

	public boolean setClosingHour(int aClosingHour) {
		boolean wasSet = false;
		closingHour = aClosingHour;
		wasSet = true;
		return wasSet;
	}

	public Date getDate() {
		return date;
	}

	public boolean setDate(Date aDate) {
		boolean wasSet = false;
		if (aDate == null) {
			return wasSet;
		}
		date = aDate;
		wasSet = true;
		return wasSet;
	}

	public boolean getIsHoliday() {
		return isHoliday;
	}

	public boolean setIsHoliday(boolean aIsHoliday) {
		boolean wasSet = false;
		isHoliday = aIsHoliday;
		wasSet = true;
		return wasSet;
	}

}
