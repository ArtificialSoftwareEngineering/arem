/**
 *
 */
package biorimp.optmodel.mappings.metaphor;

import biorimp.controller.RefactoringReaderBIoRIMP;
import biorimp.storage.entities.RefKey;
import biorimp.storage.entities.Register;
import biorimp.storage.repositories.RegisterRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.wayne.cs.severe.redress2.controller.HierarchyBuilder;
import edu.wayne.cs.severe.redress2.controller.MetricUtils;
import edu.wayne.cs.severe.redress2.controller.metric.CodeMetric;
import edu.wayne.cs.severe.redress2.entity.ProgLang;
import edu.wayne.cs.severe.redress2.entity.TypeDeclaration;
import edu.wayne.cs.severe.redress2.io.MetricsReader;
import edu.wayne.cs.severe.redress2.main.MainPredFormulasBIoRIPM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import com.google.common.base.MoreObjects;

/**
 * @author Daavid
 */
public final class MetaphorCode {

    private final static int DECREASE = 0;
    private final static double penaltyReGeneration = 0.02;
    private final static double penaltyRepair = 0.01;
    public static Logger LOGGER = LoggerFactory
            .getLogger(MetaphorCode.class);
    protected static RefactoringReaderBIoRIMP refactorReader;
    private static HierarchyBuilder builder;
    private static List<TypeDeclaration> sysTypeDcls;
    private static HashMap<Integer, TypeDeclaration> mapClass =
            new HashMap<Integer, TypeDeclaration>();
    private static HashMap<Integer, TypeDeclaration> mapNewClass =
            new HashMap<Integer, TypeDeclaration>();
    private static ProgLang lang;
    private static ArrayList<CodeMetric> metrics;
    private static File systemPath;
    private static String sysName;
    private static int COUNTER = 0;
    private static LinkedHashMap<String, LinkedHashMap<String, Double>> prevMetrics;

    public MetaphorCode(MainPredFormulasBIoRIPM init) {
        this.systemPath = init.getSystemPath();
        this.sysName = init.getSysName();

        this.sysTypeDcls = init.getSysTypeDcls();
        this.builder = init.getBuilder();
        this.lang = init.getLang();
        this.metrics = init.getMetrics();
        bitAssignerClass();
        previousMetricsCalculation();
    }

    public static double getPenaltyReGeneration() {
        return penaltyReGeneration;
    }

    public static double getPenaltyRepair() {
        return penaltyRepair;
    }

    public static RefactoringReaderBIoRIMP getRefactorReader() {
        if (refactorReader == null)
            refactorReader = new RefactoringReaderBIoRIMP();
        return refactorReader;

    }

    public static int getDECREASE() {
        return DECREASE;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, Double>> getPrevMetrics() {
        return prevMetrics;
    }

    //Get the complete list of Methods of a specific class
    public static LinkedHashSet<String> getMethodsFromClass(TypeDeclaration typeDcl) {
        LinkedHashSet<String> methods = new LinkedHashSet<String>();
        try {
            methods = MetricUtils.getMethods(typeDcl);

        } catch (Exception e) {
            MetaphorCode.LOGGER.error("Error for class (in Methaphor): " + typeDcl.getQualifiedName()
                    + " - " + e.getMessage());
            methods = null;
        }

        return methods;

    }

    //Get the complete list of Fields of a specific class
    public static HashSet<String> getFieldsFromClass(TypeDeclaration typeDcl) {
        HashSet<String> fields = new HashSet<String>();
        try {
            fields = MetricUtils.getFields(typeDcl);

        } catch (Exception e) {
            MetaphorCode.LOGGER.error("Error for class (in Methaphor): " + typeDcl.getQualifiedName()
                    + " - " + e.getMessage());
            fields = null;
        }

        return fields;

    }

    public static HierarchyBuilder getBuilder() {
        return builder;
    }

    public static List<TypeDeclaration> getSysTypeDcls() {
        return sysTypeDcls;
    }

    public static ProgLang getLang() {
        return lang;
    }

    public static HashMap<Integer, TypeDeclaration> getMapClass() {
        return mapClass;
    }

    public void setMapClass(HashMap<Integer, TypeDeclaration> mapClass) {
        this.mapClass = mapClass;
    }

    public static ArrayList<CodeMetric> getMetrics() {
        return metrics;
    }

    public static File getSystemPath() {
        return systemPath;
    }

    public static String getSysName() {
        return sysName;
    }

    public static LoadingCache<RefKey, List<Register>> RefactoringCache() {
        //create a cache for refactorings based on their tgt,src,fld,mth and refid
        LoadingCache<RefKey, List<Register>> refactoringCache = CacheBuilder.newBuilder()
                .maximumSize(1000) // maximum 1000000 records can be cached
                .expireAfterAccess(2, TimeUnit.MINUTES) // cache will expire after 30 minutes of access
                .build(
                        new CacheLoader<RefKey, List<Register>>() { // build the cacheloader
                            @Override
                            public List<Register> load(RefKey refKey) throws Exception {
                                //make the expensive call
                                RegisterRepository repo = RegisterRepository.getInstance();
                                return repo.getRegistersByClass(refKey);
                            }
                        }
                );

        return refactoringCache;

    }

    //Method for assigning a bit representation to each Class
    private void bitAssignerClass() {
        //BitArray array;
        int i = 0;
        for (TypeDeclaration typeDcl : sysTypeDcls) {
            //array = new BitArray(tamBitArray,false);
            //BitArrayConverter.setNumber(array, 0, tamBitArray, i++); //set number inside bitarray
            typeDcl.setId(i);
            this.mapClass.put(i++, typeDcl);
        }
    }

    private void previousMetricsCalculation() {
        MetaphorCode.LOGGER.info("READING previous metrics");
        MetricsReader metReader = new MetricsReader(getSystemPath(), getSysName());
        try {
            prevMetrics = metReader.readMetrics();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Method for adding a class into the HashMap
    public void addClasstoHash(String pack, String name) {
        this.mapNewClass.put(COUNTER++,
                new TypeDeclaration(pack, name));
    }

}
