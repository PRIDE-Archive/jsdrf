import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.ebi.pride.sdrf.validate.Main;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.util.Constants;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
class MainTest {

    String templateName = "default";
    boolean verbose = true;

    /**
     * Validate example sdrf file in the resource folder
     *
     * @throws Exception
     */
    @Ignore
    @Test
    void validateSampleFile() throws Exception {

        URL url = MainTest.class.getClassLoader().getResource("sdrf.txt");
        if (url == null) {
            throw new IllegalStateException("no file for input found!");
        }
        File sdrfFile = new File(url.toURI());

        Constants.Templates template = Enum.valueOf(Constants.Templates.class, templateName.trim().toUpperCase());
        List<ValidationError> errors = Main.validate(sdrfFile.getAbsolutePath(),template, verbose);
        Assertions.assertEquals(1, errors.size());
    }
}