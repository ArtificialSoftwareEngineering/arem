package edu.wayne.cs.severe.redress2.entity.refactoring.opers;

import edu.wayne.cs.severe.redress2.controller.HierarchyBuilder;
import edu.wayne.cs.severe.redress2.controller.metric.*;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.entity.refactoring.RefactoringParameter;
import edu.wayne.cs.severe.redress2.entity.refactoring.formulas.rid.*;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.JSONRefParam;
import edu.wayne.cs.severe.redress2.entity.refactoring.json.OBSERVRefParam;
import edu.wayne.cs.severe.redress2.exception.RefactoringException;
import edu.wayne.cs.severe.redress2.utils.RefactoringUtils;

import java.util.HashMap;
import java.util.List;

public class ReplaceInheritanceDelegation extends RefactoringType {

    public ReplaceInheritanceDelegation(List<TypeDeclaration> sysTypeDcls, HierarchyBuilder builder) {
        super(sysTypeDcls);

        formulas.put(new LOCMetric().getMetricAcronym(),
                new LOCReplaceInhDelegPF());
        formulas.put(new DITMetric().getMetricAcronym(),
                new DITReplaceInhDelegPF(builder));
        formulas.put(new LCOM5Metric().getMetricAcronym(),
                new LCOM5ReplaceInhDelegPF());
        formulas.put(new LCOM2Metric().getMetricAcronym(),
                new LCOM2ReplaceInhDelegPF());
        formulas.put(new NOCMetric().getMetricAcronym(),
                new NOCReplaceInhDelegPF());
        formulas.put(new DACMetric().getMetricAcronym(),
                new DACReplaceInhDelegPF());
    }

    @Override
    public String getAcronym() {
        return "RID";
    }

    @Override
    public HashMap<String, List<RefactoringParameter>> getRefactoringParams(
            List<JSONRefParam> jsonParams) throws RefactoringException {

        String srcKey = "src";
        String tgtKey = "tgt";

        HashMap<String, JSONRefParam> idxParams = RefactoringUtils
                .validateJsonParams(jsonParams, 2, new String[]{srcKey,
                        tgtKey}, new int[]{1, 1});

        HashMap<String, List<RefactoringParameter>> params = new HashMap<String, List<RefactoringParameter>>();
        JSONRefParam jsonParam = idxParams.get(srcKey);
        List<RefactoringParameter> refParams = RefactoringUtils
                .getOpersCodeObject(jsonParam, sysTypeDcls,
                        TypeDeclaration.class);
        params.put(srcKey, refParams);

        jsonParam = idxParams.get(tgtKey);
        refParams = RefactoringUtils.getOpersCodeObject(jsonParam, sysTypeDcls,
                TypeDeclaration.class);
        params.put(tgtKey, refParams);

        return params;
    }


    //danaderp vers 1000
    @Override
    public HashMap<String, List<RefactoringParameter>> getOBSERVRefactoringParams(List<OBSERVRefParam> jsonParams)
            throws RefactoringException {
        // TODO Auto-generated method stub
        String srcKey = "src";
        String tgtKey = "tgt";

        HashMap<String, OBSERVRefParam> idxParams = RefactoringUtils
                .validateObservParams(jsonParams, 2, new String[]{srcKey,
                        tgtKey}, new int[]{1, 1});

        HashMap<String, List<RefactoringParameter>> params = new HashMap<String, List<RefactoringParameter>>();
        OBSERVRefParam jsonParam = idxParams.get(srcKey);
        List<RefactoringParameter> refParams = RefactoringUtils
                .getOpersCodeObject(jsonParam, sysTypeDcls,
                        TypeDeclaration.class);
        params.put(srcKey, refParams);

        jsonParam = idxParams.get(tgtKey);
        refParams = RefactoringUtils.getOpersCodeObject(jsonParam, sysTypeDcls,
                TypeDeclaration.class);
        params.put(tgtKey, refParams);

        return params;
    }

}
