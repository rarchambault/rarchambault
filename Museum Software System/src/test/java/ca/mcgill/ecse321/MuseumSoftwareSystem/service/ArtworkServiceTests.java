package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ArtworkRepository;

@ExtendWith(MockitoExtension.class)
public class ArtworkServiceTests {

	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	ArtworkRepository artworkRepo;

	@Mock
	MuseumService museumService;

	@Mock
	VisitorService visitorService;

	@Mock
	RoomService roomService;

	// Get a service that uses the mock repository
	@InjectMocks
	ArtworkService artworkService;

	/**
	 *
	 * Tests method that attempts to find Artwork by Id
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testFindArtworkById() {

		Museum museum = new Museum("Owner@mail.ca", "Rich Homie Quan", "Owner");
		RoomType type = RoomType.Large;
		Room room = new Room(type, museum);
		Visitor visitor = new Visitor("Visitor@mail.ca", "pass123", "Visitor", museum);

		final int id = 1;
		final String name = "Owner";
		final boolean isAvaliable = true;
		final double loanFee = 1.00;
		final Museum aMuseum = museum;
		final Room aRoom = room;
		final Visitor aVisitor = visitor;
		final Artwork anArtwork = new Artwork();

		anArtwork.setMuseum(aMuseum);
		anArtwork.setName(name);
		anArtwork.setIsAvailableForLoan(isAvaliable);
		anArtwork.setLoanFee(loanFee);
		anArtwork.setVisitorOnWaitlist(aVisitor);
		anArtwork.setRoom(aRoom);

		when(artworkRepo.findArtworkById(id)).thenAnswer((InvocationOnMock invocation) -> anArtwork);
		Artwork artwork = artworkService.findArtworkById(id);

		assertNotNull(artwork);
		assertEquals(name, artwork.getName());
		assertEquals(isAvaliable, artwork.getIsAvailableForLoan());
		assertEquals(loanFee, artwork.getLoanFee());
		assertEquals("Visitor@mail.ca", artwork.getVisitorOnWaitlist().getEmailAddress());

	}


	/**
	 * Tests to see if can finding artwork can be invalid
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testFindArtworkByInvalidId() {

		final int invalidId = 99;

		when(artworkRepo.findArtworkById(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> artworkService.findArtworkById(invalidId));

		assertEquals("Artwork Not Found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

	}

	/**
	 *
	 * Test method that creates an Artwork object from pre-determined parameters
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testCreateArtwork() {// finish

		Museum museum = new Museum("Owner@mail.ca", "Rich Homie Quan", "Owner");
		RoomType type = RoomType.Large;
		Room room = new Room(type, museum);
		Visitor visitor = new Visitor("Visitor@mail.ca", "pass123", "Visitor", museum);

		final int id = 1;
		final String name = "Owner";
		final boolean isAvaliable = true;
		final double loanFee = 1.00;
		final Museum aMuseum = museum;
		final Room aRoom = room;
		final Visitor aVisitor = visitor;
		final Artwork anArtwork = new Artwork();

		anArtwork.setMuseum(aMuseum);
		anArtwork.setName(name);
		anArtwork.setIsAvailableForLoan(isAvaliable);
		anArtwork.setLoanFee(loanFee);
		anArtwork.setVisitorOnWaitlist(aVisitor);
		anArtwork.setRoom(aRoom);

		ArtworkRequestDto request = new ArtworkRequestDto();
		request.setMuseumId(museum.getId());
		request.setIsAvailableForLoan(isAvaliable);
		request.setLoanFee(loanFee);
		request.setName(name);
		request.setRoomId(room.getId());
		request.setVisitorOnWaitingListEmail(visitor.getEmailAddress());

		when(roomService.findRoomById(aRoom.getId())).thenAnswer((InvocationOnMock invocation) -> aRoom);
		when(visitorService.retrieveVisitorByEmail(aVisitor.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> aVisitor);
		when(museumService.getMuseumById(aMuseum.getId())).thenAnswer((InvocationOnMock invocation) -> aMuseum);
		when(artworkRepo.save(any(Artwork.class))).thenAnswer((InvocationOnMock invocation) -> anArtwork);

		ArtworkResponseDto returnedArtwork = artworkService.createArtwork(request);

		assertNotNull(returnedArtwork);
		assertEquals(name, returnedArtwork.getName());
		assertEquals("Owner", returnedArtwork.getMuseum().getOwner().getName());

	}

	/**
	 *
	 * Tests method that deletes artwork object
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testDeleteArtwork() {
		Museum museum = new Museum("Owner@mail.ca", "Rich Homie Quan", "Owner");
		RoomType type = RoomType.Large;
		Room room = new Room(type, museum);
		Visitor visitor = new Visitor("Visitor@mail.ca", "pass123", "Visitor", museum);

		final int id = 1;
		final String name = "Owner";
		final boolean isAvaliable = true;
		final double loanFee = 1.00;
		final Museum aMuseum = museum;
		final Room aRoom = room;
		final Visitor aVisitor = visitor;
		final Artwork anArtwork = new Artwork();

		anArtwork.setMuseum(aMuseum);
		anArtwork.setName(name);
		anArtwork.setIsAvailableForLoan(isAvaliable);
		anArtwork.setLoanFee(loanFee);
		anArtwork.setVisitorOnWaitlist(aVisitor);
		anArtwork.setRoom(aRoom);

		ArtworkRequestDto request = new ArtworkRequestDto();
		request.setMuseumId(museum.getId());
		request.setIsAvailableForLoan(isAvaliable);
		request.setLoanFee(loanFee);
		request.setName(name);
		request.setRoomId(room.getId());
		request.setVisitorOnWaitingListEmail(visitor.getEmailAddress());

		when(roomService.findRoomById(aRoom.getId())).thenAnswer((InvocationOnMock invocation) -> aRoom);
		when(visitorService.retrieveVisitorByEmail(aVisitor.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> aVisitor);
		when(museumService.getMuseumById(aMuseum.getId())).thenAnswer((InvocationOnMock invocation) -> aMuseum);
		when(artworkRepo.save(any(Artwork.class))).thenAnswer((InvocationOnMock invocation) -> anArtwork);

		ArtworkResponseDto returnedArtwork = artworkService.createArtwork(request);
		artworkService.deleteArtworkById(anArtwork.getId());

		assertNull(artworkRepo.findArtworkById(returnedArtwork.getId()));
	}

	/**
	 *
	 * Tests method that attempts to add visitor object to waitlist.
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testAddVisitorToWaitList(){
		Museum museum = new Museum("Owner@mail.ca", "Rich Homie Quan", "Owner");
		Room room = new Room(RoomType.Large, museum);
		Visitor visitor = new Visitor("Visitor@mail.ca", "pass123", "Visitor", museum);
		Artwork artwork = new Artwork("name", 1.00, false, true, room, null, museum);
		artwork.setVisitorOnWaitlist(visitor);

		when(roomService.findRoomById(room.getId())).thenAnswer((InvocationOnMock invocation) -> room);
		when(visitorService.retrieveVisitorByEmail(visitor.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> visitor);
		when(museumService.getMuseumById(museum.getId())).thenAnswer((InvocationOnMock invocation) -> museum);
		when(artworkRepo.save(any(Artwork.class))).thenAnswer((InvocationOnMock invocation) -> artwork);

		ArtworkRequestDto request = new ArtworkRequestDto(artwork);
		ArtworkResponseDto returnedArtwork = artworkService.createArtwork(request);

		assertEquals(returnedArtwork.getVisitorOnWaitlist().getEmailAddress(), artwork.getVisitorOnWaitlist().getEmailAddress());
	}

	// Other tests
}
