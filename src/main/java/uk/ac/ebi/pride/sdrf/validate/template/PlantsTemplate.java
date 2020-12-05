package uk.ac.ebi.pride.sdrf.validate.template;

import uk.ac.ebi.pride.sdrf.validate.model.SDRFColumnSchema;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.util.Constants.*;
import uk.ac.ebi.pride.sdrf.validate.validation.SDRFValidator;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
public class PlantsTemplate extends DefaultTemplate{

    public PlantsTemplate(SDRFContent sdrfContent) {
        super(sdrfContent);
        this.sdrfSchemaColumnsSize = 8;
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[developmental stage]",  true, true, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[strain/breed]",  true, true, Ontology.NONE));
    }

    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errors  = super.validate();
        System.out.println("plants template validation running...");
        errors.addAll(SDRFValidator.validate(this.sdrfContent, this.sdrfSchemaColumns, this.sdrfSchemaColumnsSize));
        return errors;
    }
}
