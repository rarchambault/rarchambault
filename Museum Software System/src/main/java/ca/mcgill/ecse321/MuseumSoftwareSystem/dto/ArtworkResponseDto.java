package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;

public class ArtworkResponseDto {

	private int id;
	private String name;
	private double loanFee;
	private boolean isAvailableForLoan;
	private boolean isInMuseum;
	private RoomResponseDto room;
	private VisitorResponseDto visitorOnWaitlist;
	private MuseumResponseDto museum;

	public ArtworkResponseDto(Artwork artwork) {

		this.id = artwork.getId();
		this.name = artwork.getName();
		this.loanFee = artwork.getLoanFee();
		this.isAvailableForLoan = artwork.getIsAvailableForLoan();
		this.isInMuseum = artwork.getIsInMuseum();
		this.room = new RoomResponseDto(artwork.getRoom());

		if (artwork.getVisitorOnWaitlist() != null) {
			this.visitorOnWaitlist = new VisitorResponseDto(artwork.getVisitorOnWaitlist());
		}

		this.museum = new MuseumResponseDto(artwork.getMuseum());
	}

	public ArtworkResponseDto() {
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public double getLoanFee() {
		return this.loanFee;
	}

	public boolean getIsAvailableForLoan() {
		return this.isAvailableForLoan;
	}

	public boolean getIsInMuseum() {
		return this.isInMuseum;
	}

	public RoomResponseDto getRoom() {
		return room;
	}

	public VisitorResponseDto getVisitorOnWaitlist() {
		return visitorOnWaitlist;
	}

	public MuseumResponseDto getMuseum() {
		return museum;
	}

}
