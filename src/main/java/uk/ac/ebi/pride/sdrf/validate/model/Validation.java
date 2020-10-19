package uk.ac.ebi.pride.sdrf.validate.model;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
public interface Validation {

    List<ValidationError> validate();
}
