package biorimp.main;

import edu.wayne.cs.severe.redress2.main.MainMetrics;
import edu.wayne.cs.severe.redress2.main.MainPredFormulasBIoRIPM;

/**
 * Created by Daniel on 4/10/17.
 */
public class MainParserAndPredictMetrics {
    private static String systems = "JF_DATASET-1";
    private static String userPath = System.getProperty("user.dir");
    private static String project_path = "/resources/systems/";
    // -r = NO parse code
    private static String[] args = {"-l", "Java", "-p", userPath + project_path + systems + "/src", "-s", "     " + systems + "      "};


    public static void  main(String[] args){


        measureMetrics();

    }

    private static void measureMetrics() {
        // First Step: Calculate Actual Metrics
        MainMetrics init = new MainMetrics();
        init.main(args);

    }
    private static  void parseSystem(){
        MainPredFormulasBIoRIPM init = new MainPredFormulasBIoRIPM();
        init.main(args);
    }
}
