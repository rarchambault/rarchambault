package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;

@Entity
public class Owner extends Person {

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Owner() {
		super();
	}

	public Owner(String aEmailAddress, String aPassword, String aName) {
		super(aEmailAddress, aPassword, aName);
	}
}