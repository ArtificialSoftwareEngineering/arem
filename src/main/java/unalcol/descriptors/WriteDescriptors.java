package unalcol.descriptors;

import unalcol.io.Write;

import java.io.Writer;

/**
 * <p>Writer of object descriptors.</p>
 * <p>
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author Jonatan Gomez Perdomo
 * @version 1.0
 */
public class WriteDescriptors extends Write<Object> {
    @Override
    public void write(Object obj, Writer writer) throws Exception {
        double[] d = Descriptors.obtain(obj);
        Write.apply(d, writer);
    }
}