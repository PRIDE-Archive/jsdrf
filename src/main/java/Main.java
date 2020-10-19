import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import uk.ac.ebi.pride.sdrf.validate.model.SDRFContent;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.validation.SDRFParser;
import uk.ac.ebi.pride.sdrf.validate.validation.Validator;

import java.util.List;

import static uk.ac.ebi.pride.sdrf.validate.util.Constants.Templates;

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

        Templates template = Enum.valueOf(Templates.class, templateName.trim().toUpperCase());
        try {
            List<ValidationError> errors = validate(sdrfFile,template, verbose);
           // provide some info to the user, as no info is confusing
            if(errors.size()==0) {
                System.out.println("Everything seems to be fine. Well done.");
            }else {
                System.out.println("There were validation errors.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}

