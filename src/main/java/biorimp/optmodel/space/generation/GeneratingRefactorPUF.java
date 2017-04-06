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
public class GeneratingRefactorPUF extends GeneratingRefactor {

    protected Refactoring type = Refactoring.pullUpField;


    @Override
    public OBSERVRefactoring generatingRefactor(ArrayList<Double> penalty) {

        boolean feasible;
        int counterPUF = 0; //<-- 1.
        int break_point = MetaphorCode.getClassesWithInheritanceAndFields().size();//Number of Classes
        OBSERVRefactoring refRepair = null;

        IntUniform g = new IntUniform(break_point);
        IntUniform numFldObs;
        TypeDeclaration sysType_src;
        TypeDeclaration sysType_tgt;
        List<String> value_src;
        List<String> value_fld = null;
        List<String> value_tgt;


        do {
            feasible = true;

            //2. Creating the OBSERVRefParam for the tgt/super class
            value_tgt = new ArrayList<String>();
            sysType_tgt = MetaphorCode.getClassesWithInheritanceAndFields().get(g.generate());//<--
            value_tgt.add(sysType_tgt.getQualifiedName());

            //Creating the OBSERVRefParam for the src class
            value_src = new ArrayList<String>();

            //3. verification of SRCSubClassTGT
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                List<TypeDeclaration> childrenClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                IntUniform indexClass = new IntUniform(childrenClasses.size());
                //4. RandomlySelectedClass
                sysType_src = childrenClasses.get(indexClass.generate());

                //5. Creating the OBSERVRefParam for the fld field
                value_fld = new ArrayList<String>();
                if (!MetaphorCode.getFieldsFromClass(sysType_src).isEmpty()) {
                    // 6. Pick up a field from the src class
                    numFldObs = new IntUniform(MetaphorCode.getFieldsFromClass(sysType_src).size());
                    value_fld.add((String) MetaphorCode.getFieldsFromClass(sysType_src).toArray()
                            [numFldObs.generate()]);

                    //Identifying other src(s) with the fld
                    for (TypeDeclaration clase : childrenClasses) {
                        for (String field : MetaphorCode.getFieldsFromClass(clase)) {
                            if (field.equals(value_fld.get(0))) {
                                value_src.add(clase.getQualifiedName());
                            }
                        }
                    }

                } else {
                    feasible = false;
                }

            } else {
                feasible = false;
            }

            counterPUF++;
            if (counterPUF > break_point) {
                feasible = false;
                break;
            }
        } while (!feasible);

        if (feasible) {
            List<OBSERVRefParam> params;
            params = new ArrayList<OBSERVRefParam>();
            params.add(new OBSERVRefParam("src", value_src));
            params.add(new OBSERVRefParam("fld", value_fld));
            params.add(new OBSERVRefParam("tgt", value_tgt));
            refRepair = new OBSERVRefactoring(type.name(), params, feasible, penalty);
        }

        return refRepair;
    }

    @Override
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair = null;

        int counterPUF = 0; //<-- 1.


        boolean feasible;
        List<OBSERVRefParam> params;
        TypeDeclaration sysType_src = null;
        TypeDeclaration sysType_tgt = RepairingRefactor.extractTGTforRepairing(ref); //<-2.
        List<String> value_src;
        List<String> value_fld = null;
        List<String> value_tgt;
        IntUniform numFldObs;


        params = new ArrayList<OBSERVRefParam>();
        //Creating the OBSERVRefParam for the tgt/super class

        value_tgt = new ArrayList<String>();
        value_tgt.add(sysType_tgt.getQualifiedName());

        //Creating the OBSERVRefParam for the src class
        value_src = new ArrayList<String>();

        //3. Verification of SRCSubClassTGT
        if (MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()) != null) {
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName()).isEmpty()) {
                //4. Getting Child Classes
                List<TypeDeclaration> childrenClasses = MetaphorCode.getBuilder().getChildClasses().get(sysType_tgt.getQualifiedName());
                IntUniform indexClass = new IntUniform(childrenClasses.size());
                int break_point = childrenClasses.size();//Number of Classes
                do {
                    feasible = true;
                    //5. RandomlySelectedClass
                    sysType_src = childrenClasses.get(indexClass.generate());

                    //6. Creating the OBSERVRefParam for the fld field
                    value_fld = new ArrayList<String>();
                    if (!MetaphorCode.getFieldsFromClass(sysType_src).isEmpty()) {
                        //6. Pick up a field
                        numFldObs = new IntUniform(MetaphorCode.getFieldsFromClass(sysType_src).size());
                        value_fld.add((String) MetaphorCode.getFieldsFromClass(sysType_src).toArray()
                                [numFldObs.generate()]);

                        //7. Identifying other src(s) with the fld
                        for (TypeDeclaration clase : childrenClasses) {
                            for (String field : MetaphorCode.getFieldsFromClass(clase)) {
                                if (field.equals(value_fld.get(0))) {
                                    value_src.add(clase.getQualifiedName());
                                }
                            }
                        }

                    } else {
                        feasible = false;
                    }

                    counterPUF++;
                    if (counterPUF > break_point) {
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
