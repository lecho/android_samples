package lecho.sample.green_dao_generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class SampleDaoGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "lecho.sample.green_dao.dao");

		Entity faculty = schema.addEntity("Faculty");
		faculty.addIdProperty();
		faculty.addStringProperty("name");
		faculty.addStringProperty("description");
		Property facultyPlaceId = faculty.addLongProperty("placeId")
				.getProperty();

		Entity category = schema.addEntity("Category");
		category.addIdProperty();
		category.addStringProperty("name");
		category.addStringProperty("description");
		Property categoryPlaceId = category.addLongProperty("placeId")
				.getProperty();

		Entity place = schema.addEntity("Place");
		place.addIdProperty();
		place.addStringProperty("name");
		place.addStringProperty("code");
		place.addStringProperty("description");
		place.addToMany(faculty, facultyPlaceId);
		place.addToMany(category, categoryPlaceId);

		new DaoGenerator().generateAll(schema, "./dao-gen");

	}

}
