package uk.ac.ebi.pride.sdrf.validate;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.validation.SDRFParser;
import uk.ac.ebi.pride.sdrf.validate.validation.Validator;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfig;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.*;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
@Command
public class Main implements Runnable{

    @Option(names = {"-s", "--sdrf"}, description ="SDRF file to be validated", required = true)
    private String sdrfFile;

    @Option(names = {"-t", "--template"}, description ="Select the template that will be use to validate the file \n" +
            "        CELL_LINES,\n" +
            "        DEFAULT(default),\n" +
            "        HUMAN,\n" +
            "        MASS_SPECTROMETRY,\n" +
            "        NON_VERTEBRATES,\n" +
            "        PLANTS,\n" +
            "        VERTEBRATES")
    private String templateName = "DEFAULT";

    @Option(names = {"-v", "--verbose"}, description ="Print errors")
    private boolean verbose = false;


    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display a help message")
    private boolean helpRequested = false;

    public static void main(String[] args) throws Exception {

        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            Set<ValidationError> errors = validate(sdrfFile, verbose);
           // provide some info to the user, as no info is confusing
            if(errors == null || errors.size()==0) {
                System.out.println("Everything seems to be fine. Well done.");
            }else {
                System.out.println("There were validation errors.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will automatically detect the template by checkings the values of SDRF
     * @param sdrfFile SDRF file full path
     * @param verbose boolean value to print errors or not
     * @return Set of errors, empty list if no errors
     * @throws Exception
     */
    public static Set<ValidationError> validate(String sdrfFile, boolean verbose) throws Exception {

        Set<ValidationError> errors = new HashSet<>();

        try {
            // parse file
            SDRFParser sdrfParser = new SDRFParser();
            SDRFContent sdrfContent = sdrfParser.getSDRFContent(sdrfFile);
            Validator validator = new Validator(Templates.DEFAULT, sdrfContent);
            List<ValidationError> errorsInDefault = validator.validate();
            errors.addAll(errorsInDefault);
            if(errorsInDefault.size()==0){
                List<Templates> templates = getTemplate(sdrfContent);
                if(templates.size()>0){
                    for (Templates templatesFound:templates) {
                        validator = new Validator(templatesFound, sdrfContent);
                        List<ValidationError> errorsIntemplate= validator.validate();
                        if(errorsIntemplate.size()>0) {
                            errors.addAll(errorsIntemplate);
                        }
                    }
                }
                validator = new Validator(Templates.MASS_SPECTROMETRY, sdrfContent);
                List<ValidationError> errorsInMS= validator.validate();
                if(errorsInMS.size()>0) {
                    errors.addAll(errorsInMS);
                }
            }
            if(verbose){
                for (ValidationError validationError : errors){
                    log.info(validationError.toString());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return errors;
    }

    /**
     * Validates the SDRF file based on the template and returns the errors(if any)
     *
     * @param sdrfFile SDRF file full path
     * @param template Template
     * @return List of errors, empty list if no errors
     */
    public static List<ValidationError> validate(String sdrfFile, Templates template, boolean verbose) throws Exception {

        List<ValidationError> errors;

        try {
            // parse file
            SDRFParser sdrfParser = new SDRFParser();
            SDRFContent sdrfContent = sdrfParser.getSDRFContent(sdrfFile);

            // validation
            Validator validator = new Validator(template, sdrfContent);
            errors = validator.validate();
            if(verbose){
                for (ValidationError validationError : errors){
                    log.info(validationError.toString());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return errors;
    }

    /**
     * Extract organism information and pick a template for validation
     *
     * @return
     */
    private  static List<Templates> getTemplate(SDRFContent sdrfContent){

        OLSClient olsClient = new OLSClient(new OLSWsConfig());
        List<Templates> templates = new ArrayList<>();
        String cell = "characteristics[cultured cell]";
        String organism = "characteristics[organism]";

        List<String> columnData = sdrfContent.getColumnDataByName(cell);

        for(String value: columnData){
            if(!(value.equals(NOT_AVAILABLE) || value.equals(NOT_APPLICABLE))){
                templates.add(Templates.CELL_LINES);
                break;
            }
        }

        Set<String> organisms = new HashSet<>();
        for (String s : sdrfContent.getColumnDataByName(organism)) {
            organisms.add(s);
        }

        for(String org: organisms){
            if (org.equals("homo sapiens")){
                templates.add(Templates.HUMAN);
            }else{
               Term hit = olsClient.getExactTermByName(org, "ncbitaxon");
               if(hit != null){
                   List<Term> ancestors = olsClient.getTermParents(hit.getIri(),"ncbitaxon",100000);
                   if (ancestors == null || ancestors.size()==0) {
                       System.out.println("Could not get ancestors!");
                   }
                   Set<String> labels = new HashSet<>();
                   for (Term ancestor: ancestors) {
                       labels.add(ancestor.getLabel());
                   }
                   if (labels.contains("Gnathostomata <vertebrates>")) {
                       templates.add(Templates.VERTEBRATES);
                   }else if(labels.contains("Metazoa")) {
                       templates.add(Templates.NON_VERTEBRATES);
                   }else if (labels.contains("Viridiplantae")){
                       templates.add(Templates.PLANTS);
                    }
               }
            }

        }
        return templates;
    }
}

