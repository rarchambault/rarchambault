package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// line 84 "model.ump"
// line 165 "model.ump"
@Entity
public class Day {
	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Day Attributes
	@Id
	private Date date;
	private int openingHour;
	private int closingHour;
	private boolean isHoliday;

	// Day Associations
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Day() {
	}

	public Day(Date aDate, int aOpeningHour, int aClosingHour, boolean aIsHoliday, Museum aMuseum) {
		this.date = aDate;
		this.openingHour = aOpeningHour;
		this.closingHour = aClosingHour;
		this.isHoliday = aIsHoliday;
		this.museum = aMuseum;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setOpeningHour(int aOpeningHour) {
		boolean wasSet = false;
		openingHour = aOpeningHour;
		wasSet = true;
		return wasSet;
	}

	public boolean setClosingHour(int aClosingHour) {
		boolean wasSet = false;
		closingHour = aClosingHour;
		wasSet = true;
		return wasSet;
	}

	public boolean setDate(Date aDate) {
		boolean wasSet = false;
		date = aDate;
		wasSet = true;
		return wasSet;
	}

	public boolean setIsHoliday(boolean aIsHoliday) {
		boolean wasSet = false;
		isHoliday = aIsHoliday;
		wasSet = true;
		return wasSet;
	}

	public int getOpeningHour() {
		return openingHour;
	}

	public int getClosingHour() {
		return closingHour;
	}

	public Date getDate() {
		return date;
	}

	public boolean getIsHoliday() {
		return isHoliday;
	}

	public boolean isIsHoliday() {
		return isHoliday;
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
}