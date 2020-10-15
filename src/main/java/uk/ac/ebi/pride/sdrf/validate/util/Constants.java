package uk.ac.ebi.pride.sdrf.validate.util;

/**
 * NOTE: Keep ontology in an alphabetical order
 *
 * @author Suresh Hewapathirana
 */
public class Constants {

    public enum Templates {
        CELL_LINES,
        DEFAULT,
        HUMAN,
        MASS_SPECTROMETRY,
        NON_VERTEBRATES,
        PLANTS,
        VERTEBRATES;
    }

    public enum Logging {
        DEBUG,
        ERROR,
        FATAL,
        INFO,
        WARNING;
    }

    public enum Ontology {
        NONE("none", true),
        NCBI_TAXON("ncbitaxon", true),
        MS("ms", true),
        MS_OPT("ms", false),
        PRIDE("pride", false),
        UNIMOD("unimod", true);

        String name;
        boolean isNotApplicable;

        Ontology(String name, boolean isNotApplicable) {
            this.name = name;
            this.isNotApplicable = isNotApplicable;
        }

        public String getName() {
            return name;
        }

        public boolean isNotApplicable() {
            return isNotApplicable;
        }
    }

    public static final String TERM_NAME = "NM";
    public static final String NOT_AVAILABLE = "not available";
    public static final String NOT_APPLICABLE = "not applicable";
}
