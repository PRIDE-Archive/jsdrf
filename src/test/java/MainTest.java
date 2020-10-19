import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.ebi.pride.sdrf.validate.model.ValidationError;
import uk.ac.ebi.pride.sdrf.validate.util.Constants;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Test
    void validateSampleFile() throws Exception {

        URL url = MainTest.class.getClassLoader().getResource("sdrf.txt");
        if (url == null) {
            throw new IllegalStateException("no file for input found!");
        }
        File sdrfFile = new File(url.toURI());

        Constants.Templates template = Enum.valueOf(Constants.Templates.class, templateName.trim().toUpperCase());
        List<ValidationError> errors = Main.validate(sdrfFile.getAbsolutePath(),template, verbose);
        Assertions.assertTrue(errors.size()==0);
    }

    /**
     * Run a bulk test with the sdrf files in the annotated projects in Github repository
     * @throws Exception
     */
    @Test
    void bulkValidation() throws Exception {

        final String repoURL = "https://raw.githubusercontent.com/bigbio/proteomics-metadata-standard/master/annotated-projects/";
        Set<String> accessions = new HashSet<>(Arrays.asList(
                "PXD000288",
                "PXD000312",
                "PXD000396",
                "PXD000527",
                "PXD000534",
                "PXD000547",
                "PXD000548",
                "PXD000561",
                "PXD000612",
                "PXD000651",
                "PXD000652",
                "PXD000759",
                "PXD000792",
                "PXD000793",
                "PXD000793",
                "PXD000815",
                "PXD001404",
                "PXD001474",
                "PXD001558",
                "PXD001725",
                "PXD001819",
                "PXD002049",
                "PXD002080",
                "PXD002081",
                "PXD002082",
                "PXD002084",
                "PXD002086",
                "PXD002087",
                "PXD002088",
                "PXD002171",
                "PXD002255",
                "PXD002395",
                "PXD002619",
                "PXD002870",
                "PXD003133",
                "PXD003430",
                "PXD003452",
                "PXD003515",
                "PXD003531",
                "PXD003615",
                "PXD003636",
                "PXD003668",
                "PXD003977",
                "PXD004132",
                "PXD004159",
                "PXD004242",
                "PXD004436",
                "PXD004452",
                "PXD004624",
                "PXD004682",
                "PXD004683",
                "PXD004684",
                "PXD004732",
                "PXD004987",
                "PXD005300",
                "PXD005354",
                "PXD005355",
                "PXD005366",
                "PXD005445",
                "PXD005463",
                "PXD005507",
                "PXD005819",
                "PXD005940",
                "PXD005942",
                "PXD005946",
                "PXD006003",
                "PXD006195",
                "PXD006401",
                "PXD006430",
                "PXD006482",
                "PXD006542",
                "PXD006607",
                "PXD006675",
                "PXD006675",
                "PXD006833",
                "PXD006877",
                "PXD006914",
                "PXD007160",
                "PXD007555",
                "PXD007662",
                "PXD008012",
                "PXD008222",
                "PXD008355",
                "PXD008722",
                "PXD008722",
                "PXD008840",
                "PXD008934",
                "PXD009203",
                "PXD009602",
                "PXD009623",
                "PXD009630",
                "PXD009815",
                "PXD010154",
                "PXD010595",
                "PXD010603",
                "PXD010698",
                "PXD010705",
                "PXD010981",
                "PXD011175",
                "PXD011799",
                "PXD011839",
                "PXD012203",
                "PXD012255",
                "PXD012277",
                "PXD012431",
                "PXD012593",
                "PXD012755",
                "PXD012923",
                "PXD013234",
                "PXD013523",
                "PXD013765",
                "PXD013868",
                "PXD013923",
                "PXD014525",
                "PXD014528",
                "PXD015578",
                "PXD015957",
                "PXD016002",
                "PXD016837",
                "PXD017201",
                "PXD017291",
                "PXD017710",
                "PXD018117",
                "PXD018241",
                "PXD018581",
                "PXD018594",
                "PXD018678",
                "PXD018682",
                "PXD018970",
                "PXD019113"
        ));

        for (String accession:accessions) {
            String stringURL = repoURL + accession + "/sdrf.tsv";
            File sdrfFile = null;
            URL url;
            try {
                url = new URL(stringURL);
                sdrfFile = File.createTempFile(accession + "_sdrf", ".tsv");
                FileUtils.copyURLToFile(url, sdrfFile);
                log.info(sdrfFile.getAbsolutePath());

                Constants.Templates template = Enum.valueOf(Constants.Templates.class, templateName.trim().toUpperCase());
                List<ValidationError> errors = Main.validate(sdrfFile.getAbsolutePath(),template, verbose);
                Assertions.assertTrue(errors.size()==0);
                Thread.sleep(1000); // to avoid too many request to OLS
            } catch (Exception e) {
                log.error("Error reading SDRF file from --" + stringURL + " "+ e.getMessage());
            }finally {;
                sdrfFile.deleteOnExit();
            }
        }
    }
}