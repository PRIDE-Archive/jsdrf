package uk.ac.ebi.pride.sdrf.validate.template;

import uk.ac.ebi.pride.sdrf.validate.model.SDRFColumnSchema;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.util.Constants.*;
import uk.ac.ebi.pride.sdrf.validate.validation.SDRFValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
public class HumanTemplate extends DefaultTemplate {

    public HumanTemplate(SDRFContent sdrfContent) {
        super(sdrfContent);
        this.sdrfSchemaColumnsSize = 11;
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[ancestry category]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[age]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[sex]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[developmental stage]", true, true, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[individual]", true, true, Ontology.NONE));
    }

    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errors  = super.validate();
        System.out.println("Human template validation running...");
        errors.addAll(SDRFValidator.validate(this.sdrfContent, this.sdrfSchemaColumns, this.sdrfSchemaColumnsSize));
        return errors;
    }
}
