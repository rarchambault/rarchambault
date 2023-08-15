package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Person Attributes
	@Id
	private String emailAddress;
	private String password;
	private String name;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Person() {
	}

	public Person(String aEmailAddress, String aPassword, String aName) {
		emailAddress = aEmailAddress;
		password = aPassword;
		name = aName;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setEmailAddress(String aEmailAddress) {
		boolean wasSet = false;
		emailAddress = aEmailAddress;
		wasSet = true;
		return wasSet;
	}

	public boolean setPassword(String aPassword) {
		boolean wasSet = false;
		password = aPassword;
		wasSet = true;
		return wasSet;
	}

	public boolean setName(String aName) {
		boolean wasSet = false;
		name = aName;
		wasSet = true;
		return wasSet;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public void delete() {
	}

	@Override
	public String toString() {
		return super.toString() + "[" + "emailAddress" + ":" + getEmailAddress() + "," + "password" + ":"
				+ getPassword() + "," + "name" + ":" + getName() + "]";
	}
}