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
public class NOMMetric extends CodeMetric {

    private static final String ACRONYM = "NOM";
    // logger
    private static Logger LOGGER = LoggerFactory.getLogger(NOMMetric.class);

    public NOMMetric(List<TypeDeclaration> sysTypeDcls, HierarchyBuilder builder) {
        super(sysTypeDcls, builder);
    }

    public NOMMetric() {
    }

    @Override
    public double computeMetric(TypeDeclaration typeDcl) throws MetricException {

        // FIXME: methods with annotations are not correcly counted because
        // srcML has a bug and does not parse well the methods
        try {
            Double numMethods = MetricUtils.getNumberOfMethods(typeDcl, typeDcl
                    .getCompUnit().getSrcFile());
            return numMethods;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            MetricException ex = new MetricException(e.getMessage());
            ExceptionUtils.addStackTrace(e, ex);
            throw ex;
        }
    }

    @Override
    public String getMetricAcronym() {
        return ACRONYM;
    }

}// end NOMMetric