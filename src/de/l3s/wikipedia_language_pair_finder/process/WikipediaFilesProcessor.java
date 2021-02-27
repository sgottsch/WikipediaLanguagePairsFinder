package de.l3s.wikipedia_language_pair_finder.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.l3s.wikipedia_language_pair_finder.data.FilesDownloader;
import de.l3s.wikipedia_language_pair_finder.model.Entity;
import de.l3s.wikipedia_language_pair_finder.model.EntityCollection;
import de.l3s.wikipedia_language_pair_finder.model.Language;
import de.l3s.wikipedia_language_pair_finder.util.FileLoader;
import de.l3s.wikipedia_language_pair_finder.util.FileName;
import gnu.trove.map.hash.TIntObjectHashMap;

public class WikipediaFilesProcessor {

	private EntityCollection entityCollection;
	private String dataPath;
	private String dumpDate;
	private Language language;
	private Set<Language> targetLanguages;

	private Map<String, String> redirects;
	private TIntObjectHashMap<String> pageIds;

	public WikipediaFilesProcessor(Language language, Set<Language> targetLanguages, String dataPath, String dumpDate,
			EntityCollection entityCollection) {
		this.dataPath = dataPath;
		this.dumpDate = dumpDate;
		this.language = language;
		this.targetLanguages = targetLanguages;
		this.entityCollection = entityCollection;
	}

	void init() {
		FilesDownloader fd = new FilesDownloader(language, dumpDate, dataPath);
		fd.downloadWikipediaFiles();
	}

	public Map<String, String> getRedirects() {

		System.out.println("Load redirects for " + language + ".");
		Map<Integer, String> redirectsWithIds = loadRedirects(language);
		System.out.println(redirectsWithIds.keySet().size() + " redirects. Load labels of redirect pages.");
		this.redirects = addLabelsToRedirects(redirectsWithIds, language);
		System.out.println("Done loading redirects for " + language + ".");

		return redirects;
	}

	private Map<String, String> addLabelsToRedirects(Map<Integer, String> redirectsWithIds, Language language) {

		this.pageIds = new TIntObjectHashMap<String>();

		Map<String, String> redirects = new HashMap<String, String>();

		BufferedReader br = null;
		try {
			try {
				br = FileLoader.getReader(dataPath, language, FileName.WIKIPEDIA_PAGE_INFOS);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("INSERT INTO")) {

					line = line.substring(line.indexOf("("));
					line = line.substring(0, line.lastIndexOf(")"));

					for (String part : line.split("\\),\\(")) {

						List<String> parts = splitInsertLine(part);

						int nameSpace = Integer.valueOf(parts.get(1));
						if (nameSpace != 0)
							continue;

						String p0 = parts.get(0);
						if (p0.startsWith("("))
							p0 = p0.substring(1);

						if (p0.isEmpty())
							continue;
						if (parts.get(2).isEmpty())
							continue;

						int pageId = Integer.valueOf(p0);

						try {
							String targetTitle = parts.get(2).substring(1, parts.get(2).length() - 1);
							pageIds.put(pageId, targetTitle);

							if (redirectsWithIds.containsKey(pageId)) {
								redirects.put(targetTitle, redirectsWithIds.get(pageId));
							}
						} catch (StringIndexOutOfBoundsException e) {
							continue;
						}

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return redirects;
	}

	private Map<Integer, String> loadRedirects(Language language) {

		Map<Integer, String> redirects = new HashMap<Integer, String>();

		BufferedReader br = null;
		try {
			try {
				br = FileLoader.getReader(dataPath, language, FileName.WIKIPEDIA_REDIRECTS);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String prevPart = null;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("INSERT INTO")) {

					line = line.substring(line.indexOf("("));
					line = line.substring(0, line.lastIndexOf(")"));

					for (String part : line.split("\\),\\(")) {
						List<String> parts = splitInsertLine(part);

						if (parts.get(2).length() <= 1) {
							System.out.println("Problem: " + prevPart + " | " + part);
							continue;
						}

						String pageIdString = parts.get(0);

						if (pageIdString.startsWith("("))
							pageIdString = pageIdString.substring(1);
						int pageId = Integer.valueOf(pageIdString);

						String targetTitle = parts.get(2).substring(1, parts.get(2).length() - 1);

						redirects.put(pageId, targetTitle);

						prevPart = part;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return redirects;
	}

	Map<Integer, String> loadLangLinks() {
		System.out.println("\nLoad language links for " + language + ".");

		Set<String> targetLanguageStrings = new HashSet<String>();
		for (Language targetLanguage : this.targetLanguages) {
			targetLanguageStrings.add(targetLanguage.getLanguageLowerCase());
		}

		Map<Integer, String> redirects = new HashMap<Integer, String>();

		BufferedReader br = null;
		try {
			try {
				br = FileLoader.getReader(dataPath, language, FileName.WIKIPEDIA_LANG_LINKS);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String prevPart = null;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("INSERT INTO")) {

					line = line.substring(line.indexOf("("));
					line = line.substring(0, line.lastIndexOf(")"));

					for (String part : line.split("\\),\\(")) {
						List<String> parts = splitInsertLine(part);

						if (parts.size() < 3 || parts.get(2).length() <= 1) {
							System.out.println("Problem: " + prevPart + " | " + part);
							continue;
						}
						// System.out.println("Okay : " + prevPart + " | " + part);

						String targetLanguageString = parts.get(1).substring(1, parts.get(1).length() - 1);
						if (!targetLanguageStrings.contains(targetLanguageString))
							continue;

						String pageIdString = parts.get(0);

						if (pageIdString.startsWith("("))
							pageIdString = pageIdString.substring(1);
						int pageId = Integer.valueOf(pageIdString);

						String targetTitle = parts.get(2).substring(1, parts.get(2).length() - 1);
						String sourceTitle = this.pageIds.get(pageId);

						if (sourceTitle == null)
							continue;

						Language targetLanguage = Language.valueOf(targetLanguageString.toUpperCase());

						entityCollection.getOrCreateEntity(language, sourceTitle, targetLanguage, targetTitle);

						prevPart = part;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return redirects;
	}

	public void addRedirects() {

		for (String redirect : this.redirects.keySet()) {
			Entity entity = entityCollection.getEntity(language, this.redirects.get(redirect));
			if (entity != null)
				entity.addRedirect(language, redirect.replace(" ", "_"));
		}

	}

	public void clean() {
		this.pageIds = null;
		this.redirects = null;
	}

	private static List<String> splitInsertLine(String line) {
		String[] partsTmp = line.split(",");

		List<String> parts = new ArrayList<String>();

		// remerge comma-separated values, if they are within
		// quotes. For example, 'Rom,_offene Stadt'
		String prefix = null;
		for (String subPart : partsTmp) {
			if (subPart.startsWith("'") && !subPart.endsWith("'"))
				prefix = subPart;
			else if (prefix != null & !subPart.endsWith("'"))
				prefix += "," + subPart;
			else {
				if (prefix != null)
					subPart = prefix + "," + subPart;
				parts.add(subPart);
				prefix = null;
			}
		}

		return parts;
	}

}
