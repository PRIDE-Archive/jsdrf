package uk.ac.ebi.pride.sdrf.validate.validation;

import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.template.*;

import java.util.List;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.*;

/**
 * @author Suresh Hewapathirana
 */
public class Validator {

    Template template;

    /**
     * Create the appropriate template object according to the template provided by the user
     *
     * @param templateName
     */
    public Validator(Templates templateName, SDRFContent sdrfContent) {

        switch (templateName){
            case HUMAN:
                template = new Template(new HumanTemplate(sdrfContent));
                break;
            case VERTEBRATES:
                template = new Template(new VertebratesTemplate(sdrfContent));
                break;
            case PLANTS:
                template = new Template(new PlantsTemplate(sdrfContent));
                break;
            case NON_VERTEBRATES:
                template = new Template(new NonVertebratesTemplate(sdrfContent));
                break;
            case CELL_LINES:
                template = new Template(new CellLinesTemplate(sdrfContent));
                break;
            case MASS_SPECTROMETRY:
                template = new Template(new MassSpectrometryTemplate(sdrfContent));
                break;
            default:
                template = new Template(new DefaultTemplate(sdrfContent));
        }
    }

    /**
     * Validate a corresponding SDRF
     */
    public List<ValidationError> validate(){
        return template.validate();
    }
}
