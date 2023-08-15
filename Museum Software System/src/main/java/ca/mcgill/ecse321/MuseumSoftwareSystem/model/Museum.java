package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Museum {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Museum Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;

	// Museum Associations
	@OneToOne(optional = false)
	private Owner owner;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Museum() {
	}

	public Museum(Owner owner) {
		this.owner = owner;
	}

	public Museum(String aEmailAddress, String aPassword, String aName) {
		this.owner = new Owner(aEmailAddress, aPassword, aName);
	}

	public Museum(Owner owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Owner getOwner() {
		return owner;
	}
	
	public boolean setId(int aId) {
		id = aId;
		return true;
	}

	public boolean setName(String aName) {
		boolean wasSet = false;
		if (aName == null) {
			return wasSet;
		}

		name = aName;
		wasSet = true;
		return wasSet;
	}

	public boolean setOwner(Owner aOwner) {
		boolean wasSet = false;
		if (aOwner == null) {
			return wasSet;
		}

		owner = aOwner;
		wasSet = true;
		return wasSet;
	}
}