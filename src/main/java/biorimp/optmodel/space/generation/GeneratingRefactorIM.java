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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class GeneratingRefactorIM extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.inlineMethod;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        List<OBSERVRefParam> params;
        IntUniform g = new IntUniform(MetaphorCode.getClassesWithMethods().size());
        TypeDeclaration sysType_src;

        do {
            feasible = true;
            params = new ArrayList<OBSERVRefParam>();

            //2. Creating the OBSERVRefParam for the src class
            sysType_src = MetaphorCode.getClassesWithMethods().get(g.generate());
            List<String> value_src = new ArrayList<String>();
            value_src.add(sysType_src.getQualifiedName());
            params.add(new OBSERVRefParam("src", value_src));

            //3. Creating the OBSERVRefParam for the mtd class
            List<String> value_mtd = new ArrayList<String>();
            if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

                IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());
                value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                        [numMtdObs.generate()]);

                //4. Verification of method not constructor
                feasible = InspectRefactor.inspectMethodNotConstructor(value_mtd, sysType_src);

                if (feasible) {
                    //Override verification parents
                    feasible = InspectRefactor.inspectOverrideParents(value_mtd, sysType_src);
                    if (feasible) {
                        //Override verification children
                        feasible = InspectRefactor.inspectOverrideChildren(value_mtd, sysType_src);
                    }
                }
                params.add(new OBSERVRefParam("mtd", value_mtd));
            } else {
                feasible = false;
            }
        } while (!feasible);//<-- Generating feasible individuals

        return new OBSERVRefactoring(type.name(), params, feasible, penalty);
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair;
        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);

        boolean feasible = true;
        params = new ArrayList<OBSERVRefParam>();

        //1. Creating the OBSERVRefParam for the src class
        List<String> value_src = new ArrayList<String>();
        value_src.add(sysType_src.getQualifiedName());
        params.add(new OBSERVRefParam("src", value_src));

        //2. Creating the OBSERVRefParam for the mtd class
        List<String> value_mtd = new ArrayList<String>();
        if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

            IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());
            value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                    [numMtdObs.generate()]);

            //3. Verification of method not constructor
            if (value_mtd.get(0).equals(sysType_src.getName()))
                feasible = false;

            if (feasible) {
                //Override verification parents
                feasible = InspectRefactor.inspectOverrideParents(value_mtd, sysType_src);
                if (feasible) {
                    //Override verification children
                    feasible = InspectRefactor.inspectOverrideChildren(value_mtd, sysType_src);
                }
            }
            params.add(new OBSERVRefParam("mtd", value_mtd));
        } else {
            feasible = false;
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
