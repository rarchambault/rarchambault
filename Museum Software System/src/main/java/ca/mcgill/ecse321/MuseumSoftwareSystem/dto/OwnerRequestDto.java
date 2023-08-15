package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OwnerRequestDto {

	@NotNull
	@NotBlank
	private String emailAddress;

	@NotNull
	@NotBlank
	private String password;

	@NotNull
	@NotBlank
	private String name;

	public OwnerRequestDto (String name, String emailAddress, String password) {
		this.name = name;
		this.password = password;
		this.emailAddress = emailAddress;
	}

	public OwnerRequestDto () { }

	public boolean setEmailAddress(String aEmailAddress) {
		boolean wasSet = false;
		emailAddress = aEmailAddress;
		wasSet = true;
		return wasSet;
	}

	public boolean setPassword(String aPassword) {
		boolean wasSet = false;
		password = aPassword;
		wasSet = true;
		return wasSet;
	}

	public boolean setName(String aName) {
		boolean wasSet = false;
		name = aName;
		wasSet = true;
		return wasSet;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
}
