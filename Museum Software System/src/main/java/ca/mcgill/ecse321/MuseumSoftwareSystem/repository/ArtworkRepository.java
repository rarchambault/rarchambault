package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;

public interface ArtworkRepository extends CrudRepository<Artwork, Integer> {
	public Artwork findArtworkById(Integer id);

	public Artwork findArtworkByName(String name);

	public void deleteArtworkById(int id);
}

