package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Donation {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Donation Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private Date requestDate;
	private ApprovalStatus status;

	// Donation Associations
	@ManyToOne(optional = false)
	private Visitor donator;

	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Donation() {
	}

	public Donation(String name, Date requestDate, ApprovalStatus status, Museum museum, Visitor donator) {
		this.name = name;
		this.requestDate = requestDate;
		this.status = status;
		this.museum = museum;
		this.donator = donator;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setName(String aName) {
		boolean wasSet = false;
		name = aName;
		wasSet = true;
		return wasSet;
	}

	public boolean setRequestDate(Date aRequestDate) {
		boolean wasSet = false;
		requestDate = aRequestDate;
		wasSet = true;
		return wasSet;
	}

	public boolean setStatus(ApprovalStatus aStatus) {
		boolean wasSet = false;
		status = aStatus;
		wasSet = true;
		return wasSet;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public Visitor getDonator() {
		return donator;
	}

	public boolean setDonator(Visitor aDonator) {
		boolean wasSet = false;
		if (aDonator == null) {
			return wasSet;
		}

		donator = aDonator;
		wasSet = true;
		return wasSet;
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
