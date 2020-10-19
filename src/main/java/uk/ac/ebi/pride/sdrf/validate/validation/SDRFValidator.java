package uk.ac.ebi.pride.sdrf.validate.validation;

import uk.ac.ebi.pride.sdrf.validate.model.*;
import uk.ac.ebi.pride.sdrf.validate.util.Constants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Suresh Hewapathirana
 */
public class SDRFValidator {

    final static List<String> specialColumns = Arrays.asList("sourcename", "assayname", "materialtype","description");
    final static String columnNamePattern = "^(characteristics|comment|factor value)\\s*\\[([^\\]]+)\\](?:\\.\\d+)?$";

    public static List<ValidationError> validate(SDRFContent sdrfContent, List<SDRFColumnSchema> sdrfSchemaColumns) {

        List<ValidationError> errors = new ArrayList<>();

        // Check minimum number of columns
        if (!SDRFValidator.isMinimumColumnsExists(sdrfContent, sdrfSchemaColumns)) {
            String errorMessage = String.format("The number of columns in the SDRF ({}) is smaller than the number of mandatory fields ({})",
                    sdrfContent.getSdrfColumns().size(), sdrfSchemaColumns.size());
            errors.add(new ValidationError(errorMessage, "", 0, "N/A", Constants.Logging.ERROR));
        }

        // Check the mandatory fields
        ValidationError validationError = SDRFValidator.validateMandatoryColumns(sdrfContent, sdrfSchemaColumns);
        if (validationError != null) {
            errors.add(validationError);
        }

        // validate all the columns against the schema
        List<ValidationError> validationErrors = SDRFValidator.validateColumns(sdrfContent, sdrfSchemaColumns);
        if (validationErrors.size() > 0) {
            errors.addAll(validationErrors);
        }

        // validate column names
        ValidationError columnNamesvalidationError = SDRFValidator.validateColumnNames(sdrfContent);
        if (columnNamesvalidationError != null) {
            errors.add(columnNamesvalidationError);
        }

        // check for any recommendations which are missing in the SDRF
        List<ValidationError> validationWarnings = SDRFValidator.checkRecommendations();
        if (validationErrors.size() > 0) {
            errors.addAll(validationErrors);
        }

        System.out.println("Default template validation completed");
        return errors;
    }

    /**
     * Validate column names
     *
     * @return
     */
    public static ValidationError validateColumnNames(SDRFContent sdrfContent) {

        List<String> spaceErrors = new ArrayList<>();
        List<String> cNameErrors = new ArrayList<>();

        for (SDRFColumn cname : sdrfContent.getSdrfColumns()) {
            if(!cname.getName().equals(cname.getName().trim())) {
                spaceErrors.add(cname.getName());
                continue;
            }
            if (specialColumns.contains(cname.getName().replace(" ", ""))) {
                continue;
            }
            Pattern pattern = Pattern.compile(columnNamePattern);
            Matcher matcher = pattern.matcher(cname.getName());
            if (!matcher.find()) {
                cNameErrors.add(cname.getName());
            }
        }
        if (spaceErrors.isEmpty() && cNameErrors.isEmpty()) {
            return null;
        } else {
            String errorMessage =
                    String.format("Invalid columns present: %s", String.join(", ", cNameErrors)) + " " +
                            String.format(" %s", String.join(" (leading or trailing whitespace) , ", spaceErrors));
            errorMessage = (errorMessage.endsWith(","))? errorMessage.substring(0, errorMessage.length() - 1):errorMessage;
            return new ValidationError(errorMessage, "", 0, String.join(", ", spaceErrors) + " " + String.join(", ", cNameErrors), Constants.Logging.ERROR);
        }
    }

    /**
     * Validate all the columns according to the schema
     *
     * @return
     */
    public static List<ValidationError> validateColumns(SDRFContent sdrfContent, List<SDRFColumnSchema> sdrfSchemaColumns){
        List<ValidationError> validationErrors = new ArrayList<>();
        Set<String> validatedOntologyTerms = new HashSet<>();

        for(SDRFRow row :sdrfContent.getSdrfRows()){
            for (int col=0; col<row.getRow().size(); col++){
                String cellValue = row.getRow().get(col);
                if(cellValue != null && cellValue.length() > 0){
                    if(SDRFValidator.leadingWhitespaceValidation(cellValue) || SDRFValidator.trailingWhitespaceValidation(cellValue)) {
                        validationErrors.add(new ValidationError("leading or trailing whitespace present", cellValue, row.getRowNo(),"", Constants.Logging.ERROR));
                    }
                    String colName = sdrfContent.getSdrfColumns().get(col).getName();
                    Optional<SDRFColumnSchema> schema = sdrfSchemaColumns.stream().filter(sdrfColumnSchema -> sdrfColumnSchema.getName().equals(colName)).findFirst();
                    if(schema.isPresent() && schema.get().getOntology() != Constants.Ontology.NONE){
                        if(!validatedOntologyTerms.contains(cellValue)){
                            if(!OntologyTerm.validate(cellValue, schema.get().getOntology())){ // if not valid
                                validationErrors.add(new ValidationError("Value is not match with ontology", cellValue, row.getRowNo(),colName, Constants.Logging.ERROR));
                            }
                            validatedOntologyTerms.add(cellValue);
                        }
                    }

                }
            }
        }
        return validationErrors;
    }

    /**
     * Check if all the mandatory columns presents in the SDRF according to the template schema
     *
     * @return ValidationError
     */
    public static ValidationError validateMandatoryColumns(SDRFContent sdrfContent, List<SDRFColumnSchema> sdrfSchemaColumns) {

        List<String> missingColumns = new ArrayList<>();

        for(SDRFColumnSchema sdrfColumnSchema: sdrfSchemaColumns){
            List<String> columnNames = sdrfContent.getSdrfColumns()
                    .stream()
                    .map(SDRFColumn::getName)
                    .collect(Collectors.toList());
            if(!sdrfColumnSchema.isOptional()){
                if(!columnNames.contains(sdrfColumnSchema.getName())){
                    missingColumns.add(sdrfColumnSchema.getName());
                }
            }
        }
        if(missingColumns.size() > 0) {
            String errorMessage = String.format("The following columns are mandatory and not present in the SDRF: %s",
                    String.join(", ", missingColumns));
            return new ValidationError(errorMessage, "", 0, "N/A", Constants.Logging.ERROR);
        }else{
            return null;
        }
    }

    /**
     * Check if the SDRF has the minimal columns defined in the schema
     * @return
     */
    public static boolean isMinimumColumnsExists(SDRFContent sdrfContent, List<SDRFColumnSchema> sdrfSchemaColumns){
        return sdrfContent.getSdrfColumns().size() >= sdrfSchemaColumns.size();
    }

    /**
     * checks for any recommendations which are missing in the SDRF
     *
     * @return
     */
    public static List<ValidationError> checkRecommendations(){
        // todo
        return new ArrayList<>();
    }

    /**
     * Check if there are spaces in the start of the String value
     * @param value
     * @return
     */
    public static boolean leadingWhitespaceValidation(String value){
        return value.startsWith(" ");
    }

    /**
     * Check if there are spaces in the end of the String value
     *
     * @param value
     * @return
     */
    public static boolean trailingWhitespaceValidation(String value){
        return value.endsWith(" ");
    }
}
