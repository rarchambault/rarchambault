package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import java.util.List;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;

public interface LoanRepository extends CrudRepository<Loan, Integer> {
	public Loan findLoanById(int id);
	public List<Loan> findLoansByLoaneeEmailAddress(String loaneeEmailAddress);
	public List<Loan> findLoansByArtworkId(int artworkId);
	public List<Loan> findLoansByStatus(ApprovalStatus status);
}