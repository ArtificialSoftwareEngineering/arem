import biorimp.optmodel.fitness.FitnessQualityDB;
import biorimp.optmodel.fitness.FitnessQualityDBScala;
import biorimp.optmodel.fitness.RefactorArrayPlainWrite;
import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.operators.RefOperClassTransposition;
import biorimp.optmodel.operators.RefOperMutation;
import biorimp.optmodel.operators.RefOperXOver;
import biorimp.optmodel.space.RefactoringOperationSpace;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.main.MainPredFormulasBIoRIPM;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.evolution.haea.*;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.search.Goal;
import unalcol.search.Solution;
import unalcol.search.population.PopulationSolution;
import unalcol.search.population.PopulationSolutionDescriptors;
import unalcol.search.population.variation.ArityTwo;
import unalcol.search.population.variation.Operator;
import unalcol.search.selection.Tournament;
import unalcol.search.space.ArityOne;
import unalcol.search.space.Space;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainHAEAFIX {

    private static String SYS = "JF_DATASET-32";

    //@Test
    public static void refactorHAEA(int iter, String systems) {
        //Tracking computational time
        long start = System.currentTimeMillis();

        //First Step: Calculate Actual Metrics
        String userPath = System.getProperty("user.dir");
        String[] args = {"-l", "Java", "-p", userPath + "/resources/systems/" + systems + "/src","-r" ,"-s", "     " + systems + "      "};

        //Second Step: Create the structures for the prediction
        MainPredFormulasBIoRIPM init = new MainPredFormulasBIoRIPM();
        init.main(args);
        MetaphorCode metaphor = new MetaphorCode(init);

        //Third Step: Optimization
        // Search Space definition
        int DIM = 5;
        Space<List<RefactoringOperation>> space = new RefactoringOperationSpace(DIM);

        // Optimization Function
        OptimizationFunction<List<RefactoringOperation>> function = new FitnessQualityDBScala(systems + "_HAEA_" + iter);
        Goal<List<RefactoringOperation>> goal = new OptimizationGoal<List<RefactoringOperation>>(function); // maximizing, remove the parameter false if minimizing

        // Variation definition
        RefOperMutation mutation = new RefOperMutation(0.5);
        ArityTwo<List<RefactoringOperation>> xover = new RefOperXOver();
        ArityOne<List<RefactoringOperation>> transposition = new RefOperClassTransposition();

        // Search method
        int POPSIZE = 40;
        int MAXITERS = 100;

        @SuppressWarnings("unchecked")
        Operator<List<RefactoringOperation>>[] opers = (Operator<List<RefactoringOperation>>[]) new Operator[3];
        opers[0] = mutation;
        opers[1] = xover;
        opers[2] = transposition;

        HaeaOperators<List<RefactoringOperation>> operators = new SimpleHaeaOperators<List<RefactoringOperation>>(opers);
        HAEA<List<RefactoringOperation>> search = new HAEA<List<RefactoringOperation>>(POPSIZE, operators, new Tournament<List<RefactoringOperation>>(4), MAXITERS);

        // Tracking the goal evaluations
        WriteDescriptors write_desc = new WriteDescriptors();
        RefactorArrayPlainWrite write = new RefactorArrayPlainWrite(false);
        List<RefactoringOperation> ref = new ArrayList<RefactoringOperation>();
        Write.set(ref, write);
        Write.set(HaeaStep.class, new WriteHaeaStep<List<RefactoringOperation>>());
        Descriptors.set(PopulationSolution.class, new PopulationSolutionDescriptors<List<RefactoringOperation>>());
        Descriptors.set(HaeaOperators.class, new SimpleHaeaOperatorsDescriptor<List<RefactoringOperation>>());
        Write.set(HaeaOperators.class, write_desc);

        ConsoleTracer tracer = new ConsoleTracer();
        FileTracer filetracergoal = new FileTracer(systems + "_fileTracerCCODECGOAL_" + iter, '\n');
        Tracer.addTracer(goal, tracer);  // Uncomment if you want to trace the function evaluations
        Tracer.addTracer(search, tracer); // Uncomment if you want to trace the hill-climbing algorithm
        Tracer.addTracer(goal, filetracergoal);  // Uncomment if you want to trace the function evaluations

        // Apply the search method
        Solution<List<RefactoringOperation>> solution = search.apply(space, goal);

        long end = System.currentTimeMillis();
        System.out.println(solution.quality() + "=" + solution.value());
        escribirTextoArchivo(iter + "__" + solution.quality() + "=" + solution.value());
        escribirTextoArchivo(iter + "_time_:_" + (end - start) + "\n");
        //The assertion verifies if the time taken is less than four hours in milliseconds
        //assertTrue( (end - start) < 1.44E+7 );
    }


    public static void main(String[] args) {
        String systems = SYS;
        for (int i = 0; i < 30; i++) {
            refactorHAEA(i, systems);
        }
    }

    public static void escribirTextoArchivo(String texto) {
        String systems = SYS;
        String ruta = systems + "_T_HAEAFIX_JAR.txt";
        try (FileWriter fw = new FileWriter(ruta, true);
             FileReader fr = new FileReader(ruta)) {
            //Escribimos en el fichero un String y un caracter 97 (a)
            fw.write(texto);
            //Guardamos los cambios del fichero
            fw.flush();
        } catch (IOException e) {
            System.out.println("Error E/S: " + e);
        }

    }
}
