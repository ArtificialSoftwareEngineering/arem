package biorimp.optmodel.space.generation;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.Refactoring;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;

import java.util.ArrayList;

public abstract class GeneratingRefactor {

    protected Refactoring type;
    protected double penaltyReGeneration = MetaphorCode.getPenaltyReGeneration();
    protected double penaltyRepair = MetaphorCode.getPenaltyRepair();
    private OBSERVRefactoring refactor;

    public abstract OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty);

    public abstract OBSERVRefactoring repairRefactor(RefactoringOperation ref,
                                                     int break_point);

    public OBSERVRefactoring getRefactor() {
        return refactor;
    }

    public void setRefactor(OBSERVRefactoring refactor) {
        this.refactor = refactor;
    }

}
