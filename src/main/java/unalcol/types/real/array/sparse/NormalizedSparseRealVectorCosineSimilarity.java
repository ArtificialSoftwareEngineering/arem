package unalcol.types.real.array.sparse;

import unalcol.math.metric.Simmilarity;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author jgomez
 */
public class NormalizedSparseRealVectorCosineSimilarity implements Simmilarity<SparseRealVector> {
    protected SparseRealVectorDotProduct prod = new SparseRealVectorDotProduct();

    @Override
    public double apply(SparseRealVector x, SparseRealVector y) {
        return prod.apply(x, y);
    }

    public double max(SparseRealVector x) {
        return 1.0;
    }
}
