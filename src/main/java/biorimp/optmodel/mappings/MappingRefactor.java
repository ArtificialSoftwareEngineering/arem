package biorimp.optmodel.mappings;

import biorimp.optmodel.mappings.quantum.QubitRefactor;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefParam;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefactoring;

import java.util.List;

public abstract class MappingRefactor {

    protected Refactoring type;
    private OBSERVRefactoring refactor;

    public abstract OBSERVRefactoring mappingRefactor(
            QubitRefactor genome);

    public abstract List<OBSERVRefParam> mappingParams();

    public OBSERVRefactoring getRefactor() {
        return refactor;
    }

    public void setRefactor(OBSERVRefactoring refactor) {
        this.refactor = refactor;
    }

    enum Refactoring {
        pullUpField, moveMethod, replaceMethodObject, replaceDelegationInheritance,
        moveField, extractMethod, pushDownMethod, replaceInheritanceDelegation,
        inlineMethod, pullUpMethod, pushDownField, extractClass
    }


}
