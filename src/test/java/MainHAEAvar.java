import biorimp.optmodel.fitness.FitnessQualityDB;
import biorimp.optmodel.fitness.FitnessQualityDBScala;
import biorimp.optmodel.fitness.RefactorArrayPlainWrite;
import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.operators.RefOperAddGen;
import biorimp.optmodel.operators.RefOperDelGen;
import biorimp.optmodel.operators.RefOperJoin;
import biorimp.optmodel.space.VarLengthOperRefSpace;
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
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainHAEAvar {

    public static void refactorVAR(int iter, String systems) {
        //Tracking computational time
        long start = System.currentTimeMillis();

        //First Step: Calculate Actual Metrics
        String userPath = System.getProperty("user.dir");
        String[] args = {"-l", "Java", "-p", userPath + "/test_data/code/" + systems + "/src", "-s", "     " + systems + "      "};

        //Second Step: Create the structures for the prediction
        MainPredFormulasBIoRIPM init = new MainPredFormulasBIoRIPM();
        init.main(args);
        MetaphorCode metaphor = new MetaphorCode(init, iter);

        //Third Step: Optimization
        // Search Space definition
        VarLengthOperRefSpace space = new VarLengthOperRefSpace(2, 7);

        // Optimization Function
        OptimizationFunction<List<RefactoringOperation>> function = new FitnessQualityDBScala(systems + "_HAEAVAR_" + iter);
        Goal<List<RefactoringOperation>> goal = new OptimizationGoal<List<RefactoringOperation>>(function); // maximizing, remove the parameter false if minimizing

        // Variation definition
        RefOperAddGen add = new RefOperAddGen(1, 2, 7);
        RefOperDelGen del = new RefOperDelGen(1, 2, 7);
        ArityTwo<List<RefactoringOperation>> xover = new RefOperJoin();

        // Search method
        int POPSIZE = 50;
        int MAXITERS = 100;
        @SuppressWarnings("unchecked")
        Operator<List<RefactoringOperation>>[] opers = (Operator<List<RefactoringOperation>>[]) new Operator[3];
        opers[0] = add;
        opers[1] = del;
        opers[2] = xover;

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
        Tracer.addTracer(goal, filetracergoal);

        // Apply the search method
        Solution<List<RefactoringOperation>> solution = search.apply(space, goal);

        long end = System.currentTimeMillis();
        System.out.println(solution.quality() + "=" + solution.value());
        escribirTextoArchivo(iter + "__" + solution.quality() + "=" + solution.value());
        escribirTextoArchivo(iter + "_time_:_" + (end - start) + "\n");
    }

    public static void main(String[] args) {
        String systems = "acra";
        for (int i = 0; i < 30; i++) {
            refactorVAR(i, systems);
        }
    }

    public static void escribirTextoArchivo(String texto) {

        String systems = "acra";
        String ruta = systems + "_T_HAEAVAR_JAR.txt";
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
