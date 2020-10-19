package uk.ac.ebi.pride.sdrf.validate.validation;

import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfig;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.*;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.NOT_APPLICABLE;
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

        String ontologyTerms = null;
        OLSClient olsClient = new OLSClient(new OLSWsConfig());
        List<String> valuesFoundFromSearch = new ArrayList<>();
        List<Term> termsFound = new ArrayList<>();

        Map<String,String> terms = ontologyTermParser(cellValue);
        for (Map.Entry<String, String> term : terms.entrySet()) {
            if(!term.getKey().equals(TERM_NAME)){
                ontologyTerms = null;
            }else{
                if(ontology != null){
                    termsFound = olsClient.searchTermById(term.getValue(), ontology.getName());
                 }else{
                    termsFound = olsClient.searchTermById(term.getValue(), null);
                }
            }
            for (Term t: termsFound) {
                valuesFoundFromSearch.add(t.getName().toLowerCase());
            }
        }
        if(ontology.isNotApplicable()) {
            valuesFoundFromSearch.add(NOT_APPLICABLE);
        }
        return validate_ontology_terms(cellValue, valuesFoundFromSearch);
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
     * @param valuesFoundFromSearch list of values Found From OLS Search
     * @return
     */
    static boolean validate_ontology_terms(String cellValue, List<String> valuesFoundFromSearch){
        boolean isvalid = true;
        cellValue = cellValue.toLowerCase();
        Map<String,String> terms = ontologyTermParser(cellValue);
        for(Map.Entry<String,String> term : terms.entrySet()){
            if(!valuesFoundFromSearch.contains(term.getValue())){
                isvalid = false;
                break;
            }
        }
        return isvalid;
    }
}
