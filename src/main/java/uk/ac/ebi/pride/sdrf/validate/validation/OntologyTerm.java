package uk.ac.ebi.pride.sdrf.validate.validation;

import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfig;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.*;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.Ontology;
import static uk.ac.ebi.pride.sdrf.validate.util.Constants.TERM_NAME;

/**
 * @author Suresh Hewapathirana
 */
public class OntologyTerm {

    String OntologyName;
    boolean notAvailable;
    boolean notApplicable;

    /**
     *  Validate if the term is present in the provided ontology. This method looks in the provided
     *         ontology _ontology_name
     * @return
     */
    public static boolean validate(String cellValue, Ontology ontology){
        OLSClient olsClient = new OLSClient(new OLSWsConfig());
        List<String> labels = new ArrayList<>();
        boolean isValid = true;

        Map<String,String> terms = ontologyTermParser(cellValue);
        for (Map.Entry<String, String> entry : terms.entrySet()) {
            if(ontology != null && !entry.getKey().equals(TERM_NAME)){
               List<Term> termsFounds = olsClient.searchTermById(entry.getValue(), ontology.getName());
               if(termsFounds.size() == 0){
                   isValid = false;
               }
            }
        }
        return isValid;
    }

    /**
     * Parse a line string and convert it into a dictionary {key -> value}
     * @param cellValue String line
     * @return
     */
    static Map<String,String> ontologyTermParser(String cellValue){

        Map<String,String> term = new HashMap<>();
        List<String> values = Arrays.asList(cellValue.split(";"));
        if (values.size() ==1 && !values.get(0).contains("=")){
            term.put(TERM_NAME, values.get(0).toLowerCase());
        } else {
            for (String name : values){
            String[] valueTerms = name.split("=");
            term.put(valueTerms[0].trim().toUpperCase(), valueTerms[1].trim().toLowerCase());
            }
        }
        return term;
    }

    /**
     * Check if a cell value is in a list of labels or list of strings
     *
     * @param cellValue String line in cell
     * @param labels list of labels
     * @return
     */
    boolean validate_ontology_terms(String cellValue, List<String> labels){
        cellValue = cellValue.toLowerCase();
        Map<String,String> term = ontologyTermParser(cellValue);
        return labels.contains(term.get(TERM_NAME));
    }

}
