package biorimp.optmodel.space;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.feasibility.FeasibilityRefactor;
import biorimp.optmodel.space.generation.*;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactorings;
import edu.wayne.cs.severe.redress2.exception.ReadException;
import unalcol.clone.Clone;
import unalcol.random.integer.IntUniform;
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
        OBSERVRefactorings oper = new OBSERVRefactorings();
        List<OBSERVRefactoring> refactorings = new ArrayList<OBSERVRefactoring>();
        String mapRefactor;
        GeneratingRefactor specificRefactor;
        boolean feasible = false;

        List<RefactoringOperation> clon;
        List<RefactoringOperation> repaired = new ArrayList<RefactoringOperation>();

        //Repairing Space
        if (x != null) {
            if (x.size() > n) {
                clon = new ArrayList<RefactoringOperation>();
                for (int i = 0; i < n; i++) {
                    clon.add(x.get(i));
                    //repaired.add( x.get(i) );
                }
            } else {
                clon = (List<RefactoringOperation>) Clone.create(x);
                if (x.size() < n) {
                    clon.addAll(CreateRefOper.getFixedRefactoring(n - x.size()));
                }
            }
        } else {
            clon = new ArrayList<RefactoringOperation>();
            clon.addAll(get());
        }

        //Repairing Refactoring
        for (RefactoringOperation refOp : clon) {
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
                //Fixme, Repair must be static
                //When the refoper is not feasible then we have to penalize the repaired refactoring
                switch (mapRefactor) {
                    case "PUF":
                        specificRefactor = new GeneratingRefactorPUF();
                        break;
                    case "MM":
                        specificRefactor = new GeneratingRefactorMM();
                        break;
                    case "RMMO":
                        specificRefactor = new GeneratingRefactorRMMO();
                        break;
                    case "RDI":
                        specificRefactor = new GeneratingRefactorRDI();
                        break;
                    case "MF":
                        specificRefactor = new GeneratingRefactorMF();
                        break;
                    case "EM":
                        specificRefactor = new GeneratingRefactorEM();
                        break;
                    case "PDM":
                        specificRefactor = new GeneratingRefactorPDM();
                        break;
                    case "RID":
                        specificRefactor = new GeneratingRefactorRID();
                        break;
                    case "IM":
                        specificRefactor = new GeneratingRefactorIM();
                        break;
                    case "PUM":
                        specificRefactor = new GeneratingRefactorPUM();
                        break;
                    case "PDF":
                        specificRefactor = new GeneratingRefactorPDF();
                        break;
                    case "EC":
                        specificRefactor = new GeneratingRefactorEC();
                        break;
                    default:
                        specificRefactor = new GeneratingRefactorEC();
                }//END CASE
                //refactorings.add(specificRefactor.repairRefactor(refOp, break_point));
                OBSERVRefactoring candidateRef = specificRefactor.repairRefactor(refOp);
                if (candidateRef != null) {
                    refactorings.add(candidateRef);
                }
            } else {
                //If it is feasible then the refactoring operation remains the same.
                refOp.setNonRepair(true);//Starts from the beginning No Penalty
                repaired.add(refOp);
            }
        }

        oper.setRefactorings(refactorings);

        try {
            List<RefactoringOperation> repairedOper =
                    MetaphorCode.getRefactorReader().getRefactOperations(oper);
            for (RefactoringOperation refOp : repairedOper) {
                refOp.setNonRepair(false); //The penalty is high according to repair vector
            }
            repaired.addAll(repairedOper);

        } catch (ReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Reading Error in Repair");
            return null;
        }

        return repaired;
    }

    @Override
    public List<RefactoringOperation> get() {

        List<RefactoringOperation> setRefactoring;

        if ( MetaphorCode.getClassesWithInheritance().isEmpty() ) {
            setRefactoring = CreateRefOper.getFixedRefactoring(n);
        } else {
            setRefactoring = CreateRefOper.getRefactoringWithInheritance(n);
        }

        return setRefactoring;
    }


}
