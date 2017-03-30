package biorimp.optmodel.space.feasibility;

import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.CodeObjState;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringParameter;

import java.util.ArrayList;
import java.util.List;

public final class InspectRefactor {
    public InspectRefactor() {

    }

    /**
     * @param value_mtd
     * @param sysType_src
     * @return
     */
    public static boolean inspectMethodNotConstructor(List<String> value_mtd,
                                                      TypeDeclaration sysType_src) {
        return !value_mtd.get(0).equals(sysType_src.getName());
    }

    /**
     * @param value_mtd
     * @param sysType_src
     * @return
     */
    public static boolean inspectOverrideParents(List<String> value_mtd, TypeDeclaration sysType_src) {
        boolean feasible = true;
        if (MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()) != null)
            if (!MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                for (TypeDeclaration clase : MetaphorCode.getBuilder().getParentClasses().get(sysType_src.getQualifiedName())) {
                    if (MetaphorCode.getMethodsFromClass(clase) != null)
                        if (!MetaphorCode.getMethodsFromClass(clase).isEmpty()) {
                            for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                if (method.equals(value_mtd.get(0))) {
                                    feasible = false;
                                    break;
                                }
                            }
                        }
                }
            }
        return feasible;
    }

    /**
     * @param value_mtd
     * @param sysType_src
     * @param sysType_tgt
     * @return
     */
    public static boolean inspectHierarchyOverrideParents(List<String> value_mtd, TypeDeclaration sysType_src, TypeDeclaration sysType_tgt) {
        boolean feasible = true;
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
        return feasible;
    }

    public static boolean inspectOverrideChildren(List<String> value_mtd, TypeDeclaration sysType_src) {
        boolean feasible = true;
        if (MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()) != null)
            if (!MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName()).isEmpty()) {
                for (TypeDeclaration clase : MetaphorCode.getBuilder().getChildClasses().get(sysType_src.getQualifiedName())) {
                    if (MetaphorCode.getMethodsFromClass(clase) != null)
                        if (!MetaphorCode.getMethodsFromClass(clase).isEmpty()) {
                            for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                if (method.equals(value_mtd.get(0))) {
                                    feasible = false;
                                    break;
                                }
                            }
                        }
                }
            }
        return feasible;
    }

    public static boolean inspectHierarchyOverrideChildren(List<String> value_mtd, TypeDeclaration sysType_src, TypeDeclaration sysType_tgt) {
        boolean feasible = true;
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
        return feasible;
    }

    /**
     * Verify if the class has override parents or children
     * True for NO overriding
     * False for overriding
     * @return
     */

    public static boolean nonOverrideVerificationParentsAndChildren(List<String> value_src,
                                                                    List<String> value_mtd){
        boolean feasible = true;
        for (String src_type : value_src) {
            //Override verification parents
            if (MetaphorCode.getBuilder().getParentClasses().get(src_type) != null)
                if (!MetaphorCode.getBuilder().getParentClasses().get(src_type).isEmpty()) {
                    for (TypeDeclaration clase : MetaphorCode.getBuilder().getParentClasses().get(src_type)) {
                        if (MetaphorCode.getMethodsFromClass(clase) != null)
                            if (!MetaphorCode.getMethodsFromClass(clase).isEmpty()) {
                                for (String method : MetaphorCode.getMethodsFromClass(clase)) {
                                    if (method.equals(value_mtd.get(0))) {
                                        feasible = false;
                                        break;
                                    }
                                }
                            }
                    }
                }

            if (feasible) {
                //Override verification children
                if (MetaphorCode.getBuilder().getChildClasses().get(src_type) != null)
                    if (!MetaphorCode.getBuilder().getChildClasses().get(src_type).isEmpty()) {
                        for (TypeDeclaration clase_child : MetaphorCode.getBuilder().getChildClasses().get(src_type)) {
                            if (MetaphorCode.getMethodsFromClass(clase_child) != null)
                                if (!MetaphorCode.getMethodsFromClass(clase_child).isEmpty()) {
                                    for (String method : MetaphorCode.getMethodsFromClass(clase_child)) {
                                        if (method.equals(value_mtd.get(0))) {
                                            feasible = false;
                                            break;
                                        }
                                    }
                                }
                        }
                    }
            }
        }
        return feasible;
    }

}
