# Wikipedia Language Pairs Collector

This Java projects downloads Wikipedia dump files and processes them to create a compact file of Wikipedia article titles and redirects in a given set of languages.

## Running

Simply run the class LanguagePairsFinder, e.g., as follows:

```
java -jar LanguagePairsFinder.jar data_folder 20201020 langlinks.tsv en,fr,ru,de
```

Parameters:
1. Path to the data folder
2. Date of the dump (see [here](https://dumps.wikimedia.org/enwiki/))
3. Name of the output file
4. Comma-separated list of languages

## Example Output

Articles that are only available in one language are not added to the file!

```
en	fr	ru	de	en_redirects	fr_redirects	ru_redirects	de_redirects
Musa_(genus)	Bananier	Банан_(род)	Bananen	Cold_hardy_bananas Callimusa Musa_spp. Ingentimusa Musa_sect._Musa Musa_(Musaceae) Australimusa Musa_sect._Callimusa	Figuier_d\'Adam Bananière Musa_(genre) Musa_(plante) Bacove	Банан_(растение) Musa	Musa_(Pflanzengattung)
```
