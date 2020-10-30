package uk.ac.ebi.pride.sdrf.validate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class SDRFContent {

    List<SDRFColumn> sdrfColumns;
    List<SDRFRow> sdrfRows;

    /**
     * Get data of a column by the column name
     *
     * @param colName
     * @return
     */
    public List<String> getColumnDataByName(String colName){
        List<String> columnData = new ArrayList<>();
        int columnIndex = -1;

        for(int i=0; i<sdrfColumns.size();i++){
            if(sdrfColumns.get(i).getName().toLowerCase().equals(colName.toLowerCase())){
                columnIndex = i;
                break;
            }
        }
        if(columnIndex != -1) {
            for (SDRFRow sdrfRow : sdrfRows) {
                columnData.add(sdrfRow.getRow().get(columnIndex));
            }
        }

        return columnData;
    }
}
