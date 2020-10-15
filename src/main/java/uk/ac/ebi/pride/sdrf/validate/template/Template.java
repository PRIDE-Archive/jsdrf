package uk.ac.ebi.pride.sdrf.validate.template;

import uk.ac.ebi.pride.sdrf.validate.model.Validation;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
public class Template {

    private Validation validation;

    public Template(Validation validation) {
        this.validation = validation;
    }

    public List<ValidationError> validate(){
        return validation.validate();
    }
}
