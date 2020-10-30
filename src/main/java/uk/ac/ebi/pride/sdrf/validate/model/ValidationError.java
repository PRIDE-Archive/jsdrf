package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.*;

/**
 * Represents a difference between the schema and data frame, found during the validation of the data
 *
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class ValidationError {
    // error message
    String message;
    // The value of the failing cell
    String value;
    // The row indexes (usually an integer starting from 0) of the cell that failed the validation
//    Set<Integer> rows;
    int row;
    // The column name of the cell that failed the validation
    String column;
    // error type (warning, error, info etc)
    Logging errorType;

    @Override
    public String toString() {
        return errorType + " : " + message + '\'' +
                ", value='" + value + '\'' +
                ", row=" + row +
                ", column='" + column + '\'';
    }
}
