package de.l3s.wikipedia_language_pair_finder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityCollection {

	private Map<Language, Map<String, Entity>> entities = new HashMap<Language, Map<String, Entity>>();
	private Set<Entity> allEntities = new HashSet<Entity>();

	public void init(Collection<Language> languages) {
		for (Language language : languages) {
			entities.put(language, new HashMap<String, Entity>());
		}
	}

	public Entity getOrCreateEntity(Language language1, String title1, Language language2, String title2) {

		title1 = title1.replace(" ", "_");
		title2 = title2.replace(" ", "_");

		Entity entity = entities.get(language2).get(title2);

		if (entity == null) {
			entity = entities.get(language1).get(title1);

			if (entity == null) {
				entity = new Entity();
				allEntities.add(entity);
			}
		}

		entity.addTitle(language1, title1);
		entity.addTitle(language2, title2);

		entities.get(language1).put(title1, entity);
		entities.get(language2).put(title2, entity);

		return entity;
	}

	public Entity getEntity(Language language, String title) {

		title = title.replace(" ", "_");

		Entity entity = entities.get(language).get(title);

		return entity;
	}

	public Set<Entity> getAllEntities() {
		return allEntities;
	}

}
