package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Visitor extends Person {

	@ManyToOne(optional = false)
	private Museum museum;

	public Visitor() { }

	public Visitor(String aEmailAddress, String aPassword, String aName, Museum aMuseum) {
		super(aEmailAddress, aPassword, aName);
		this.museum = aMuseum;
	}

	public Museum getMuseum() {
		return this.museum;
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