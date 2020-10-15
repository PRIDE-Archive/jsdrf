package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.Data;
import static uk.ac.ebi.pride.sdrf.validate.util.Constants.*;

/**
 * @author Suresh Hewapathirana
 */
@Data
public class SDRFColumnSchema extends Column {
    boolean isEmptyAllowed;
    boolean isOptional;
    Ontology ontology;

    public SDRFColumnSchema(String name, boolean isEmptyAllowed,  boolean isOptional, Ontology ontology) {
        super(name);
        this.isEmptyAllowed = isEmptyAllowed;
        this.isOptional = isOptional;
        this.ontology = ontology;
    }
}
