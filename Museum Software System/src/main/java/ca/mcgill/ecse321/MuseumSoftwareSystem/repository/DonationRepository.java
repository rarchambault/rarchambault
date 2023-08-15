package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;

import java.util.List;

public interface DonationRepository extends CrudRepository<Donation, Integer> {
	public Donation findDonationById(int id);
	public List<Donation> findDonationsByDonatorEmailAddress(String donatorEmailAddress);
	public Donation findDonationByName(String name);
	public List<Donation> findDonationsByStatus(ApprovalStatus status);
}