package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.LoanService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/loan")
public class LoanController {
	@Autowired
	LoanService loanService;

	/**
	 * Gets all loans saved in the database
	 * @return Iterable of all loans in the database
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping()
	public ResponseEntity<Iterable<LoanResponseDto>> getAllLoans() {
		Iterable<Loan> loans = loanService.getAllLoans();

		ArrayList<LoanResponseDto> loanResponses = new ArrayList<LoanResponseDto>();

		for (var loan : loans) {
			loanResponses.add(new LoanResponseDto(loan));
		}

		return new ResponseEntity<Iterable<LoanResponseDto>>(loanResponses, HttpStatus.OK);
	}

	/**
	 * Creates a new Loan from a LoanRequest
	 *
	 * @param request Loan request with appropriate parameters required to create it
	 * @return Created Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<LoanResponseDto> createLoan(@Valid @RequestBody LoanRequestDto request) {
		LoanResponseDto loan = loanService.createLoan(request);
		return new ResponseEntity<LoanResponseDto>(loan, HttpStatus.CREATED);
	}

	/**
	 * Retrieves a Loan from database from its id
	 *
	 * @param id ID of the Loan we want to retrieve
	 * @return Retrieved Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable int id) {
		Loan loan = loanService.getLoanById(id);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Retrieves a list of Loans from db with the email address of a loanee
	 *
	 * @param emailAddress Email address with which we want to retrieve the loans
	 * @return List of LoanResponseDtos retrieved
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping("/loaneeEmailAddress/{emailAddress}")
	public ResponseEntity<List<LoanResponseDto>> getLoansByLoaneeEmailAddress(@PathVariable String emailAddress) {
		List<Loan> loans = loanService.getLoansByLoaneeEmailAddress(emailAddress);
		List<LoanResponseDto> loanDtos = new ArrayList<LoanResponseDto>();

		for (int i = 0; i < loans.size(); i++) {
			loanDtos.add(new LoanResponseDto(loans.get(i)));
		}

		return new ResponseEntity<List<LoanResponseDto>>(loanDtos, HttpStatus.OK);
	}

	/**
	 * Retrieves a list of Loans from db from the id of an artwork
	 *
	 * @param artworkId ID of the artwork with which we want to retrieve the loans
	 * @return List of LoanResponseDtos retrieved
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping("/artworkId/{artworkId}")
	public ResponseEntity<List<LoanResponseDto>> getLoansByArtworkId(@PathVariable int artworkId) {
		List<Loan> loans = loanService.getLoansByArtworkId(artworkId);

		List<LoanResponseDto> loanDtos = new ArrayList<LoanResponseDto>();

		for (int i = 0; i < loans.size(); i++) {
			loanDtos.add(new LoanResponseDto(loans.get(i)));
		}
		return new ResponseEntity<List<LoanResponseDto>>(loanDtos, HttpStatus.OK);
	}

	/**
	 * Gets all loans from the database with the given status
	 * @param status Status with which we want to retrieve loans
	 * @return List of all loans with a given status
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping("/status/{status}")
	public ResponseEntity<Iterable<LoanResponseDto>> getLoansByStatus(@PathVariable ApprovalStatus status) {
		Iterable<Loan> loans = loanService.getLoansByStatus(status);

		ArrayList<LoanResponseDto> loanResponses = new ArrayList<LoanResponseDto>();

		for (var loan : loans) {
			loanResponses.add(new LoanResponseDto(loan));
		}

		return new ResponseEntity<Iterable<LoanResponseDto>>(loanResponses, HttpStatus.OK);
	}

	/**
	 * Approves a loan
	 *
	 * @param loanId ID of the loan we want to approve
	 * @return Approved loan (LoanReponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/approve")
	public ResponseEntity<LoanResponseDto> approveLoan(@RequestBody IdDto loanId) {
		Loan loan = loanService.approveLoan(loanId);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Rejects a loan
	 *
	 * @param loanId ID of the Loan we want to reject
	 * @return Rejected Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/reject")
	public ResponseEntity<LoanResponseDto> rejectLoan(@RequestBody IdDto loanId) {
		Loan loan = loanService.rejectLoan(loanId);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Renews a loan for the next 2 weeks, if possible, and pays its fees
	 *
	 * @param request      Request containing the loan ID and the pay load
	 * @return Renewed Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/renew")
	public ResponseEntity<LoanResponseDto> renewLoan(@RequestBody PayRequestDto request) {
		Loan loan = loanService.renewLoan(request);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Sets a loan as "late"
	 *
	 * @param loanId ID of the loan we want to set to late
	 * @return Loan set to late (LoanResponseDto)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/setLate")
	public ResponseEntity<LoanResponseDto> setLoanLate(@RequestBody int loanId) {
		Loan loan = loanService.setLoanLate(loanId);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Pays all fees associated with a loan
	 *
	 * @param payRequest      Request containing the loan ID and the pay load
	 * @return Paid Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/pay")
	public ResponseEntity<LoanResponseDto> payLoan(@Valid @RequestBody PayRequestDto payRequest) {
		Loan loan = loanService.payLoan(payRequest);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}

	/**
	 * Returns a loan
	 *
	 * @param request      Request containing the loan ID and the pay load
	 * @return Returned Loan (LoanResponseDto object)
	 *
	 * @author Roxanne Archambault
	 */
	@PostMapping("/return")
	public ResponseEntity<LoanResponseDto> returnLoan(@RequestBody PayRequestDto request) {
		Loan loan = loanService.returnLoan(request);
		return new ResponseEntity<LoanResponseDto>(new LoanResponseDto(loan), HttpStatus.OK);
	}
}
