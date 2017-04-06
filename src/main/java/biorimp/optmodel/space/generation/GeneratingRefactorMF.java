/**
 *
 */
package biorimp.optmodel.space.generation;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.Refactoring;
import biorimp.optmodel.space.repairing.RepairingRefactor;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefParam;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;
import unalcol.random.integer.IntUniform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class GeneratingRefactorMF extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor
     * (java.lang.String,
     * unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.moveField;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src;
        IntUniform g = new IntUniform(MetaphorCode.getClassesWithFields().size());

        do {
            feasible = true;
            params = new ArrayList<OBSERVRefParam>();

            //Creating the OBSERVRefParam for the src class
            sysType_src = MetaphorCode.getClassesWithFields().get(g.generate());
            List<String> value_src = new ArrayList<String>();
            value_src.add(sysType_src.getQualifiedName());
            params.add(new OBSERVRefParam("src", value_src));

            //3. Creating the OBSERVRefParam for the fld field
            List<String> value_fld = new ArrayList<String>();
            if (!MetaphorCode.getFieldsFromClass(sysType_src).isEmpty()) {
                IntUniform numFldObs = new IntUniform(MetaphorCode.getFieldsFromClass(sysType_src).size());

                value_fld.add((String) MetaphorCode.getFieldsFromClass(sysType_src).toArray()
                        [numFldObs.generate()]);
                params.add(new OBSERVRefParam("fld", value_fld));
            } else {
                //value_fld.add("");
                //params.add(new OBSERVRefParam("fld", value_fld));
                feasible = false;
            }

        } while (!feasible);

        //4. Creating the OBSERVRefParam for the tgt
        List<String> value_tgt = new ArrayList<String>();
        TypeDeclaration sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
        value_tgt.add(sysType_tgt.getQualifiedName());
        params.add(new OBSERVRefParam("tgt", value_tgt));

        return new OBSERVRefactoring(type.name(), params, feasible, penalty);
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair;

        boolean feasible;
        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);

        feasible = true;
        params = new ArrayList<OBSERVRefParam>();

        //1. Creating the OBSERVRefParam for the src class
        List<String> value_src = new ArrayList<String>();
        value_src.add(sysType_src.getQualifiedName());
        params.add(new OBSERVRefParam("src", value_src));

        //2. Creating the OBSERVRefParam for the fld field
        List<String> value_fld = new ArrayList<String>();
        if (!MetaphorCode.getFieldsFromClass(sysType_src).isEmpty()) {
            IntUniform numFldObs = new IntUniform(MetaphorCode.getFieldsFromClass(sysType_src).size());

            value_fld.add((String) MetaphorCode.getFieldsFromClass(sysType_src).toArray()
                    [numFldObs.generate()]);
            params.add(new OBSERVRefParam("fld", value_fld));
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
            //3. Creating the OBSERVRefParam for the tgt
            List<String> value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = RepairingRefactor.extractTGTforRepairing(ref);
            value_tgt.add(sysType_tgt.getQualifiedName());
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, ref.getPenalty());
        }

        return refRepair;
    }

}
