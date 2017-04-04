/**
 *
 */
package biorimp.optmodel.space.generation;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.Refactoring;
import biorimp.optmodel.space.feasibility.InspectRefactor;
import biorimp.optmodel.space.reapairing.RepairingRefactor;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.CodeObjState;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefParam;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import unalcol.random.integer.IntUniform;
import unalcol.random.util.RandBool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class GeneratingRefactorPDM extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.pushDownMethod;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;

        int counterPDM = 0; //<-- 1.
        int break_point = MetaphorCode.getClassesWithInheritanceAndMethods().size();//Number of Classes
        OBSERVRefactoring refRepair = null;

        IntUniform g = new IntUniform(break_point);
        TypeDeclaration sysType_src;
        List<String> value_tgt;
        List<String> value_src;
        List<String> value_mtd;


        do {
            //2.Generating a random src with its mtd
            do {
                feasible = true;

                //Creating the OBSERVRefParam for the src class/super class
                sysType_src = MetaphorCode.getClassesWithInheritanceAndMethods().get(g.generate());
                value_src = new ArrayList<String>();
                value_src.add(sysType_src.getQualifiedName());

                //3. Creating the OBSERVRefParam for the mtd class
                value_mtd = new ArrayList<String>();
                if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

                    IntUniform numMtdObs =
                            new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

                    value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                            [numMtdObs.generate()]);

                    //4. Override verification parents
                    feasible = InspectRefactor.inspectOverrideParents(value_mtd, sysType_src);

                    if (feasible) {
                        // Override verification children
                        feasible = InspectRefactor.inspectOverrideChildren(value_mtd, sysType_src);
                    }

                    if (feasible) {
                        //5. Verification of method not constructor
                        feasible = InspectRefactor.inspectMethodNotConstructor(value_mtd, sysType_src);
                    }

                } else {
                    feasible = false;
                }

                //6. Break point Verification
                counterPDM++;
                if (counterPDM > break_point) {
                    feasible = false;
                    break;
                }
            } while (!feasible);

            //Creating the OBSERVRefParam for the tgt class/child classes
            value_tgt = new ArrayList<String>();

            //Verification of SRCSupClassTGT
            //7. Retrieving all child classes and choosing randomly
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childClasses =
                        MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName());
                RandBool gC = new RandBool();
                do {
                    for (TypeDeclaration clase : childClasses) {
                        if (gC.next()) {
                            value_tgt.add(clase.getQualifiedName());
                        }
                    }
                } while (value_tgt.isEmpty());
            } else {
                feasible = false;
            }

            //counterPDM++;
            if (counterPDM > break_point) {
                feasible = false;
                break;
            }
        } while (!feasible);//Checking Subclasses for SRC selected

        if(feasible) {
            List<OBSERVRefParam> params;
            params = new ArrayList<OBSERVRefParam>();
            params.add(new OBSERVRefParam("src", value_src));
            params.add(new OBSERVRefParam("mtd", value_mtd));
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, penalty);
        }

        return refRepair;
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair;

        boolean feasible;
        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);
        List<String> value_tgt = null;
        List<String> value_src;
        List<String> value_mtd;

        feasible = true;
        params = new ArrayList<OBSERVRefParam>();

        //1. Creating the OBSERVRefParam for the src class/super class
        value_src = new ArrayList<String>();
        value_src.add(sysType_src.getQualifiedName());

        //2. Creating the OBSERVRefParam for the mtd class
        value_mtd = new ArrayList<String>();
        if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

            IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

            value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                    [numMtdObs.generate()]);

            //3. Override verification parents
            feasible = InspectRefactor.inspectOverrideParents(value_mtd, sysType_src);

            if (feasible) {
                //Override verification children
                feasible = InspectRefactor.inspectOverrideChildren(value_mtd, sysType_src);
            }

            if (feasible) {
                //4. verification of method not constructor
                feasible = InspectRefactor.inspectMethodNotConstructor(value_mtd, sysType_src);
            }

        } else {
            feasible = false;
        }

        if (feasible) {

            //Creating the OBSERVRefParam for the tgt class/child classes
            value_tgt = new ArrayList<String>();

            //Verification of SRCSupClassTGT
            //5. Retrieving all child classes and choosing randomly
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName());
                RandBool gC = new RandBool();
                do {
                    for (TypeDeclaration clase : childClasses) {
                        if (gC.next()) {
                            value_tgt.add(clase.getQualifiedName());
                        }
                    }
                } while (value_tgt.isEmpty());
            } else {
                feasible = false;
            }
        }

        if (!feasible) {
            //Penalty
            ref.getPenalty().add(penaltyReGeneration);
            refRepair = generatingRefactor(ref.getPenalty());
        } else {
            //Penalty
            ref.getPenalty().add(penaltyRepair);
            params.add(new OBSERVRefParam("src", value_src));
            params.add(new OBSERVRefParam("mtd", value_mtd));
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, ref.getPenalty());
        }

        return refRepair;
    }

}
