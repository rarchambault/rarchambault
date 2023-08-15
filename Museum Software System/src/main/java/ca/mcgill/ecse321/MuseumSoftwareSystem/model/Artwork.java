package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Artwork {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Artwork Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Column(nullable = true)
	private double loanFee;
	private boolean isAvailableForLoan;
	private boolean isInMuseum;

	// Artwork Associations
	@ManyToOne(optional = false)
	private Room room;
	@ManyToOne(optional = true)
	private Visitor visitorOnWaitlist;
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Artwork() {
	}

	public Artwork(String aName, double aLoanFee, boolean aIsInMuseum, boolean aIsAvailableForLoan, Room aRoom,
			Visitor aVisitorOnWaitlist, Museum aMuseum) {
		this.name = aName;
		this.loanFee = aLoanFee;
		this.isInMuseum = aIsInMuseum;
		this.isAvailableForLoan = aIsAvailableForLoan;
		this.room = aRoom;
		this.visitorOnWaitlist = aVisitorOnWaitlist;
		this.museum = aMuseum;
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

	public boolean setLoanFee(double aLoanFee) {
		boolean wasSet = false;
		loanFee = aLoanFee;
		wasSet = true;
		return wasSet;
	}

	public boolean setIsAvailableForLoan(boolean aIsAvailableForLoan) {
		boolean wasSet = false;
		isAvailableForLoan = aIsAvailableForLoan;
		wasSet = true;
		return wasSet;
	}

	public boolean setIsInMuseum(boolean aIsInMuseum) {
		boolean wasSet = false;
		isInMuseum = aIsInMuseum;
		wasSet = true;
		return wasSet;
	}

	public boolean setId(int aId) {
		id = aId;
		return true;
	}

	public String getName() {
		return name;
	}

	public double getLoanFee() {
		return loanFee;
	}

	public boolean getIsAvailableForLoan() {
		return isAvailableForLoan;
	}

	public int getId() {
		return id;
	}

	public boolean isIsAvailableForLoan() {
		return isAvailableForLoan;
	}

	public boolean getIsInMuseum() {
		return isInMuseum;
	}

	public Room getRoom() {
		return room;
	}

	public boolean hasRoom() {
		boolean has = room != null;
		return has;
	}

	public Visitor getVisitorOnWaitlist() {
		return visitorOnWaitlist;
	}

	public Museum getMuseum() {
		return museum;
	}

	public boolean setRoom(Room aRoom) {
		boolean wasSet = false;
		if (aRoom == null) {
			return wasSet;
		}

		room = aRoom;
		wasSet = true;
		return wasSet;
	}

	public boolean setVisitorOnWaitlist(Visitor aVisitor) {
		boolean wasSet = false;
		if (aVisitor == null) {
			return wasSet;
		}

		visitorOnWaitlist = aVisitor;
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
}
