package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class SDRFRow {

    int rowNo;
    List<String> row;
}
