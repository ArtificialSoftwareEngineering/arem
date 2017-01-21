/**
 *
 */
package biorimp.optmodel.space.generation;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import biorimp.optmodel.space.Refactoring;
import biorimp.optmodel.space.feasibility.InspectRefactor;
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

                //Creating the OBSERVRefParam for the src class
                sysType_src = MetaphorCode.getMapClass().get(g.generate());
                value_src = new ArrayList<String>();
                value_src.add(sysType_src.getQualifiedName());


                //Creating the OBSERVRefParam for the mtd class
                value_mtd = new ArrayList<String>();
                if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {
                    IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

                    value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                            [numMtdObs.generate()]);

                    //+Verification of method not constructor
                    feasible = InspectRefactor.inspectMethodNotConstructor(value_mtd, sysType_src);

                } else {
                    feasible = false;
                }
            } while (!feasible);

            //Creating the OBSERVRefParam for the tgt
            value_tgt = new ArrayList<String>();
            TypeDeclaration sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
            value_tgt.add(sysType_tgt.getQualifiedName());


            //Override and hierarchy verification parents
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
    public OBSERVRefactoring repairRefactor(RefactoringOperation ref, int break_point) {
        // TODO Auto-generated method stub
        OBSERVRefactoring refRepair = null;
        int counter = 0;
        boolean feasible;
        List<OBSERVRefParam> params;
        IntUniform g = new IntUniform(MetaphorCode.getMapClass().size());
        TypeDeclaration sysType_src;
        List<String> value_mtd;
        List<String> value_src;
        List<String> value_tgt = null;


        do {
            do {
                feasible = true;
                params = new ArrayList<OBSERVRefParam>();

                //Creating the OBSERVRefParam for the src class
                //sysType_src =  code.getMapClass().get( g.generate() );
                if (ref.getParams() != null) {
                    //New class verification in src class
                    if (ref.getParams().get("src").get(0).getObjState().equals(CodeObjState.NEW))
                        sysType_src = MetaphorCode.getMapClass().get(g.generate());
                    else
                        sysType_src = (TypeDeclaration) ref.getParams().get("src").get(0).getCodeObj(); //Assumes the first src class of a set of classes
                } else {
                    sysType_src = MetaphorCode.getMapClass().get(g.generate());
                }

                value_src = new ArrayList<String>();
                value_src.add(sysType_src.getQualifiedName());


                //Creating the OBSERVRefParam for the mtd class
                value_mtd = new ArrayList<String>();
                if (!MetaphorCode.getMethodsFromClass(sysType_src).isEmpty()) {
                    IntUniform numMtdObs = new IntUniform(MetaphorCode.getMethodsFromClass(sysType_src).size());

                    value_mtd.add((String) MetaphorCode.getMethodsFromClass(sysType_src).toArray()
                            [numMtdObs.generate()]);

                    //verification of method not constructor
                    if (value_mtd.get(0).equals(sysType_src.getName()))
                        feasible = false;

                } else {
                    feasible = false;
                    //break;
                }

                counter++;

                if (counter < break_point)
                    break;

            } while (!feasible);

            if (counter < break_point) {
                break;
            } else {

                //Creating the OBSERVRefParam for the tgt
                value_tgt = new ArrayList<String>();
                //TypeDeclaration sysType_tgt = code.getMapClass().get( g.generate() );
                //TypeDeclaration sysType_tgt = (TypeDeclaration) ref.getParams().get("tgt").get(0).getCodeObj();
                TypeDeclaration sysType_tgt = null;
                if (ref.getParams() != null) {
                    if (ref.getParams().get("tgt") != null) {
                        if (!ref.getParams().get("tgt").isEmpty())
                            //New class verification in tgt class
                            if (ref.getParams().get("tgt").get(0).getObjState().equals(CodeObjState.NEW))
                                sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                            else
                                sysType_tgt = (TypeDeclaration) ref.getParams().get("tgt").get(0).getCodeObj(); //Assumes the first tgt class of a set of classes
                        else
                            sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                    } else {
                        sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                    }
                } else {
                    sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                }
                value_tgt.add(sysType_tgt.getQualifiedName());


                //Override and hierarchy verification parents
                if (MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()) != null)
                    if (!MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                        for (TypeDeclaration clase : MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName())) {
                            if (MetaphorCode.getMethodsFromClass(clase) != null)
                                if (!MetaphorCode.getMethodsFromClass(clase).isEmpty()) {
                                    for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                        if (method.equals(value_mtd.get(0)) || clase.equals(sysType_tgt)) {
                                            feasible = false;
                                            break;
                                        }
                                    }
                                }
                        }
                    }

                if (feasible) {
                    //Override and hierarchy verification children
                    if (MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()) != null)
                        if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                            for (TypeDeclaration clase : MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName())) {
                                if (MetaphorCode.getMethodsFromClass(clase) != null)
                                    if (!MetaphorCode.getMethodsFromClass(clase).isEmpty()) {
                                        for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                            if (method.equals(value_mtd.get(0)) || clase.equals(sysType_tgt)) {
                                                feasible = false;
                                                break;
                                            }
                                        }
                                    }
                            }
                        }
                }
            }


        } while (!feasible);

        if (!feasible || counter < break_point) {
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