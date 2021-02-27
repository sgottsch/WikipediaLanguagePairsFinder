package de.l3s.wikipedia_language_pair_finder.process;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.l3s.wikipedia_language_pair_finder.model.Entity;
import de.l3s.wikipedia_language_pair_finder.model.EntityCollection;
import de.l3s.wikipedia_language_pair_finder.model.Language;

public class LanguagePairsFinder {

	private static String TAB = "\t";

	public static void main(String[] args) {

		String dataPath = args[0];
		String dumpDate = args[1];
		String resultFile = args[2];

		String languagesString = args[3];
		List<Language> languages = new ArrayList<Language>();
		for (String language : languagesString.split(","))
			languages.add(Language.valueOf(language.toUpperCase()));

		EntityCollection entityCollection = new EntityCollection();
		entityCollection.init(languages);

		for (Language language : languages) {

			Set<Language> targetLanguages = new HashSet<Language>();
			for (Language targetLanguage : languages) {
				if (language != targetLanguage)
					targetLanguages.add(targetLanguage);
			}

			WikipediaFilesProcessor rtc = new WikipediaFilesProcessor(language, targetLanguages, dataPath, dumpDate,
					entityCollection);
			rtc.init();
			rtc.getRedirects();
			rtc.loadLangLinks();
			rtc.addRedirects();
			rtc.clean();
		}

		Entity example1 = entityCollection.getEntity(Language.DE, "Bananen");
		System.out.println("Example 1: Bananen (DE)");

		if (example1 == null) {
			System.out.println(" -> null");
		} else {
			for (Language language : languages) {
				System.out.println(language.getLanguageLowerCase() + " -> " + example1.getTitle(language));
				for (String redirect : example1.getRedirects(language)) {
					System.out.println(" " + redirect);
				}
			}
		}

		System.out.println("Write file.");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(resultFile);
			List<String> headerString = new ArrayList<String>();
			for (Language language : languages) {
				headerString.add(language.getLanguageLowerCase());
			}
			for (Language language : languages) {
				headerString.add(language.getLanguageLowerCase() + "_redirects");
			}
			writer.println(StringUtils.join(headerString, TAB));

			for (Entity entity : entityCollection.getAllEntities()) {
				List<String> entityString = new ArrayList<String>();
				for (Language language : languages) {
					entityString.add(getStringOrNull(entity.getTitle(language)));
				}
				for (Language language : languages) {
					entityString.add(getStringOrNull(StringUtils.join(entity.getRedirects(language), " ")));
				}
				writer.println(StringUtils.join(entityString, TAB));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

	private static String getStringOrNull(String value) {
		if (value == null)
			return "";
		else
			return value;
	}

}
