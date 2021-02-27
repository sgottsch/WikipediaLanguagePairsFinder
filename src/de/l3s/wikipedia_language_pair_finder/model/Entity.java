package de.l3s.wikipedia_language_pair_finder.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Entity {

	private Map<Language, NameMap> titles = new HashMap<Language, NameMap>();

	public String getTitle(Language language) {
		if (titles.containsKey(language))
			return titles.get(language).getTitle();
		else
			return null;
	}

	private class NameMap {

		private String title;
		private Set<String> redirects = new HashSet<String>();

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Set<String> getRedirects() {
			return redirects;
		}

	}

	public void addTitle(Language language, String title) {

		NameMap nameMap = this.titles.get(language);
		if (nameMap != null)
			return;

		nameMap = new NameMap();
		nameMap.setTitle(title);
		this.titles.put(language, nameMap);
	}

	public void addRedirect(Language language, String redirect) {
		NameMap nameMap = this.titles.get(language);
		nameMap.getRedirects().add(redirect);
	}

	public Set<String> getRedirects(Language language) {
		if (titles.containsKey(language))
			return titles.get(language).getRedirects();
		else
			return new HashSet<String>();
	}

}
