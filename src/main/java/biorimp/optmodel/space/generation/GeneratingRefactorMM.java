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
public class GeneratingRefactorMM extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.moveMethod;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        List<OBSERVRefParam> params;
        IntUniform g = new IntUniform(MetaphorCode.getMapClass().size());
        TypeDeclaration sysType_src;
        List<String> value_mtd;
        List<String> value_src;
        List<String> value_tgt;

        do {
            do {
                feasible = true;
                params = new ArrayList<OBSERVRefParam>();

                //2. Creating the OBSERVRefParam for the src class
                sysType_src = MetaphorCode.getMapClass().get(g.generate());
                value_src = new ArrayList<String>();
                value_src.add(sysType_src.getQualifiedName());


                //3. Creating the OBSERVRefParam for the mtd class
                value_mtd = new ArrayList<String>();
                if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {
                    IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

                    value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                            [numMtdObs.generate()]);

                    //4. Verification of method not constructor
                    feasible = InspectRefactor.inspectMethodNotConstructor(value_mtd, sysType_src);

                } else {
                    feasible = false;
                }
            } while (!feasible);

            //5. Creating the OBSERVRefParam for the tgt
            value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
            value_tgt.add(sysType_tgt.getQualifiedName());


            //6. Override and Hierarchy verification parents
            feasible = InspectRefactor.inspectHierarchyOverrideParents(value_mtd, sysType_src, sysType_tgt);
            if (feasible) {
                //Override and hierarchy verification children
                feasible = InspectRefactor.inspectHierarchyOverrideChildren(value_mtd, sysType_src, sysType_tgt);
            }

        } while (!feasible);

        params.add(new OBSERVRefParam("src", value_src));
        params.add(new OBSERVRefParam("mtd", value_mtd));
        params.add(new OBSERVRefParam("tgt", value_tgt));

        return new OBSERVRefactoring(type.name(), params, feasible, penalty);

    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair;

        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);
        List<String> value_mtd;
        List<String> value_src;
        List<String> value_tgt = null;

        boolean feasible = true;
        params = new ArrayList<OBSERVRefParam>();

        //1. Creating the OBSERVRefParam for the src class
        value_src = new ArrayList<String>();
        value_src.add(sysType_src.getQualifiedName());


        //2. Creating the OBSERVRefParam for the mtd class
        value_mtd = new ArrayList<String>();
        if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {
            IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

            value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                    [numMtdObs.generate()]);

            //3. Verification of method not constructor
            if (value_mtd.get(0).equals(sysType_src.getName()))
                feasible = false;

        } else {
            feasible = false;
        }

        if (feasible) {
            //4. Creating the OBSERVRefParam for the tgt
            value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = RepairingRefactor.extractTGTforRepairing(ref);
            value_tgt.add(sysType_tgt.getQualifiedName());

            //5. Override and Hierarchy verification parents
            feasible = InspectRefactor.inspectHierarchyOverrideParents(value_mtd, sysType_src, sysType_tgt);
            if (feasible) {
                //Override and hierarchy verification children
                feasible = InspectRefactor.inspectHierarchyOverrideChildren(value_mtd, sysType_src, sysType_tgt);
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