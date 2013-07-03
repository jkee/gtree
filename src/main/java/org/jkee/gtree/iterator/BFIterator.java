package org.jkee.gtree.iterator;

import org.jkee.gtree.Tree;

import java.util.Iterator;

/**
 * IDDF
 * http://en.wikipedia.org/wiki/Iterative_deepening_depth-first_search
 * @author jkee
 */
public class BFIterator<T> implements Iterator<T> {

    private final Tree<T> root;
    private LevelBFIterator<T> currentIterator;

    public BFIterator(Tree<T> root) {
        this.root = root;
        currentIterator = new LevelBFIterator<T>(root, 0);
    }

    @Override
    public boolean hasNext() {
        return currentIterator.hasNext();
    }

    @Override
    public T next() {
        T next = currentIterator.next();
        if (!currentIterator.hasNext()) {
            int nextLevel = currentIterator.level + 1;
            currentIterator = new LevelBFIterator<T>(root, nextLevel);
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
