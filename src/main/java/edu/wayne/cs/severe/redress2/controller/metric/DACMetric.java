package edu.wayne.cs.severe.redress2.controller.metric;

import edu.wayne.cs.severe.redress2.controller.HierarchyBuilder;
import edu.wayne.cs.severe.redress2.controller.MetricUtils;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.exception.MetricException;
import edu.wayne.cs.severe.redress2.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author ojcchar
 * @version 1.0
 * @created 23-Mar-2014 12:28:47
 */
public class DACMetric extends CodeMetric {

    private static final String ACRONYM = "DAC";
    // logger
    private static Logger LOGGER = LoggerFactory.getLogger(DACMetric.class);

    public DACMetric(List<TypeDeclaration> sysTypeDcls, HierarchyBuilder builder) {
        super(sysTypeDcls, builder);
    }

    public DACMetric() {
    }

    @Override
    public double computeMetric(TypeDeclaration typeDcl) throws MetricException {
        try {

            return MetricUtils.getNumFieldsDiff(typeDcl);

        } catch (Exception e) {
            LOGGER.error("Error for class: " + typeDcl.getQualifiedName()
                    + " - " + e.getMessage(), e);
            MetricException ex = new MetricException(e.getMessage());
            ExceptionUtils.addStackTrace(e, ex);
            throw ex;
        }
    }

    @Override
    public String getMetricAcronym() {
        return ACRONYM;
    }

}// end DACMetric