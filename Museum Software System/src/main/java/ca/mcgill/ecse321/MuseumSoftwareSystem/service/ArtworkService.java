package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkUpdateRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ArtworkRepository;

/**
 * Service layer implementation for business logic behind controller calls for
 * the Artwork class
 *
 * @author Pinak Ghosh
 */
@Service
public class ArtworkService {

	@Autowired
	ArtworkRepository artworkRepo;

	@Autowired
	private MuseumService museumService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private VisitorService visitorService;

	/**
	 * Gets all artworks from the database.
	 *
	 * @return All artwork records in the database
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Iterable<Artwork> getAllArtworks() {

		Iterable<Artwork> artworks = artworkRepo.findAll();

		if (artworks == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Artworks not found.");
		}

		return artworks;
	}

	/**
	 *
	 * Gets Artwork Given Id
	 *
	 * @param id
	 * @return Artwork object
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public Artwork findArtworkById(int id) {

		Artwork artwork = artworkRepo.findArtworkById(id);

		if (artwork == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Artwork Not Found.");
		}

		return artwork;
	}

	/**
	 *
	 * Creates a new Artwork object in database
	 *
	 * @param artworkRequest
	 * @return Saved Artwork
	 *
	 *
	 * @author Pinak Ghosh
	 *
	 */
	@Transactional
	public ArtworkResponseDto createArtwork(ArtworkRequestDto artworkRequest) {

		Museum museum = museumService.getMuseumById(artworkRequest.getMuseumId());
		Room room = roomService.findRoomById(artworkRequest.getRoomId());

		Visitor visitor = visitorService.retrieveVisitorByEmail(artworkRequest.getVisitorOnWaitingListEmail());

		Artwork artwork = new Artwork();

		if (visitor != null) {
			artwork.setVisitorOnWaitlist(visitor);
		}

		artwork.setName(artworkRequest.getName());
		artwork.setLoanFee(artworkRequest.getLoanFee());
		artwork.setRoom(room);
		artwork.setMuseum(museum);
		artwork.setIsAvailableForLoan(artworkRequest.getIsAvailableForLoan());
		artwork.setIsInMuseum(artworkRequest.getIsInMuseum());

		artwork = artworkRepo.save(artwork);
		return new ArtworkResponseDto(artwork);
	}

	/**
	 * Updates an artwork entry based on the provided ID
	 * @param artworkRequest Artwork request with all required parameters to update
	 * @return Modified artwork, wrapped in an ArtworkResponseDto object
	 *
	 * @author Roxanne Archambault
	 */
	@Transactional
	public ArtworkResponseDto updateArtwork(ArtworkUpdateRequestDto artworkRequest) {

		Artwork artwork = findArtworkById(artworkRequest.getId());

		Museum museum = museumService.getMuseumById(artworkRequest.getMuseumId());
		Room room = roomService.findRoomById(artworkRequest.getRoomId());
		Visitor visitor = visitorService.retrieveVisitorByEmail(artworkRequest.getVisitorOnWaitingListEmail());

		if (visitor != null) {
			artwork.setVisitorOnWaitlist(visitor);
		}

		artwork.setName(artworkRequest.getName());
		artwork.setLoanFee(artworkRequest.getLoanFee());
		artwork.setRoom(room);
		artwork.setMuseum(museum);
		artwork.setIsAvailableForLoan(artworkRequest.getIsAvailableForLoan());
		artwork.setIsInMuseum(artworkRequest.getIsInMuseum());

		artwork = artworkRepo.save(artwork);
		return new ArtworkResponseDto(artwork);
	}

	/**
	 *
	 * Gets Artwork given Name
	 *
	 * @param name
	 * @return Saved Artwork
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public ArtworkResponseDto findArtworkByName(String name) {
		Artwork artwork = artworkRepo.findArtworkByName(name);

		if (artwork == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Artwork Not Found.");
		}

		return new ArtworkResponseDto(artwork);
	}

	/**
	 *
	 * Deletes Artwork Given Id
	 *
	 * @param id
	 * @return Artwork object
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public Artwork deleteArtworkById(int id) {
		Artwork artwork = artworkRepo.findArtworkById(id);

		artworkRepo.deleteById(id);

		return artwork;
	}

	/**
	 *
	 * Sets artwork in the museum
	 *
	 * @param artworkId
	 * @param isInMuseum
	 * @return Artwork object
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public Artwork setArtworkIsInMuseum(int artworkId, boolean isInMuseum) {

		Artwork artwork = findArtworkById(artworkId);

		artwork.setIsInMuseum(isInMuseum);
		artwork = artworkRepo.save(artwork);

		return artwork;
	}

	/**
	 *
	 * Sets visitor on waiting list for artwork
	 *
	 * @param artworkId
	 * @param visitor
	 * @return Artwork object
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public Artwork setArtworkVisitorOnWaitlist(Integer artworkId, Visitor visitor) {
		Artwork artwork = findArtworkById(artworkId);
		artwork.setVisitorOnWaitlist(visitor);
		artwork = artworkRepo.save(artwork);
		return artwork;
	}

}
