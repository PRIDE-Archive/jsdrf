# jsdrf

![Java CI with Maven](https://github.com/bigbio/jsdrf/workflows/Java%20CI%20with%20Maven/badge.svg)


Java library to validate SDRF file formats. The [SDRF](https://github.com/bigbio/proteomics-metadata-standard) file format represent the sample to data information in proteomics experiments.

## features

- Java data model to include in java application SDRF.
- Validation of sdrf files with proteomics rules.


## How to use it:

```bash

java -jar jdsrf-{X.X.X}.jar --sdrf query_file.tsv --template HUMAN

```

Using the Java library with maven:

```java

<dependency>
     <groupId>uk.ac.ebi.pride.sdrf</groupId>
     <artifactId>jsdrf</artifactId>
     <version>{version}</version>
</dependency>

```

## How to cite

Perez-Riverol, Yasset, European Bioinformatics Community for Mass Spectrometry. "Towards a sample metadata standard in public proteomics repositories." Journal of Proteome Research (2020) [Manuscript](https://pubs.acs.org/doi/abs/10.1021/acs.jproteome.0c00376).

Dai, C., Füllgrabe, A., Pfeuffer, J. et al. A proteomics sample metadata representation for multiomics integration and big data analysis. Nat Commun 12, 5854 (2021). https://doi.org/10.1038/s41467-021-26111-3 [Manuscript](https://www.nature.com/articles/s41467-021-26111-3.pdf)

