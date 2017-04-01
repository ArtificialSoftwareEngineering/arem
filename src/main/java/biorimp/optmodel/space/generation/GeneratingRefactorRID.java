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
public class GeneratingRefactorRID extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.replaceInheritanceDelegation;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        List<OBSERVRefParam> params;
        IntUniform g = new IntUniform(MetaphorCode.getClassesWithInheritance().size());

        int counterRID = 0; //<-- 1.
        int break_point = MetaphorCode.getClassesWithInheritance().size();//Number of Classes

        do {
            feasible = true;
            params = new ArrayList<OBSERVRefParam>();

            //2. Creating the OBSERVRefParam for the src class
            List<String> value_src = new ArrayList<String>();

            //Creating the OBSERVRefParam for the tgt
            List<String> value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = MetaphorCode.getClassesWithInheritance().get(g.generate());
            value_tgt.add(sysType_tgt.getQualifiedName());
            params.add(new OBSERVRefParam("tgt", value_tgt));

            //4. Verification of SRCSubClassTGT
            if (MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()) != null) {
                if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                    List<TypeDeclaration> childClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                    for (TypeDeclaration clase : childClasses) {
                        value_src.add(clase.getQualifiedName());
                    }
                    params.add(new OBSERVRefParam("src", value_src));
                } else {
                    feasible = false;
                }
            } else {
                feasible = false;
            }

            counterRID++;
            if (counterRID > break_point) {
                feasible = false;
                break;
            }
        } while (!feasible);

        return new OBSERVRefactoring(type.name(), params, feasible, penalty);
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
            //4. Verification of SRCSubClassTGT
            if (MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()) != null) {
                if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                    List<TypeDeclaration> childrenClasses =
                            MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                    feasible = false;
                    for (TypeDeclaration clase : childrenClasses) {
                        if (clase.equals(sysType_src)) {
                            feasible = true;
                            break;
                        }
                    }
                } else {
                    feasible = false;
                }
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
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, ref.getPenalty());
        }

        return refRepair;
    }
}
