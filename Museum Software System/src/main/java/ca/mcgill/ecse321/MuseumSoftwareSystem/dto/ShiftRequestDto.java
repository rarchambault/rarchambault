package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;
import java.sql.Date;

public class ShiftRequestDto {

    @Positive
    private int museumId;

    @NotNull
    @NotBlank
    private String employeeEmailAddress;

    @NotNull
    private Date dayDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    public ShiftRequestDto(LocalTime startTime, LocalTime endTime, String employeeEmailAddress, Date dayDate, int museumId){
        this.museumId = museumId;
        this.employeeEmailAddress = employeeEmailAddress;
        this.dayDate = dayDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
