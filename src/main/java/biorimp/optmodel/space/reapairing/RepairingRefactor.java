package biorimp.optmodel.space.reapairing;


import biorimp.optmodel.mappings.metaphor.MetaphorCode;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.CodeObjState;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringOperation;
import unalcol.random.integer.IntUniform;

import java.util.List;

/**
 * Created by david on 5/11/16.
 */
public class RepairingRefactor {

    /**
     * Extracting a Target Class From a Refactoring
     *
     * @param ref is a refactoring operation
     * @return the typedeclaration of the class
     */
    public static TypeDeclaration extractTGTforRepairing(RefactoringOperation ref) {
        TypeDeclaration sysType_tgt;
        int break_point = MetaphorCode.getMapClass().size(); //Number of Classes
        IntUniform g = new IntUniform(break_point);
        int counter = 0;
        do {
            if (ref.getParams() != null) {
                if (ref.getParams().get("tgt") != null) {
                    if (!ref.getParams().get("tgt").isEmpty()) {
                        //New class verification in tgt class
                        if (ref.getParams().get("tgt").get(0).getObjState().equals(CodeObjState.NEW)) {
                            sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                        } else {
                            // 2. Real extraction from the refactoring
                            // If the tgt is fixed then counter variable has the same value
                            // as the breakpoint.
                            // Assumes the first tgt class of a set of classes
                            sysType_tgt = (TypeDeclaration) ref.getParams().get("tgt").get(0).getCodeObj();
                            counter = break_point;
                        }
                    } else {
                        sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                    }
                } else {
                    sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
                }
            } else {
                sysType_tgt = MetaphorCode.getMapClass().get(g.generate());
            }
        } while (++counter < break_point);
        return sysType_tgt;
    }

    public static TypeDeclaration extractSRCforRepairing(RefactoringOperation ref) {
        TypeDeclaration sysType_src;
        int break_point = MetaphorCode.getMapClass().size(); //Number of Classes
        IntUniform g = new IntUniform(break_point);
        int counter = 0;

        do {
            if (ref.getParams() != null) {
                if (ref.getParams().get("src") != null) {
                    if (!ref.getParams().get("src").isEmpty()) {
                        //New class verification in src class
                        if (ref.getParams().get("src").get(0).getObjState().equals(CodeObjState.NEW)){
                            sysType_src = MetaphorCode.getMapClass().get(g.generate());}
                        else{
                            //Assumes the first src class of a set of classes
                            sysType_src = (TypeDeclaration) ref.getParams().get("src").get(0).getCodeObj();
                            counter = break_point;
                        }
                    } else {
                        sysType_src = MetaphorCode.getMapClass().get(g.generate());
                    }
                } else {
                    sysType_src = MetaphorCode.getMapClass().get(g.generate());
                }
            } else {
                sysType_src = MetaphorCode.getMapClass().get(g.generate());
            }
        } while (++counter < break_point);
        return sysType_src;
    }


}
