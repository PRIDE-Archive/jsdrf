package uk.ac.ebi.pride.sdrf.validate.template;

import uk.ac.ebi.pride.sdrf.validate.model.SDRFColumnSchema;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.Validation;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.util.Constants.Ontology;
import uk.ac.ebi.pride.sdrf.validate.validation.SDRFValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
public class DefaultTemplate implements Validation {

    SDRFContent sdrfContent;
    final List<SDRFColumnSchema> sdrfSchemaColumns = new ArrayList<>();

    public DefaultTemplate(SDRFContent sdrfContent) {
        this.sdrfContent = sdrfContent;
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("source name",true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[organism part]",true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[disease]",true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[organism]",false, false, Ontology.NCBI_TAXON));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("characteristics[cell type]", false, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("assay name",false, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("comment[fraction identifier]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema("comment[data file]",true, false, Ontology.NONE));
    }

    @Override
    public List<ValidationError> validate() {
        System.out.println("Default template validation running...");
        return SDRFValidator.validate(this.sdrfContent, this.sdrfSchemaColumns);
    }
}
