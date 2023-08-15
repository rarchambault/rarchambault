package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.IdDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.LoanRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.LoanResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.PayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.LoanRepository;

@Service
public class LoanService {

	// Autowire all necessary services and the repository
	@Autowired
	LoanRepository loanRepo;

	@Autowired
	VisitorRepository visitorRepo;

	@Autowired
	VisitorService visitorService;

	@Autowired
	ArtworkService artworkService;

	@Autowired
	MuseumService museumService;

	/**
	 * Gets all loans from the database
	 *
	 * @return List of all loans from the database
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Iterable<Loan> getAllLoans() {

		Iterable<Loan> loans = loanRepo.findAll();

		if (loans == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Loans not found.");
		}

		return loans;
	}

	/**
	 * Looks through database to find a Loan object with the given ID, throws an
	 * exception if it could not find it.
	 *
	 * @param id ID with which we want to look for the Loan
	 * @return The retrieved Loan object
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan getLoanById(int id) {
		Loan loan = loanRepo.findLoanById(id);

		if (loan == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Loan not found.");
		}
		return loan;
	}

	/**
	 * Looks through database to retrieve Loan objects with the given loanee email
	 * address, throws an exception if it was not found
	 *
	 * @param loaneeEmailAddress Email address with which we want to retrieve the
	 *                           loans
	 * @return A list of loans for the given loanee email address
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public List<Loan> getLoansByLoaneeEmailAddress(String loaneeEmailAddress) {
		List<Loan> loans = loanRepo.findLoansByLoaneeEmailAddress(loaneeEmailAddress);
		if (loans == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Loans not found.");
		}
		return loans;
	}

	/**
	 * Looks through database to retrieve all loans associated with the given
	 * artwork ID
	 *
	 * @param artworkId ID of the artwork for which we want to find loans
	 * @return List of retrieved Loan objects
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public List<Loan> getLoansByArtworkId(int artworkId) {
		List<Loan> loans = loanRepo.findLoansByArtworkId(artworkId);

		if (loans == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Loans not found.");
		}
		return loans;
	}

	/**
	 * Gets all loans with a given status from the database
	 * @param status Status with which we want to search
	 * @return List of all loans with particular status from the database
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public List<Loan> getLoansByStatus(ApprovalStatus status) {
		List<Loan> loans = loanRepo.findLoansByStatus(status);

		if (loans == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Loans not found.");
		}

		return loans;
	}

	/**
	 * Approves a loan if all its fees have been paid for
	 *
	 * @param loanId ID of the Loan we want to approve
	 * @return Approved Loan object
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan approveLoan(IdDto loanId) {
		Loan loan = getLoanById(loanId.getId());

		// Check if the loan has been paid -> cannot approve otherwise
		if (loan.getIsFeePaid() == false) {
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST,
					"Cannot approve a loan that has not been paid for yet.");
		}

		// Approve it
		loan.setStatus(ApprovalStatus.Approved);

		// Start the loan and set the end date to in two weeks
		LocalDate now = LocalDate.now();
		loan.setStartDate(Date.valueOf(now));
		loan.setEndDate(Date.valueOf(now.plusWeeks(2)));

		// Set the artwork as "not in museum"
		Artwork artwork = artworkService.setArtworkIsInMuseum(loan.getArtwork().getId(), false);
		loan.setArtwork(artwork);

		// Save Loan
		loan = loanRepo.save(loan);
		return loan;
	}

	/**
	 * Rejects a Loan by setting its approval status to Rejected
	 *
	 * @param loanId ID of the Loan we want to reject
	 * @return Rejected Loan
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan rejectLoan(IdDto loanId) {
		Loan loan = getLoanById(loanId.getId());
		loan.setStatus(ApprovalStatus.Rejected);

		// Save Loan
		loan = loanRepo.save(loan);
		return loan;
	}

	/**
	 * Renew a loan for the next 2 weeks, if possible, and pay for the renewal
	 *
	 * @param request PayRequestDto containing Loan ID and input amount
	 * @return Renewed Loan
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan renewLoan(PayRequestDto request) {
		Loan loan = getLoanById(request.getLoanId());

		// Check if the loan has been approved -> cannot renew otherwise
		if (loan.getStatus() != ApprovalStatus.Approved) {
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST,
					"Cannot renew a loan that has not been approved.");
		}

		// Check if there is a visitor on the waitlist -> cannot renew if there is
		Visitor visitorOnWaitlist = loan.getArtwork().getVisitorOnWaitlist();
		if (visitorOnWaitlist != null) {
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST,
					"Cannot renew loan because there is a visitor on the waitlist for this artwork");
		}

		// Increment end date by two weeks
		Date newEndDate = Date.valueOf(loan.getEndDate().toLocalDate().plusWeeks(2));
		loan.setEndDate(newEndDate);

		// Increment number of loans
		loan.setNumberOfRenewals(loan.getNumberOfRenewals() + 1);

		payLoan(request);

		// Save Loan
		loan = loanRepo.save(loan);

		return loan;
	}

	/**
	 * Sets a loan as "late", add a late fee and set the IsFeePaid attribute to
	 * false
	 *
	 * @param loanId ID of the Loan we want to set to Late
	 * @return The Loan set to late
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan setLoanLate(int loanId) {
		Loan loan = getLoanById(loanId);

		loan.setIsLate(true);
		loan.setLateFee(loan.getArtwork().getLoanFee() / 10);
		loan.setIsFeePaid(false);

		// Save loan
		loan = loanRepo.save(loan);
		return loan;
	}

	/**
	 * Pays all fees associated with a loan
	 *
	 * @param request PayRequestDto containing loan ID and input amount
	 * @return Loan with IsFeePaid attribute set to true
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan payLoan(PayRequestDto request) {
		Loan loan = getLoanById(request.getLoanId());

		double price = loan.getArtwork().getLoanFee();

		if (request.getInputAmount() < price) {
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST, "Input amount $" + request.getInputAmount()
					+ " does not cover the total price of the loan which is $" + price);
		}

		loan.setIsFeePaid(true);

		// Save Loan
		loan = loanRepo.save(loan);

		return loan;
	}

	/**
	 * Returns a loan, sets it to late if it is returned after the endDate of the
	 * Loan and pays for the late fee if applicable
	 *
	 * @param request PayRequestDto containing loan ID and input amount
	 * @return Returned Loan
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Loan returnLoan(PayRequestDto request) {
		Loan loan = getLoanById(request.getLoanId());

		if (loan.getEndDate().before(Date.valueOf(LocalDate.now()))) {
			setLoanLate(request.getLoanId());
		}

		double lateFee = loan.getLateFee();

		if (request.getInputAmount() < lateFee) {
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST, "Input amount $" + request.getInputAmount()
					+ " does not cover the late fee for the loan which is $" + lateFee);
		}

		loan.setIsFeePaid(true);
		loan.setStatus(ApprovalStatus.Returned);

		Artwork loanArtwork = loan.getArtwork();
		int artworkId = loanArtwork.getId();

		// Set the artwork as "back in the museum"
		Artwork artwork = artworkService.setArtworkIsInMuseum(artworkId, true);

		// If there was a visitor on the waitlist, create a new loan for them
		Visitor visitorOnWaitlist = loanArtwork.getVisitorOnWaitlist();
		if (visitorOnWaitlist != null)
		{
			createLoan(new LoanRequestDto(visitorOnWaitlist.getEmailAddress(), artworkId, loan.getMuseum().getId()));
			artworkService.setArtworkVisitorOnWaitlist(artworkId, null);
		}

		loan.setArtwork(artwork);

		// Save Loan
		loan = loanRepo.save(loan);

		return loan;
	}

	/**
	 * Creates a loan with all input parameters
	 *
	 * @param loanRequest Request to post a Loan with all required parameters
	 * @return Created Loan
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public LoanResponseDto createLoan(LoanRequestDto loanRequest) {
		Visitor loanee = visitorService.retrieveVisitorByEmail(loanRequest.getLoaneeEmailAddress());

		Artwork artwork = artworkService.findArtworkById(loanRequest.getArtworkId());
		Museum museum = museumService.getMuseumById(loanRequest.getMuseumId());




		Loan loan = new Loan();
		loan.setStatus(ApprovalStatus.InReview);
		loan.setIsLate(false);
		loan.setIsFeePaid(false);
		loan.setNumberOfRenewals(0);
		loan.setLoanee(loanee);
		loan.setArtwork(artwork);
		loan.setMuseum(museum);


		// If the artwork is already taken out on a loan, add loanee on the waitlist and
		// do not create a loan
		if (!artwork.getIsInMuseum()) {
			if (artwork.getVisitorOnWaitlist() == null){
				artworkService.setArtworkVisitorOnWaitlist(artwork.getId(), loanee);
				return null;
			}

			if (artwork.getVisitorOnWaitlist().getEmailAddress() != loanee.getEmailAddress()) {
				throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST, "Error: artwork is already on loan and there is already someone on the waitlist");
			} else {
				throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST, "Error: artwork is already on loan and you are already on the waitlist");
			}
		}

		// Save new object and return it
		Loan savedLoan = loanRepo.save(loan);

		if (artwork.getIsAvailableForLoan() == false) {
			rejectLoan(new IdDto(savedLoan.getId()));
			throw new MuseumSoftwareSystemException(HttpStatus.BAD_REQUEST,
					"Cannot take artwork " + artwork.getId() + " on a loan since it is not available for loan");
		}



		return new LoanResponseDto(savedLoan);
	}
}
