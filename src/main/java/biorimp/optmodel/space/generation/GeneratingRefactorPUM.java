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
public class GeneratingRefactorPUM extends GeneratingRefactor {

	/* (non-Javadoc)
     * @see entity.MappingRefactor#mappingRefactor(java.lang.String, unalcol.types.collection.bitarray.BitArray, entity.MetaphorCode)
	 */

    protected Refactoring type = Refactoring.pullUpMethod;


    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {
        // TODO Auto-generated method stub
        boolean feasible;

        List<OBSERVRefParam> params;

        int counterPUM = 0;
        int break_point = MetaphorCode.getMapClass().size();//Number of Classes

        IntUniform g = new IntUniform(break_point);
        List<String> value_mtd = null;
        List<String> value_tgt;
        TypeDeclaration sysType_tgt;
        TypeDeclaration sysType_src;
        List<String> value_src;

        do {
            feasible = true;
            params = new ArrayList<OBSERVRefParam>();

            //1. Creating the OBSERVRefParam for the tgt
            value_tgt = new ArrayList<String>();
            sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
            value_tgt.add(sysType_tgt.getQualifiedName());

            //Creating the OBSERVRefParam for the src class
            value_src = new ArrayList<String>();

            //2. Verification of SRCSubClassTGT
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                IntUniform indexClass = new IntUniform(childClasses.size());
                //3. RandomlySelectedClass
                sysType_src = childClasses.get(indexClass.generate());

                //Creating the OBSERVRefParam for the mtd class randomly
                value_mtd = new ArrayList<String>();

                if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

                    //4. Extracting methods from child class.
                    IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

                    //5. Picking up a method.
                    value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                            [numMtdObs.generate()]);

                    //6. Verification of method not constructor
                    if (value_mtd.get(0).equals(sysType_src.getName())) {
                        feasible = false;
                    } else {
                        //Choosing other src(s) with the mtd(?)
                        for (TypeDeclaration clase : childClasses) {
                            for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                if (method.equals(value_mtd.get(0))) {
                                    value_src.add(clase.getQualifiedName());
                                }
                            }
                        }

                        feasible = InspectRefactor.nonOverrideVerificationParentsAndChildren(value_src,
                                value_mtd);
                    }

                } else {
                    feasible = false;
                }

            } else {
                feasible = false;

            }

            counterPUM++;
            if (counterPUM > break_point) {
                feasible = false;
                break;
            }

        } while (!feasible);


        params.add(new OBSERVRefParam("src", value_src));
        params.add(new OBSERVRefParam("mtd", value_mtd));
        params.add(new OBSERVRefParam("tgt", value_tgt));

        //When returning without src, mtd or tgt, means that is not a feasible refactoring.
        return new OBSERVRefactoring(type.name(), params, feasible, penalty);
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair = null;

        int counterPUM = 0;


        boolean feasible;
        List<OBSERVRefParam> params;
        List<String> value_mtd = null;
        List<String> value_tgt;
        TypeDeclaration sysType_tgt = RepairingRefactor.extractTGTforRepairing(ref); //<--
        TypeDeclaration sysType_src;
        List<String> value_src;
        IntUniform numMtdObs;


        params = new ArrayList<OBSERVRefParam>();

        // 1. Creating the OBSERVRefParam for the tgt
        value_tgt = new ArrayList<String>();
        value_tgt.add(sysType_tgt.getQualifiedName());

        //Creating the OBSERVRefParam for the src class
        value_src = new ArrayList<String>();

        //2. Verification of SRCSubClassTGT
        if (MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()) != null) {
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                IntUniform indexClass = new IntUniform(childClasses.size());
                int break_point = childClasses.size();//Number of Children

                do {
                    feasible = true;
                    //3. RandomlySelectedClass
                    sysType_src = childClasses.get(indexClass.generate());

                    //4. Creating the OBSERVRefParam for the mtd class randomly
                    value_mtd = new ArrayList<String>();

                    if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {

                        numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());
                        value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                                [numMtdObs.generate()]);

                        //5. Verification of method not constructor
                        if (value_mtd.get(0).equals(sysType_src.getName())) {
                            feasible = false;
                        } else {
                            //6. Identifying other children classes src(s) with the same mtd
                            for (TypeDeclaration clase : childClasses) {
                                for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                    if (method.equals(value_mtd.get(0))) {
                                        value_src.add(clase.getQualifiedName());
                                    }
                                }
                            }

                            feasible = InspectRefactor.nonOverrideVerificationParentsAndChildren(value_src,
                                    value_mtd);
                        }

                    } else {
                        feasible = false;
                    }

                    counterPUM++;
                    if (counterPUM > break_point) {
                        feasible = false;
                        break;
                    }

                } while (!feasible);

            } else {
                feasible = false;
            }
        } else {
            feasible = false;
        }

        if ( !feasible ) {
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
