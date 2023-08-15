package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

public class OwnerResponseDto {

	private String emailAddress;
	private String name;
	private String password;

	public OwnerResponseDto(Owner owner) {
		this.emailAddress = owner.getEmailAddress();
		this.name = owner.getName();
		this.password = owner.getPassword();
	}

	public OwnerResponseDto() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getName() {
		return name;
	}

	public String getPassword() { return password; }
}
