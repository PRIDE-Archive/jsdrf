package uk.ac.ebi.pride.sdrf.validate.validation;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFColumn;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFRow;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Suresh Hewapathirana
 */
public class SDRFParser {

    /**
     * Convert the parsed data to an object
     *
     * @param sdrfFile
     * @return
     * @throws Exception
     */
    public SDRFContent getSDRFContent(String sdrfFile) throws Exception {
        List<String[]> rows = parse(sdrfFile);

        List<SDRFColumn> sdrfColumns = new ArrayList<>();
        for (String columnName:rows.get(0)) {
            sdrfColumns.add(new SDRFColumn(columnName.toLowerCase()));
        }

        List<SDRFRow> sdrfRows = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++){
            sdrfRows.add(new SDRFRow(i,
                    Arrays.asList(rows.get(i))
                    .stream()
                    .map(s ->(!StringUtils.isEmpty(s))?s.toLowerCase():s)
                    .collect(Collectors.toList()))
            );
        }

        return new SDRFContent(sdrfColumns, sdrfRows);
    }

    /**
     * Parsing the sdrf file
     *
     * @param sdrfFile
     * @return
     * @throws Exception
     */
    private List<String[]> parse(String sdrfFile) throws Exception {
        List<String[]> parsedRows;
        try (Reader inputReader = new InputStreamReader(new FileInputStream(new File(sdrfFile)), "UTF-8")) {
            TsvParser parser = new TsvParser(new TsvParserSettings());
            parsedRows = parser.parseAll(inputReader);
        } catch (IOException e) {
            throw new Exception("SDRF file cannot be parsed");
        }
        return parsedRows;
    }
}
