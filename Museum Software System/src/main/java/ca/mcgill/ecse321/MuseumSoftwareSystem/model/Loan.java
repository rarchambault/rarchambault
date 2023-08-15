package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Loan {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Loan Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date startDate;
	private Date endDate;
	private int numberOfRenewals;
	private ApprovalStatus status;
	private boolean isLate;
	private double lateFee;
	private boolean isFeePaid;

	// Loan Associations
	@ManyToOne(optional = false)
	private Visitor loanee;
	@OneToOne
	private Artwork artwork;
	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Loan() {
	}

	public Loan(Date aStartDate, Date aEndDate, int aNumberOfRenewals, ApprovalStatus aStatus, boolean aIsLate,
			double aLateFee, boolean aIsFeePaid, Visitor aVisitor, Artwork aArtwork, Museum aMuseum) {
		startDate = aStartDate;
		endDate = aEndDate;
		numberOfRenewals = aNumberOfRenewals;
		status = aStatus;
		isLate = aIsLate;
		lateFee = aLateFee;
		isFeePaid = aIsFeePaid;
		boolean didAddVisitor = setLoanee(aVisitor);
		if (!didAddVisitor) {
			throw new RuntimeException(
					"Unable to create loan due to visitor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
		boolean didAddArtwork = setArtwork(aArtwork);
		if (!didAddArtwork) {
			throw new RuntimeException(
					"Unable to create loan due to artwork. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
		boolean didAddMuseum = setMuseum(aMuseum);
		if (!didAddMuseum) {
			throw new RuntimeException(
					"Unable to create loan due to museum. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setStartDate(Date aStartDate) {
		boolean wasSet = false;
		startDate = aStartDate;
		wasSet = true;
		return wasSet;
	}

	public boolean setEndDate(Date aEndDate) {
		boolean wasSet = false;
		endDate = aEndDate;
		wasSet = true;
		return wasSet;
	}

	public boolean setNumberOfRenewals(int aNumberOfRenewals) {
		boolean wasSet = false;
		numberOfRenewals = aNumberOfRenewals;
		wasSet = true;
		return wasSet;
	}

	public boolean setStatus(ApprovalStatus aStatus) {
		boolean wasSet = false;
		status = aStatus;
		wasSet = true;
		return wasSet;
	}

	public boolean setIsLate(boolean aIsLate) {
		boolean wasSet = false;
		isLate = aIsLate;
		wasSet = true;
		return wasSet;
	}

	public boolean setLateFee(double aLateFee) {
		boolean wasSet = false;
		lateFee = aLateFee;
		wasSet = true;
		return wasSet;
	}

	public boolean setIsFeePaid(boolean aIsFeePaid) {
		boolean wasSet = false;
		isFeePaid = aIsFeePaid;
		wasSet = true;
		return wasSet;
	}

	public boolean setId(int aId) {
		id = aId;
		return true;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getNumberOfRenewals() {
		return numberOfRenewals;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public boolean getIsLate() {
		return isLate;
	}

	public double getLateFee() {
		return lateFee;
	}

	public boolean getIsFeePaid() {
		return isFeePaid;
	}

	public int getId() {
		return id;
	}

	public boolean isIsLate() {
		return isLate;
	}

	public boolean isIsFeePaid() {
		return isFeePaid;
	}

	public Visitor getLoanee() {
		return loanee;
	}

	public Artwork getArtwork() {
		return artwork;
	}

	public Museum getMuseum() {
		return museum;
	}

	public boolean setLoanee(Visitor aVisitor) {
		boolean wasSet = false;
		if (aVisitor == null) {
			return wasSet;
		}

		loanee = aVisitor;
		wasSet = true;
		return wasSet;
	}

	public boolean setArtwork(Artwork aNewArtwork) {
		boolean wasSet = false;
		if (aNewArtwork == null) {
			// Unable to setArtwork to null, as loan must always be associated to a artwork
			return wasSet;
		}

		artwork = aNewArtwork;
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

	@Override
	public String toString() {
		return super.toString() + "[" + "numberOfRenewals" + ":" + getNumberOfRenewals() + "," + "isLate" + ":"
				+ getIsLate() + "," + "lateFee" + ":" + getLateFee() + "," + "isFeePaid" + ":" + getIsFeePaid() + ","
				+ "id" + ":" + getId() + "]" + System.getProperties().getProperty("line.separator") + "  " + "startDate"
				+ "="
				+ (getStartDate() != null
						? !getStartDate().equals(this) ? getStartDate().toString().replaceAll("  ", "    ") : "this"
						: "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "endDate" + "="
				+ (getEndDate() != null
						? !getEndDate().equals(this) ? getEndDate().toString().replaceAll("  ", "    ") : "this"
						: "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "status" + "="
				+ (getStatus() != null
						? !getStatus().equals(this) ? getStatus().toString().replaceAll("  ", "    ") : "this"
						: "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "visitor = "
				+ (getLoanee() != null ? Integer.toHexString(System.identityHashCode(getLoanee())) : "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "artwork = "
				+ (getArtwork() != null ? Integer.toHexString(System.identityHashCode(getArtwork())) : "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "museum = "
				+ (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null");
	}
}