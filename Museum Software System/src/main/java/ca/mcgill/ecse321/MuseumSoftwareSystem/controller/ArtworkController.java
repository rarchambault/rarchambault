package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import java.util.ArrayList;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkUpdateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.ArtworkService;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.VisitorService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/artwork")
public class ArtworkController {

    @Autowired
    ArtworkService artworkService;

    @Autowired
    VisitorService visitorService;

    /**
     * Gets all Artwork objects in the database
     *
     * @return All Artwork objects from the database
     *
     * @author Roxanne Archambault
     */
    @GetMapping()
    public ResponseEntity<Iterable<ArtworkResponseDto>> getAllArtworks() {
        Iterable<Artwork> artworks = artworkService.getAllArtworks();

        ArrayList<ArtworkResponseDto> artworkResponses = new ArrayList<ArtworkResponseDto>();

        for (var artwork : artworks) {
            artworkResponses.add(new ArtworkResponseDto(artwork));
        }

        return new ResponseEntity<Iterable<ArtworkResponseDto>>(artworkResponses, HttpStatus.OK);
    }

    /**
     * Returns the Artwork object associated with the specified id from the DB
     *
     *
     * @param id
     * @return ArtworkDto with the Artwork's parameters
     *
     * @author Pinak Ghosh
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ArtworkResponseDto> findArtworkById(@PathVariable int id) {
        Artwork artwork = artworkService.findArtworkById(id);
        return new ResponseEntity<ArtworkResponseDto>(new ArtworkResponseDto(artwork), HttpStatus.OK);
    }

    /**
     *
     * Posts a new Artwork with the input parameters.
     *
     * @param request
     * @return ArtworkDto of the created Artwork object
     *
     * @author Pinak Ghosh
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ArtworkResponseDto> createArtwork(@RequestBody ArtworkRequestDto request) {
        ArtworkResponseDto artwork = artworkService.createArtwork(request);
        return new ResponseEntity<ArtworkResponseDto>(artwork, HttpStatus.CREATED);
    }

    /**
     *
     * Updates an Artwork with the input parameters.
     *
     * @param request
     * @return ArtworkDto of the updated Artwork object
     *
     * @author Roxanne Archambault
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    public ResponseEntity<ArtworkResponseDto> updateArtwork(@RequestBody ArtworkUpdateRequestDto request) {
        ArtworkResponseDto artwork = artworkService.updateArtwork(request);
        return new ResponseEntity<ArtworkResponseDto>(artwork, HttpStatus.OK);
    }

    /**
     *
     * Adds visitor to waitinglist
     *
     * @param request
     * @return Retrieved Artwork (ArtworkResponseDto object)
     *
     * @author Pinak Ghosh
     */
    @PostMapping("/addVisitorToWaitList")
    public ResponseEntity<ArtworkResponseDto> addVisitorToWaitList(@RequestBody ArtworkUpdateRequestDto request) {
        Artwork artwork = artworkService.findArtworkById(request.getId());
        Visitor visitor = visitorService.retrieveVisitorByEmail(request.getVisitorOnWaitingListEmail());

        artwork = artworkService.setArtworkVisitorOnWaitlist(request.getId(), visitor);
        return new ResponseEntity<ArtworkResponseDto>(new ArtworkResponseDto(artwork), HttpStatus.CREATED);
    }

    /**
     *
     * Deletes Artwork
     *
     * @param id
     * @return Retrieved Artwork (ArtworkResponseDto object)
     *
     * @author Pinak Ghosh
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<ArtworkResponseDto> deleteArtworkById(@PathVariable Integer id) {
        Artwork artwork = artworkService.deleteArtworkById(id);
        return new ResponseEntity<ArtworkResponseDto>(new ArtworkResponseDto(artwork), HttpStatus.OK);
    }

}
