//package model;
//
//import org.junit.jupiter.api.Test;
//import validation.SDRFParser;
//
//import java.io.File;
//import java.net.URL;
//import java.util.List;
//
///**
// * @author Suresh Hewapathirana
// */
//class SDRFParserTest {
//
//    SDRFParser sdrfParser = new SDRFParser();
//
//    @Test
//    void parse() throws Exception {
//
//        URL url = SDRFParserTest.class.getClassLoader().getResource("sdrf.txt");
//        if (url == null) {
//            throw new IllegalStateException("no file for input found!");
//        }
//        File sdrf_file = new File(url.toURI());
//        List<String[]> content = sdrfParser.parse(sdrf_file.getAbsolutePath());
//        System.out.println(content.size());
//    }
//}