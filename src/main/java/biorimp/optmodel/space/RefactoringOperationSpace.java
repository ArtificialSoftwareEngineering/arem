package biorimp.optmodel.space;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.feasibility.FeasibilityRefactor;
import biorimp.optmodel.space.generation.*;
import biorimp.optmodel.space.repairing.RepairRefOper;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactorings;
import edu.wayne.cs.severe.redress2.exception.ReadException;
import unalcol.clone.Clone;
import unalcol.search.space.Space;

import java.util.ArrayList;
import java.util.List;

public class RefactoringOperationSpace extends Space<List<RefactoringOperation>> {
    protected int n = 1;

    public RefactoringOperationSpace() {
    }

    public RefactoringOperationSpace(int n) {
        this.n = n;
    }


    @Override
    public boolean feasible(List<RefactoringOperation> x) {
        boolean feasible = false;
        String mapRefactor;

        for (RefactoringOperation refOp : x) {
            mapRefactor = refOp.getRefType().getAcronym();
            switch (mapRefactor) {
                case "PUF":
                    feasible = FeasibilityRefactor.feasibleRefactorPUF(refOp);
                    break;
                case "MM":
                    feasible = FeasibilityRefactor.feasibleRefactorMM(refOp);
                    break;
                case "RMMO":
                    feasible = FeasibilityRefactor.feasibleRefactorRMMO(refOp);
                    break;
                case "RDI":
                    feasible = FeasibilityRefactor.feasibleRefactorRDI(refOp);
                    break;
                case "MF":
                    feasible = FeasibilityRefactor.feasibleRefactorMF(refOp);
                    break;
                case "EM":
                    feasible = FeasibilityRefactor.feasibleRefactorEM(refOp);
                    break;
                case "PDM":
                    feasible = FeasibilityRefactor.feasibleRefactorPDM(refOp);
                    break;
                case "RID":
                    feasible = FeasibilityRefactor.feasibleRefactorRID(refOp);
                    break;
                case "IM":
                    feasible = FeasibilityRefactor.feasibleRefactorIM(refOp);
                    break;
                case "PUM":
                    feasible = FeasibilityRefactor.feasibleRefactorPUM(refOp);
                    break;
                case "PDF":
                    feasible = FeasibilityRefactor.feasibleRefactorPDF(refOp);
                    break;
                case "EC":
                    feasible = FeasibilityRefactor.feasibleRefactorEC(refOp);
                    break;
            }//END CASE

            if (!feasible) {
                System.out.println("Wrong Feasible Refactor (IN FEASIBLE): " + refOp.toString());
                break;
            }
        }
        return x.size() <= n && feasible;
    }

    @Override
    public double feasibility(List<RefactoringOperation> x) {
        return feasible(x) ? 1 : 0;
    }

    @Override
    public List<RefactoringOperation> repair(List<RefactoringOperation> x) {
        List<RefactoringOperation> clon;

        //1. Repairing Space
        if (x != null) {
            //x.removeAll(null);
            if (x.size() > n) {
                clon = new ArrayList<RefactoringOperation>();
                for (int i = 0; i < n; i++) {
                    clon.add(x.get(i));
                    //repaired.add( x.get(i) );
                }
            } else {
                clon = (List<RefactoringOperation>) Clone.create(x);
                if (x.size() < n) {
                    clon.addAll(CreateRefOper.get(n - x.size()));
                }
            }
        } else {
            clon = new ArrayList<RefactoringOperation>();
            clon.addAll(get());
        }

        //2. Repairing Refactoring
        return RepairRefOper.repair(clon);
    }

    @Override
    public List<RefactoringOperation> get() {
        return CreateRefOper.get(n);
    }


}
