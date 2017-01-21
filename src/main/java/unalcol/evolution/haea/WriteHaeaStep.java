package unalcol.evolution.haea;

import unalcol.io.Write;

import java.io.Writer;

public class WriteHaeaStep<T> extends Write<HaeaStep<T>> {
    @Override
    public void write(HaeaStep<T> step, Writer writer) throws Exception {
        Write.apply(step.operators, writer);
    }
}