/**
 *
 */
package biorimp.optmodel.space.generation;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.Refactoring;
import biorimp.optmodel.space.reapairing.RepairingRefactor;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.CodeObjState;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefParam;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import unalcol.random.integer.IntUniform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class GeneratingRefactorRDI extends GeneratingRefactor {

    /* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
     */
    protected Refactoring type = Refactoring.replaceDelegationInheritance;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        int counterRDI = 0; //<-- 1.
        int break_point = MetaphorCode.getClassesWithOutInheritance().size();//Number of Classes
        IntUniform g = new IntUniform(break_point);
        OBSERVRefactoring refRepair = null;
        List<String> value_src;
        List<String> value_tgt;
        do {
            feasible = true;
            //2. Creating the OBSERVRefParam for the src class
            TypeDeclaration sysType_src = MetaphorCode.getClassesWithOutInheritance().get(g.generate());
            value_src = new ArrayList<String>();
            value_src.add(sysType_src.getQualifiedName());


            //Creating the OBSERVRefParam for the tgt
            value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = MetaphorCode.getClassesWithOutInheritance().get(g.generate());
            value_tgt.add(sysType_tgt.getQualifiedName());


            //3. Verification of equality
            if (sysType_src.equals(sysType_tgt))
                feasible = false;

            if (feasible) {
                //4. Hierarchy verification parents
                if (MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()) != null) {
                    if (!MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                        for (TypeDeclaration parentClasses : MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName())) {
                            if (parentClasses.equals(sysType_tgt)) {
                                feasible = false;
                                break;
                            }

                        }
                    }

                }
                if (feasible) {
                    //Hierarchy verification children
                    if (MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()) != null) {
                        if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                            for (TypeDeclaration childrenClasses : MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName())) {
                                if (childrenClasses.equals(sysType_tgt)) {
                                    feasible = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            counterRDI++;
            if (counterRDI > break_point) {
                feasible = false;
                break;
            }

        } while (!feasible);

        if (feasible){
            List<OBSERVRefParam> params;
            params = new ArrayList<OBSERVRefParam>();
            params.add(new OBSERVRefParam("src", value_src));
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, penalty);
        }

        return refRepair;
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair;
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);
        TypeDeclaration sysType_tgt = RepairingRefactor.extractTGTforRepairing(ref);
        boolean feasible = true;
        List<OBSERVRefParam> params;

        params = new ArrayList<OBSERVRefParam>();

        //1. Creating the OBSERVRefParam for the src class
        List<String> value_src = new ArrayList<String>();
        value_src.add(sysType_src.getQualifiedName());
        params.add(new OBSERVRefParam("src", value_src));

        //2. Creating the OBSERVRefParam for the tgt
        List<String> value_tgt = new ArrayList<String>();
        value_tgt.add(sysType_tgt.getQualifiedName());
        params.add(new OBSERVRefParam("tgt", value_tgt));

        //3. Verification of equality
        if (sysType_src.equals(sysType_tgt))
            feasible = false;

        if (feasible) {
            //Hierarchy verification parents
            if (MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()) != null)
                if (!MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                    for (TypeDeclaration clase : MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName())) {
                        if (clase.equals(sysType_tgt)) {
                            feasible = false;
                            break;
                        }

                    }
                }

            if (feasible) {
                //Hierarchy verification children
                if (MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()) != null)
                    if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                        for (TypeDeclaration clase : MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName())) {
                            if (clase.equals(sysType_tgt)) {
                                feasible = false;
                                break;
                            }
                        }
                    }
            }
        }

        if (!feasible) {
            //Penalty
            ref.getPenalty().add(penaltyReGeneration);
            refRepair = generatingRefactor(ref.getPenalty());
        } else {
            //Penalty
            ref.getPenalty().add(penaltyRepair);
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, ref.getPenalty());
        }

        return refRepair;
    }

}
