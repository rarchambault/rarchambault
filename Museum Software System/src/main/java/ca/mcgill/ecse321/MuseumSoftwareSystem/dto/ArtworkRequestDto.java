package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ArtworkRequestDto {
	@Positive
	private int museumId;

	@NotNull
	@Positive
	private int roomId;

	@NotNull
	private String name;

	@Positive
	private double loanFee;

	private boolean isAvailableForLoan;

	private boolean isInMuseum;

	private String visitorOnWaitingListEmail;

	public ArtworkRequestDto(int aMuseumId, int aRoomId, String aName, double aLoanFee, boolean aIsInMuseum,
			boolean aIsAvailableForLoan, String aVisitorOnWaitlistEmail) {
		this.museumId = aMuseumId;
		this.roomId = aRoomId;
		this.name = aName;
		this.loanFee = aLoanFee;
		this.isAvailableForLoan = aIsAvailableForLoan;
		this.isInMuseum = aIsInMuseum;
		this.visitorOnWaitingListEmail = aVisitorOnWaitlistEmail;
	}

	public ArtworkRequestDto(Artwork artwork) {
		this.museumId = artwork.getMuseum().getId();
		this.roomId = artwork.getRoom().getId();
		this.name = artwork.getName();
		this.loanFee = artwork.getLoanFee();
		this.isAvailableForLoan = artwork.getIsAvailableForLoan();
		this.isInMuseum = artwork.getIsInMuseum();
		this.visitorOnWaitingListEmail = artwork.getVisitorOnWaitlist().getEmailAddress();
	}

	public ArtworkRequestDto() {
	}

	public void setMuseumId(int museumId) {
		this.museumId = museumId;
	}

	public int getMuseumId() {
		return this.museumId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLoanFee() {
		return this.loanFee;
	}

	public void setLoanFee(double loanFee) {
		this.loanFee = loanFee;
	}

	public boolean getIsAvailableForLoan() {
		return this.isAvailableForLoan;
	}

	public void setIsAvailableForLoan(boolean isAvailableForLoan) {
		this.isAvailableForLoan = isAvailableForLoan;
	}

	public boolean setIsInMuseum(boolean aIsInMuseum) {
		boolean wasSet = false;
		isInMuseum = aIsInMuseum;
		wasSet = true;
		return wasSet;
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getVisitorOnWaitingListEmail() {
		return this.visitorOnWaitingListEmail;
	}

	public boolean getIsInMuseum() {
		return isInMuseum;
	}

	public void setVisitorOnWaitingListEmail(String visitorOnWaitingListEmail) {
		this.visitorOnWaitingListEmail = visitorOnWaitingListEmail;
	}
}
