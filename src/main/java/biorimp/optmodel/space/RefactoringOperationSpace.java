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
        int break_point = 10;

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
                    clon.addAll(getRefactoring(n - x.size()));
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
                refactorings.add(specificRefactor.repairRefactor(refOp, break_point));
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


    public List<RefactoringOperation> getRefactoring(int k) {

        int mapRefactor;
        OBSERVRefactorings oper = new OBSERVRefactorings();
        List<OBSERVRefactoring> refactorings = new ArrayList<OBSERVRefactoring>();

        final int DECREASE = 5;
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
                //TODO: Quitar defaul y descomentar lineas del switch para activar todas
                default:
                    randomRefactor = new GeneratingRefactorIM();
            }//END CASE

            //System.out.println( "Refactor [ " + Refactoring.values()[mapRefactor] + "]");
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

    @Override
    public List<RefactoringOperation> get() {

        int mapRefactor;
        OBSERVRefactorings oper = new OBSERVRefactorings();
        List<OBSERVRefactoring> refactorings = new ArrayList<OBSERVRefactoring>();

        final int DECREASE = MetaphorCode.getDECREASE();
        IntUniform g = new IntUniform(Refactoring.values().length - DECREASE);
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
                //TODO: Quitar defaul y descomentar lineas del switch para activar todas
                default:
                    randomRefactor = new GeneratingRefactorIM();
            }//END CASE

            //System.out.println( "Refactor [ " + Refactoring.values()[mapRefactor] + "]");
            refactorings.add(randomRefactor.generatingRefactor(new ArrayList<Double>()));

        }

        oper.setRefactorings(refactorings);
        try {
            return MetaphorCode.getRefactorReader().getRefactOperations(oper);
        } catch (ReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Reading Error :3");
            return null;
        }
    }

}