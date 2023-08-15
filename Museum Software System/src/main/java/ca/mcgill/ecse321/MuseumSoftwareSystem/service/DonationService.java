package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.IdDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DonationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonationService {

	@Autowired
	DonationRepository donationRepo;

	@Autowired
	MuseumService museumService;

	@Autowired
	VisitorService visitorService;

	/**
	 *
	 * Finds Donation object given its Id
	 *
	 * @author Haroun Guessous
	 *
	 * @param id
	 * @return Donation Object
	 *
	 * @author Haroun Guessous
	 */
	@Transactional
	public Donation getDonationById(int id) {
		Donation donation = donationRepo.findDonationById(id);

		if (donation == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Donation not found.");
		}

		return donation;
	}

	/**
	 * Gets a donation object by its name
	 * @param name Name of the donation
	 * @return Fetched donation
	 *
	 * @author Haroun Guessous
	 */
	@Transactional
	public Donation getDonationByName(String name) {
		Donation donation = donationRepo.findDonationByName(name);

		if (donation == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Donation not found.");
		}

		return donation;
	}

	/**
	 * Gets all donations with a given status from the database
	 * @param status Status of the donations we want to find
	 * @return List of fetched donations
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public List<Donation> getDonationsByStatus(ApprovalStatus status) {
		List<Donation> donations = donationRepo.findDonationsByStatus(status);

		if (donations == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Donations not found.");
		}

		return donations;
	}

	/**
	 *
	 * @author Haroun Guessous
	 *
	 *
	 * Creates a new Donation object in the database
	 *
	 * @param donationRequest
	 * @return saved Donation
	 */
	@Transactional
	public DonationResponseDto createDonation(DonationRequestDto donationRequest) {
		Museum museum = museumService.getMuseumById(donationRequest.getMuseumId());
		Visitor donator = visitorService.retrieveVisitorByEmail(donationRequest.getDonatorEmailAddress());

		Donation donation = new Donation();
		donation.setMuseum(museum);
		donation.setName(donationRequest.getName());
		donation.setDonator(donator);
		donation.setStatus(ApprovalStatus.InReview);
		donation.setRequestDate(Date.valueOf(LocalDate.now()));

		donation = donationRepo.save(donation);
		return new DonationResponseDto(donation);
	}

	/**
	 * Gets all donations associated to a specific email address
	 * @param donatorEmailAddress Email address of the donator
	 * @return List of fetched donations
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public List<Donation> getDonationsByDonatorEmailAddress(String donatorEmailAddress) {
		List<Donation> donations = donationRepo.findDonationsByDonatorEmailAddress(donatorEmailAddress);
		if (donations == null || donations.isEmpty()) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK, "Donations not found.");
		}
		return donations;
	}

	/**
	 * Approves a Donation
	 * @return Approved Donation object
	 *
	 * @author Murad G.
	 */
	@Transactional
	public Donation approveDonation(IdDto donationId) {
		Donation donation = getDonationById(donationId.getId());

		// Approve it
		donation.setStatus(ApprovalStatus.Approved);

		// Save Loan
		donation = donationRepo.save(donation);
		return donation;
	}

	/**
	 * Rejects a Donation by setting its approval status to Rejected
	 * @return Rejected Loan
	 *
	 * @author Murad G.
	 */
	@Transactional
	public Donation rejectDonation(IdDto donationId) {
		Donation donation = getDonationById(donationId.getId());
		donation.setStatus(ApprovalStatus.Rejected);

		// Save Loan
		donation = donationRepo.save(donation);
		return donation;
	}
}
