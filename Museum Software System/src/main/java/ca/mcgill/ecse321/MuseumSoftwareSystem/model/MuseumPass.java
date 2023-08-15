package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Date;

@Entity
public class MuseumPass {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// MuseumPass Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private double price;
	private PassType type;
	private Date date;


	// MuseumPass Associations
	@ManyToOne(optional = false)
	private Visitor visitor;
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public MuseumPass() {
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setPrice(double aPrice) {
		boolean wasSet = false;
		price = aPrice;
		wasSet = true;
		return wasSet;
	}

	public boolean setType(PassType aType) {
		boolean wasSet = false;
		type = aType;
		wasSet = true;
		return wasSet;
	}
	
	public boolean setId(int aId) {
		id = aId;
		return true;
	}

	public double getPrice() {
		return price;
	}

	public PassType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public Visitor getVisitor() {
		return visitor;
	}

	public Museum getMuseum() {
		return museum;
	}

	public boolean setVisitor(Visitor aVisitor) {
		boolean wasSet = false;
		if (aVisitor == null) {
			return wasSet;
		}

		visitor = aVisitor;
		wasSet = true;
		return wasSet;
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
}