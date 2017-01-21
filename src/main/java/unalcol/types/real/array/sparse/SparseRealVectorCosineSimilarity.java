package unalcol.types.real.array.sparse;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author jgomez
 */
public class SparseRealVectorCosineSimilarity extends NormalizedSparseRealVectorCosineSimilarity {
    @Override
    public double apply(SparseRealVector x, SparseRealVector y) {
        return super.apply(x, y) / (prod.norm(x) * prod.norm(y));
    }
}
