package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class SDRFContent {

    List<SDRFColumn> sdrfColumns;
    List<SDRFRow> sdrfRows;
}
