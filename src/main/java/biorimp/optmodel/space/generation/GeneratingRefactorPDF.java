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
import unalcol.random.util.RandBool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daavid
 */
public class GeneratingRefactorPDF extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.pushDownField;

    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;
        List<OBSERVRefParam> params;
        IntUniform g = new IntUniform(MetaphorCode.getClassesWithFields().size());
        TypeDeclaration sysType_src;

        int counterPDF = 0; //<-- 1.
        int break_point = MetaphorCode.getMapClass().size();//Number of Classes

        do {
            feasible = true;
            params = new ArrayList<OBSERVRefParam>();

            //2. Creating the OBSERVRefParam for the src class
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
                feasible = false;
            }

            if (feasible) {
                //Creating the OBSERVRefParam for the tgt class
                List<String> value_tgt = new ArrayList<String>();

                //Verification of SRCSupClassTGT
                //4.Retrieving all child classes and choosing randomly
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

                    params.add(new OBSERVRefParam("tgt", value_tgt));
                } else {
                    feasible = false;
                }
            }

            counterPDF++;
            if (counterPDF > break_point) {
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

        boolean feasible;
        List<OBSERVRefParam> params = new ArrayList<OBSERVRefParam>();
        TypeDeclaration sysType_src = RepairingRefactor.extractSRCforRepairing(ref);


        List<String> value_src = new ArrayList<String>();
        List<String> value_fld;
        List<String> value_tgt = null;

        //1. Creating the OBSERVRefParam for the src class
        value_src.add(sysType_src.getQualifiedName());

        feasible = true;
        value_fld = new ArrayList<String>();
        //2. Select fields from src class
        if (!MetaphorCode.getFieldsFromClass(sysType_src).isEmpty()) {
            IntUniform numFldObs = new IntUniform(MetaphorCode.getFieldsFromClass(sysType_src).size());
            value_fld.add((String) MetaphorCode.getFieldsFromClass(sysType_src).toArray()
                    [numFldObs.generate()]);

        } else {
            feasible = false;
        }

        if (feasible) {
            //Creating the OBSERVRefParam for the tgt class
            value_tgt = new ArrayList<String>();

            //Verification of SRCSupClassTGT
            //3. Retrieving all child classes and choosing randomly
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childrenClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName());
                RandBool gC = new RandBool();
                do {
                    for (TypeDeclaration clase : childrenClasses) {
                        if (gC.next()) {
                            //4. Pick Up Tgt
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
            params.add(new OBSERVRefParam("fld", value_fld));
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, ref.getPenalty());
        }

        return refRepair;
    }
}
