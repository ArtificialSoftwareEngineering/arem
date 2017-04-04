/**
 *
 */
package biorimp.optmodel.space;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.generation.*;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactorings;
import edu.wayne.cs.severe.redress2.exception.ReadException;
import unalcol.random.integer.IntUniform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class CreateRefOper {


    public static List<RefactoringOperation> get(int n) {


        List<RefactoringOperation> setRefactoring;

        if ( MetaphorCode.getClassesWithInheritance().isEmpty() ) {
            setRefactoring = getFixedRefactoring(n);
        } else {
            setRefactoring = getRefactoringWithInheritance(n);
        }

        return setRefactoring;
    }

    static public List<RefactoringOperation> getRefactoringWithInheritance(int n) {
        int mapRefactor;
        OBSERVRefactorings oper = new OBSERVRefactorings();
        List<OBSERVRefactoring> refactorings = new ArrayList<OBSERVRefactoring>();

        IntUniform g = new IntUniform(Refactoring.values().length);
        GeneratingRefactor randomRefactor = null;

        for (int i = 0; i < n; i++) {
            mapRefactor = g.generate();
            switch (mapRefactor) {
                case 0:
                    randomRefactor = new GeneratingRefactorEC();
                    break;
                case 1:
                    randomRefactor = new GeneratingRefactorMM();
                    break;
                case 2:
                    randomRefactor = new GeneratingRefactorRMMO();
                    break;
                case 3:
                    randomRefactor = new GeneratingRefactorRDI();
                    break;
                case 4:
                    randomRefactor = new GeneratingRefactorMF();
                    break;
                case 5:
                    randomRefactor = new GeneratingRefactorEM();
                    break;
                case 6:
                    randomRefactor = new GeneratingRefactorIM();
                    break;
                case 7:
                    randomRefactor = new GeneratingRefactorRID();
                    break;
                case 8:
                    randomRefactor = new GeneratingRefactorPDF();
                    break;
                case 9:
                    randomRefactor = new GeneratingRefactorPUF();
                    break;
                case 10:
                    randomRefactor = new GeneratingRefactorPDM();
                    break;
                case 11:
                    randomRefactor = new GeneratingRefactorPUM();
                    break;
            }//END CASE

            OBSERVRefactoring candidateRef = randomRefactor.generatingRefactor(new ArrayList<Double>());
            if (candidateRef != null) {
                refactorings.add(candidateRef);
            }
        }
        oper.setRefactorings(refactorings);
        try {
            return MetaphorCode.getRefactorReader().getRefactOperations(oper);
        } catch (ReadException e) {
            e.printStackTrace();
            System.out.println("Reading Error :3");
            return null;
        }
    }

    static public  List<RefactoringOperation> getFixedRefactoring(int k) {

        int mapRefactor;
        OBSERVRefactorings oper = new OBSERVRefactorings();
        List<OBSERVRefactoring> refactorings = new ArrayList<OBSERVRefactoring>();

        final int DECREASE = 5; //Excluding Inheritance during the repairing
        IntUniform g = new IntUniform(Refactoring.values().length - DECREASE);
        GeneratingRefactor randomRefactor = null;

        for (int i = 0; i < k; i++) {
            mapRefactor = g.generate();
            switch (mapRefactor) {
                case 0:
                    randomRefactor = new GeneratingRefactorEC();
                    break;
                case 1:
                    randomRefactor = new GeneratingRefactorMM();
                    break;
                case 2:
                    randomRefactor = new GeneratingRefactorRMMO();
                    break;
                case 3:
                    randomRefactor = new GeneratingRefactorRDI();
                    break;
                case 4:
                    randomRefactor = new GeneratingRefactorMF();
                    break;
                case 5:
                    randomRefactor = new GeneratingRefactorEM();
                    break;
                case 6:
                    randomRefactor = new GeneratingRefactorIM();
                    break;
            }//END CASE
            refactorings.add(randomRefactor.generatingRefactor(new ArrayList<Double>()));
        }

        oper.setRefactorings(refactorings);
        try {
            return MetaphorCode.getRefactorReader().getRefactOperations(oper);
        } catch (ReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Reading Error");
            return null;
        }
    }
}
