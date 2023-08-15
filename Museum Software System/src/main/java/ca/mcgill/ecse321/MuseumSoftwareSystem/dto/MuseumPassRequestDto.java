package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import java.sql.Date;

import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.PassType;

/**
 * Please direct all questions and queries regarding this code to the
 * rightful owner and maintainer of this code @author muradgohar
 */


// MuseumPassRequestDto Constructor
public class MuseumPassRequestDto {
	private String visitorEmailAddress;
	private PassType type;
	private int museumId;
	public Date date;

	public MuseumPassRequestDto() {
	}

	//setter methods
	public boolean setVisitorEmailAddress(String aVisitorEmail) {
		boolean wasSet = false;
		if (aVisitorEmail == null) {
			return wasSet;
		}

		visitorEmailAddress = aVisitorEmail;
		wasSet = true;
		return wasSet;
	}

	public boolean setMuseumId(int aMuseumId) {
		museumId = aMuseumId;
		return true;
	}

	public boolean setType(PassType yung) {
		boolean wasSet = false;
		if (yung == null) {
			return wasSet;
		}

		type = yung;
		wasSet = true;
		return wasSet;
	}

	//getter methods 
	public String getVisitorEmailAddress() {
		return visitorEmailAddress;
	}

	public PassType getType() {
		return type;
	}

	public int getMuseumId() {
		return museumId;
	}
	public Date getDate() {
		return date;
	}

	public boolean setDate(Date aDate) {
		date = aDate;
		return true;
	}
}
