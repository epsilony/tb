/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractDifferentiableFunction implements DifferentiableFunction<double[], double[]>{
    int diffOrder;
    
    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        this.diffOrder = diffOrder;
    }
    
}
