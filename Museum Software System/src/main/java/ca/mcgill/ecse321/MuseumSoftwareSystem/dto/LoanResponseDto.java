package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import java.sql.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;

public class LoanResponseDto {

	@Positive
	private int id;

	@NotNull
	private Date startDate;

	@NotNull
	private Date endDate;

	@PositiveOrZero
	private int numberOfRenewals;

	@NotNull
	private ApprovalStatus status;

	@NotNull
	private boolean isLate;

	private double lateFee;

	private boolean isFeePaid;

	@NotNull
	private VisitorResponseDto loanee;

	@NotNull
	private ArtworkResponseDto artwork;

	@NotNull
	private MuseumResponseDto museum;

	public LoanResponseDto(Loan loan) {
		this.id = loan.getId();
		this.startDate = loan.getStartDate();
		this.endDate = loan.getEndDate();
		this.numberOfRenewals = loan.getNumberOfRenewals();
		this.status = loan.getStatus();
		this.isLate = loan.getIsLate();
		this.lateFee = loan.getLateFee();
		this.isFeePaid = loan.getIsFeePaid();
		this.loanee = new VisitorResponseDto(loan.getLoanee());
		this.artwork = new ArtworkResponseDto(loan.getArtwork());
		this.museum = new MuseumResponseDto(loan.getMuseum());
	}

	public LoanResponseDto() {
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

	public VisitorResponseDto getLoanee() {
		return loanee;
	}

	public ArtworkResponseDto getArtwork() {
		return artwork;
	}

	public MuseumResponseDto getMuseum() {
		return museum;
	}
}
