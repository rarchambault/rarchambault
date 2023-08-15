package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class EmployeeRequestDto {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String emailAddress;

    @NotNull
    @NotBlank
    private String password;

    @Positive
    private int museumId;

    public EmployeeRequestDto(String name, String emailAddress, String password, int museumId){
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.museumId = museumId;
    }

    public EmployeeRequestDto() { }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getMuseumId() {
        return museumId;
    }

    public boolean setEmailAddress(String aEmailAddress) {
        boolean wasSet = false;
        if (aEmailAddress == null) {
            return wasSet;
        }

        emailAddress = aEmailAddress;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword) {
        boolean wasSet = false;
        if (aPassword == null) {
            return wasSet;
        }

        password = aPassword;
        wasSet = true;
        return wasSet;
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

    public boolean setMuseumId(int aMuseumId) {
        boolean wasSet = false;
        if (aMuseumId == 0) {
            return wasSet;
        }

        museumId = aMuseumId;
        wasSet = true;
        return wasSet;
    }
}
