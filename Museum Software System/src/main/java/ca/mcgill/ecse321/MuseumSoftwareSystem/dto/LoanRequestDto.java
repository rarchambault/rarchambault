package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LoanRequestDto {
	@NotNull
	@NotBlank
	private String loaneeEmailAddress;

	@Positive
	private int artworkId;

	@Positive
	private int museumId;

	public LoanRequestDto(String aLoaneeEmailAddress, int aArtworkId, int aMuseumId) {
		this.loaneeEmailAddress = aLoaneeEmailAddress;
		this.artworkId = aArtworkId;
		this.museumId = aMuseumId;
	}

	public LoanRequestDto() {
	}

	public String getLoaneeEmailAddress() {
		return loaneeEmailAddress;
	}

	public int getArtworkId() {
		return artworkId;
	}

	public int getMuseumId() {
		return museumId;
	}

	public boolean setLoanerEmailAddress(String aLoanerEmailAddress) {
		boolean wasSet = false;
		if (aLoanerEmailAddress == null) {
			return wasSet;
		}

		loaneeEmailAddress = aLoanerEmailAddress;
		wasSet = true;
		return wasSet;
	}

	public boolean setArtworkId(int aArtworkId) {
		artworkId = aArtworkId;
		return true;
	}

	public boolean setMuseumId(int aMuseumId) {
		museumId = aMuseumId;
		return true;
	}
}
