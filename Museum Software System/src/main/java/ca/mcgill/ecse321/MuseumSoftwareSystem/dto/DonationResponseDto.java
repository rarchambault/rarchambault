package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DonationResponseDto {

    private int id;
    private MuseumResponseDto museum;
    private VisitorResponseDto donator;
    private String name;
    private String requestDate;
    private ApprovalStatus status;

    public DonationResponseDto(Donation donation) {
        this.id = donation.getId();
        this.museum = new MuseumResponseDto(donation.getMuseum());
        this.donator = new VisitorResponseDto(donation.getDonator());
        this.name = donation.getName();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
        this.requestDate = dt1.format(donation.getRequestDate());
        this.status = donation.getStatus();
    }

    public DonationResponseDto() { }

    public MuseumResponseDto getMuseum(){
        return museum;
    }

    public VisitorResponseDto getDonator(){
        return donator;
    }

    public String getName(){
        return name;
    }

    public String getRequestDate(){
        return requestDate;
    }

    public ApprovalStatus getStatus(){
        return status;
    }

    public int getId() {
        return this.id;
    }
}
