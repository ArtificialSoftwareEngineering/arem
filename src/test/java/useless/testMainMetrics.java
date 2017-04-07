package useless;

import edu.wayne.cs.severe.redress2.main.MainMetrics;

public class testMainMetrics {

    public static void main(String[] argsS) {
        //First Test bith evolution library
        // TODO Auto-generated method stub
        String userPath = System.getProperty("user.dir");
        //String[] args = {"-l", "Java", "-p", userPath + "\\test_data\\code\\evolutionaryagent\\src", "-s", "     evolutionaryagent      "};
        String systems = "JF_DATASET-26";
        String[] args = {"-l", "Java", "-p", userPath + "\\resources\\systems\\" + systems + "\\src", "-s", "     " + systems + "      "};
        MainMetrics.main(args);
    }

}
