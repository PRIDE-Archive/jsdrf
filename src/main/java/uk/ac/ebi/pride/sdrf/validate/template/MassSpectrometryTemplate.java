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
public class MassSpectrometryTemplate extends DefaultTemplate {

    public MassSpectrometryTemplate(SDRFContent sdrfContent) {
        super(sdrfContent);
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "assay name", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[fraction identifier]", true, false, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[label]", true, false, Ontology.PRIDE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[technical replicate]", true, true, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[instrument]", true, false, Ontology.MS_OPT));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[modification parameters]", true, true, Ontology.UNIMOD));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[cleavage agent details]", true, false, Ontology.MS));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[fragment mass tolerance]", true, true, Ontology.NONE));
        this.sdrfSchemaColumns.add(new SDRFColumnSchema( "comment[precursor mass tolerance]", true, true, Ontology.NONE));
    }

    @Override
    public List<ValidationError> validate() {

        System.out.println("Mass Spectrometry template validation running...");
        List<ValidationError> errors = new ArrayList<>(SDRFValidator.validate(this.sdrfContent, this.sdrfSchemaColumns));
        return errors;
    }
}
