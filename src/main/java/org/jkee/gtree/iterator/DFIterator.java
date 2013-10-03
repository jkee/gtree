package org.jkee.gtree.iterator;

import org.jkee.gtree.Tree;

import java.util.Iterator;

/**
 * Depth-first
 * @author jkee
 */
public class DFIterator<T> implements Iterator<T> {

    TreeDFIterator<T> treeDFIterator;

    public DFIterator(Tree<T> tree) {
        treeDFIterator = new TreeDFIterator<T>(tree);
    }

    @Override
    public boolean hasNext() {
        return treeDFIterator.hasNext();
    }

    @Override
    public T next() {
        return treeDFIterator.next().getValue();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
