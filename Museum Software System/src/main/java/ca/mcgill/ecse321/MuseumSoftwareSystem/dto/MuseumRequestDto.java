package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MuseumRequestDto {

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	@NotBlank
	private String ownerEmailAddress;

	public MuseumRequestDto (String name, String ownerEmailAddress) {
		this.name = name;
		this.ownerEmailAddress = ownerEmailAddress;
	}

	public MuseumRequestDto () { }

	public String getName() {
		return name;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public boolean setName(String aName) {
		boolean wasSet = false;
		if (aName == null) {
			return wasSet;
		}

		name = aName;
		wasSet = true;
		return wasSet;
	}

	public boolean setOwnerEmailAddress(String aOwnerEmailAddress) {
		boolean wasSet = false;
		if (aOwnerEmailAddress == null) {
			return wasSet;
		}

		ownerEmailAddress = aOwnerEmailAddress;
		wasSet = true;
		return wasSet;
	}
}
