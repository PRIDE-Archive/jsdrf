package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Creates a new Column object
 *
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
abstract class Column {
    String name;
}
