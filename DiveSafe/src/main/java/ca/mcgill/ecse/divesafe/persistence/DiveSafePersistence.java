package ca.mcgill.ecse.divesafe.persistence;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.model.DiveSafe;

/**
 * The methods in this class were adapted from the BTMS version of the
 * Persistence class (provided in tutorials)
 * 
 * @author rarchambault (Roxanne Archambault)
 *
 */
public class DiveSafePersistence {

	private static String filename = "DiveSafeData.json";
	private static JsonSerializer serializer = new JsonSerializer("ca.mcgill.ecse.divesafe");

	public static void setFilename(String filename) {
		DiveSafePersistence.filename = filename;
	}

	public static void save() {
		save(DiveSafeApplication.getDiveSafe());
	}

	public static void save(DiveSafe diveSafe) {
		serializer.serialize(diveSafe, filename);
	}

	public static DiveSafe load() {
		var diveSafe = (DiveSafe) serializer.deserialize(filename);
		// model cannot be loaded - create empty diveSafe
		if (diveSafe == null) {
			diveSafe = new DiveSafe(null, 0, 0);
		} else {
			diveSafe.reinitialize();
		}
		return diveSafe;
	}

}
