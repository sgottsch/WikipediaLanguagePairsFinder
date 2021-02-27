package de.l3s.wikipedia_language_pair_finder.util;

import de.l3s.wikipedia_language_pair_finder.model.Language;

public enum FileName {

	// Wikipedia
	WIKIPEDIA_REDIRECTS("redirects.sql.gz", FileType.RAW_DATA, false, false, true),
	WIKIPEDIA_PAGE_INFOS("page.sql.gz", FileType.RAW_DATA, false, false, true),
	WIKIPEDIA_LANG_LINKS("langlinks.sql.gz", FileType.RAW_DATA, false, false, true);

	// in case of a folder, fileName are the files' prefixes
	private String fileName;
	private FileType fileType;
	private boolean isFolder;
	private boolean hasColumnNamesInFirstLine;

	private boolean isGZipped;

	// FileName(String fileName, Source source, boolean isRawData, boolean
	// isFolder, boolean hasColumnNamesInFirstLine) {
	// this.source = source;
	// this.fileName = fileName;
	// this.isRawData = isRawData;
	// this.isFolder = false;
	// }

	FileName(String fileName, FileType fileType, boolean isFolder, boolean hasColumnNamesInFirstLine,
			boolean isGZipped) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.isFolder = isFolder;
		this.isGZipped = isGZipped;
		this.hasColumnNamesInFirstLine = hasColumnNamesInFirstLine;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileName(Language language) {
		return fileName.replace("$lang$", language.getLanguageLowerCase());
	}

	public boolean isRawData() {
		return this.fileType == FileType.RAW_DATA;
	}

	public boolean isResultsData() {
		return this.fileType == FileType.RESULTS;
	}

	public boolean isMetaData() {
		return this.fileType == FileType.META;
	}

	public boolean isOutputData() {
		return this.fileType == FileType.OUTPUT;
	}

	public boolean isOutputPreviewData() {
		return this.fileType == FileType.OUTPUT_PREVIEW;
	}

	public boolean isPreviousVersionData() {
		return this.fileType == FileType.PREVIOUS_VERSION;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public boolean hasColumnNamesInFirstLine() {
		return hasColumnNamesInFirstLine;
	}

	public boolean isGZipped() {
		return isGZipped;
	}

	private enum FileType {
		RAW_DATA, RESULTS, META, OUTPUT, OUTPUT_PREVIEW, PREVIOUS_VERSION;
	}

}