package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Employee extends Person {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Employee Associations
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Employee() {
		super();
	}

	public Employee(String aEmailAddress, String aPassword, String aName, Museum aMuseum) {
		super(aEmailAddress, aPassword, aName);
		this.museum = aMuseum;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

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