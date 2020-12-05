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
public class CellLinesTemplate extends DefaultTemplate{

    public CellLinesTemplate(SDRFContent sdrfContent) {
        super(sdrfContent);
        this.sdrfSchemaColumnsSize = 9;
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[cell type]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[cultured cell]",  true, false, Ontology.NONE));
    }

    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errors  = super.validate();
        System.out.println("Cell Line template validation running...");
        errors.addAll(SDRFValidator.validate(this.sdrfContent, this.sdrfSchemaColumns, this.sdrfSchemaColumnsSize));
        return errors;
    }
}
