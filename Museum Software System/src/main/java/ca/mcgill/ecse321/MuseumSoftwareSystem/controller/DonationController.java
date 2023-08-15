package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.IdDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/donation")
public class DonationController {
	@Autowired
	DonationService donationService;

	/**
	 *
	 * Returns the Donation object associated with the specified id from the DB
	 *
	 * @author Haroun Guessous
	 *
	 * @param request
	 * @return DonationDto with the Room's parameters
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<DonationResponseDto> createDonation(@Valid @RequestBody DonationRequestDto request) {
		DonationResponseDto donation = donationService.createDonation(request);
		return new ResponseEntity<DonationResponseDto>(donation, HttpStatus.CREATED);
	}

	/**
	 *
	 * Returns the Donation object associated with the specified id from the DB
	 *
	 * @author Haroun Guessous
	 *
	 * @param id
	 * @return DonationDto with the Room's parameters
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity<DonationResponseDto> getDonationById(@PathVariable int id) {
		Donation donation = donationService.getDonationById(id);
		return new ResponseEntity<DonationResponseDto>(new DonationResponseDto(donation), HttpStatus.OK);
	}

	/**
	 * Gets a donation object given its name
	 * @param name Name of the donation we want to find
	 * @return Fetched donation
	 *
	 * @author Haroun Guessous
	 */
	@GetMapping("/name/{name}")
	public ResponseEntity<DonationResponseDto> getDonationByName(@PathVariable String name) {
		Donation donation = donationService.getDonationByName(name);
		return new ResponseEntity<DonationResponseDto>(new DonationResponseDto(donation), HttpStatus.OK);
	}

	/**
	 * Gets all donations with a given status from the database
	 * @param status Status with which we want to search for the donations
	 * @return List of fetched donations
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping("/status/{status}")
	public ResponseEntity<Iterable<DonationResponseDto>> getDonationsByStatus(@PathVariable ApprovalStatus status) {
		Iterable<Donation> donations = donationService.getDonationsByStatus(status);

		ArrayList<DonationResponseDto> donationResponses = new ArrayList<DonationResponseDto>();

		for (var donation : donations) {
			donationResponses.add(new DonationResponseDto(donation));
		}

		return new ResponseEntity<Iterable<DonationResponseDto>>(donationResponses, HttpStatus.OK);
	}

	/**
	 * Approves a donation
	 * @return Approved Donation
	 *
	 * @author Murad G.
	 */
	@PostMapping("/approve")
	public ResponseEntity<DonationResponseDto> approveDonation(@RequestBody IdDto donationId) {
		Donation donation = donationService.approveDonation(donationId);
		return new ResponseEntity<DonationResponseDto>(new DonationResponseDto(donation), HttpStatus.OK);
	}

	/**
	 * Rejects a donation
	 * @param donationId ID of the donation we want to reject
	 *
	 * @author Murad G.
	 */
	@PostMapping("/reject")
	public ResponseEntity<DonationResponseDto> rejectDonation(@RequestBody IdDto donationId) {
		Donation donation = donationService.rejectDonation(donationId);
		return new ResponseEntity<DonationResponseDto>(new DonationResponseDto(donation), HttpStatus.OK);
	}

    /**
     * Gets all donations associated with the given donator email address
     * @param donatorEmailAddress Email address of the donator
     * @return List of donations associated with the given donator
	 *
	 * @author Roxanne Archambault
     */
    @GetMapping("/donatorEmailAddress/{donatorEmailAddress}")
    public ResponseEntity<Iterable<DonationResponseDto>> getDonationsByDonatorEmailAddress(@PathVariable String donatorEmailAddress) {
        Iterable<Donation> donations = donationService.getDonationsByDonatorEmailAddress(donatorEmailAddress);

        ArrayList<DonationResponseDto> donationResponses = new ArrayList<DonationResponseDto>();

        for (var donation : donations) {
            donationResponses.add(new DonationResponseDto(donation));
        }

        return new ResponseEntity<Iterable<DonationResponseDto>>(donationResponses, HttpStatus.OK);
    }
}
