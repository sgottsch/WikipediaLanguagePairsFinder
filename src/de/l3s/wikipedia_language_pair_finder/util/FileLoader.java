package de.l3s.wikipedia_language_pair_finder.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import de.l3s.wikipedia_language_pair_finder.model.Language;

public class FileLoader {

	public static SimpleDateFormat PARSE_DATE_FORMAT = new SimpleDateFormat("G yyyy-MM-dd", Locale.US);
	public static SimpleDateFormat PRINT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static String getFileNameWithPath(String dataPath, FileName fileName, Language language) {
		return dataPath + "/" + language.getLanguage() + "/" + fileName.getFileName();
	}

//	public static String getFileNameWithPath(FileName fileName) {
//		return getPath(fileName);
//	}
//
//
//	public static File getFile(FileName fileName, Language language) {
//		return new File(getPath(fileName, language));
//	}
//
	public static BufferedReader getReader(String dataPath, Language language, FileName fileName) throws IOException {
		return getReader(fileName, getPath(dataPath, language, fileName));
	}

//
//	public static BufferedReader getReader(String path, boolean hasColumnNamesInFirstLine)
//			throws FileNotFoundException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
//		if (hasColumnNamesInFirstLine) {
//			try {
//				br.readLine();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return br;
//	}
//
//	public static LineIterator getLineIterator(FileName fileName) throws IOException {
//		return getLineIterator(getPath(fileName), fileName.hasColumnNamesInFirstLine());
//	}
//
//	public static LineIterator getLineIterator(FileName fileName, Language language) throws IOException {
//		return getLineIterator(getPath(fileName, language), fileName.hasColumnNamesInFirstLine());
//	}
//
//	public static LineIterator getLineIterator(String path, boolean hasColumnNamesInFirstLine) throws IOException {
//
//		LineIterator it = FileUtils.lineIterator(new File(path), "UTF-8");
//
//		if (hasColumnNamesInFirstLine)
//			it.nextLine();
//
//		return it;
//	}
//
//	public static List<String> readLines(FileName fileName) {
//
//		List<String> lines = new ArrayList<String>();
//
//		BufferedReader br = null;
//		try {
//			try {
//				br = FileLoader.getReader(fileName);
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			}
//
//			String line;
//			while ((line = br.readLine()) != null) {
//				lines.add(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return lines;
//	}
//
//	public static List<String> readLines(FileName fileName, Language language) {
//
//		List<String> lines = new ArrayList<String>();
//
//		BufferedReader br = null;
//		try {
//			try {
//				br = FileLoader.getReader(fileName, language);
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			}
//
//			String line;
//			while ((line = br.readLine()) != null) {
//				lines.add(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return lines;
//	}
//
	public static BufferedReader getReader(FileName fileName, String path) throws IOException {
		BufferedReader br = null;
		if (fileName.isGZipped())
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path))));
		else
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

		if (fileName.hasColumnNamesInFirstLine()) {
			try {
				br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return br;
	}
//
//	public static PrintWriter getWriter(FileName fileName, Language language) throws FileNotFoundException {
//		return new PrintWriter(getPath(fileName, language));
//	}
//
//	public static PrintWriter getWriter(FileName fileName) throws FileNotFoundException {
//		return new PrintWriter(getPath(fileName));
//	}
//
//	public static PrintWriter getWriterWithAppend(FileName fileName) throws IOException {
//		return new PrintWriter(new FileWriter(getPath(fileName), true));
//	}
//
//	public static PrintStream getPrintStream(FileName fileName) throws IOException {
//		return new PrintStream(new FileOutputStream(getPath(fileName)));
//	}
//
//	public static String getPath(FileName fileName) {
//		return getPath(fileName, null);
//	}
//
//	public static String getPath(FileName fileName, Language language) {
//
//		// boolean local = true;
//		// if (!new File(LOCAL_RESULTS_FOLDER).exists())
//		// boolean local = false;
//
//		String path = null;
//
//		// if (local) {
//		// if (fileName.isRawData())
//		// path = LOCAL_RAW_DATA_FOLDER;
//		// else if (fileName.isResultsData())
//		// path = LOCAL_RESULTS_FOLDER;
//		// else if (fileName.isMetaData())
//		// path = LOCAL_META_FOLDER;
//		// } else {
//		// if (fileName.isRawData())
//		// path = ONLINE_RAW_DATA_FOLDER;
//		// else if (fileName.isResultsData())
//		// path = ONLINE_RESULTS_FOLDER;
//		// else if (fileName.isMetaData())
//		// path = ONLINE_META_FOLDER;
//		// }
//
//		if (!fileName.isFolder()) {
//			String fileNameString = fileName.getFileName();
//			if (language != null && fileNameString.contains("$lang$"))
//				fileNameString = fileNameString.replace("$lang$", language.getLanguage().toLowerCase());
//
//			return path + "/" + fileNameString;
//		} else
//			return path;
//	}
//
//	public static List<File> getFilesList(FileName folderName, Language language) {
//
//		if (!folderName.isFolder())
//			throw new IllegalArgumentException("Folder expected, file given: " + folderName.getFileName() + ".");
//
//		File dir = new File(getPath(folderName, language));
//
//		File[] directoryListing = dir.listFiles();
//		List<File> directoryListingWithPrefix = new ArrayList<File>();
//
//		for (File file : directoryListing) {
//			if (file.getName().startsWith(folderName.getFileName())) {
//				directoryListingWithPrefix.add(file);
//			}
//		}
//
//		return directoryListingWithPrefix;
//	}
//
//	public static List<File> getFilesList(FileName folderName) {
//
//		if (!folderName.isFolder())
//			throw new IllegalArgumentException("Folder expected, file given: " + folderName.getFileName() + ".");
//
//		File dir = new File(getPath(folderName));
//
//		File[] directoryListing = dir.listFiles();
//		List<File> directoryListingWithPrefix = new ArrayList<File>();
//
//		for (File file : directoryListing) {
//			if (file.getName().startsWith(folderName.getFileName())) {
//				directoryListingWithPrefix.add(file);
//			}
//		}
//
//		return directoryListingWithPrefix;
//	}
//
//	public static PrintStream getPrintStream(FileName fileName, Language language) throws IOException {
//		return new PrintStream(new FileOutputStream(getPath(fileName, language)));
//	}
//
//	public static String readFile(FileName fileName) throws IOException {
//		return readFile(new File(getPath(fileName)));
//	}
//
//	public static String readFile(File file) throws IOException {
//
//		String path = file.getAbsolutePath();
//
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return new String(encoded);
//	}
//
//	public static boolean fileExists(FileName fileName, Language language) {
//		String path = getPath(fileName, language);
//		File f = new File(path);
//		return f.exists();
//	}
//
//	public static boolean fileExists(FileName fileName) {
//		String path = getPath(fileName);
//		File f = new File(path);
//		return f.exists();
//	}

	private static String getPath(String dataPath, Language language, FileName fileName) {
		return dataPath + "/" + language.getLanguage() + "/" + fileName.getFileName();
	}

}
