package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

public class DonationRequestDto {

    @Positive
    private int museumId;

    @NotNull
    @NotBlank
    private String donatorEmailAddress;

    @NotNull
    @NotBlank
    private String name;

    public DonationRequestDto(int museumId, String donatorEmailAddress, String name){
        this.museumId = museumId;
        this.donatorEmailAddress = donatorEmailAddress;
        this.name = name;
    }

    public DonationRequestDto() { }

    public int getMuseumId(){
        return museumId;
    }

    public String getDonatorEmailAddress(){
        return donatorEmailAddress;
    }

    public String getName(){
        return name;
    }

    public boolean setDonatorEmailAddress(String aEmailAddress) {
        boolean wasSet = false;
        if (aEmailAddress == null) {
            return wasSet;
        }

        donatorEmailAddress = aEmailAddress;
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
