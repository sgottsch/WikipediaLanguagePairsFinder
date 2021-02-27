package de.l3s.wikipedia_language_pair_finder.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import de.l3s.wikipedia_language_pair_finder.model.Language;
import de.l3s.wikipedia_language_pair_finder.util.FileLoader;
import de.l3s.wikipedia_language_pair_finder.util.FileName;

public class FilesDownloader {

	private Language language;
	private String dumpDate;
	private String dataPath;

	public FilesDownloader(Language language, String dumpDate, String dataPath) {
		super();
		this.language = language;
		this.dumpDate = dumpDate;
		this.dataPath = dataPath;
	}

	public void downloadWikipediaFiles() {

		File folder = new File(dataPath + "/" + language.getLanguageLowerCase());

		if (folder.exists())
			return;

		folder.mkdirs();

		String wikiName = language.getWiki();
		String baseUrl = "https://dumps.wikimedia.org/" + wikiName + "/" + dumpDate + "/";

		downloadFile(baseUrl + wikiName + "-" + dumpDate + "-redirect.sql.gz",
				FileLoader.getFileNameWithPath(dataPath, FileName.WIKIPEDIA_REDIRECTS, language));
		downloadFile(baseUrl + wikiName + "-" + dumpDate + "-page.sql.gz",
				FileLoader.getFileNameWithPath(dataPath, FileName.WIKIPEDIA_PAGE_INFOS, language));
		downloadFile(baseUrl + wikiName + "-" + dumpDate + "-langlinks.sql.gz",
				FileLoader.getFileNameWithPath(dataPath, FileName.WIKIPEDIA_LANG_LINKS, language));
	}

	private File downloadFile(String url, String targetPath) {

		URL website = null;
		try {
			website = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fos = null;

		int tries = 0;

		boolean succ = false;
		while (!succ) {
			tries += 1;
			System.out.println("Download file " + url + " to " + targetPath + ".");

			try {
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				fos = new FileOutputStream(targetPath);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				succ = true;
			} catch (FileNotFoundException e) {
				System.out.println(" Warning: File does not exist.");
				succ = true;
			} catch (IOException e) {
				if (e.getMessage().contains("response code: 503")) {

					if (tries == 5) {
						System.err.println("Could not download " + url + ". Continue.");
						return null;
					}

					// if server is overload: wait for 1 minute and re-try
					System.out.println(e.getMessage());
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} else
					e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("=> Done.");

		return new File(targetPath);
	}

}
