package org.jkee.gtree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Tree or forest
 * Iterable
 *
 * Define some access operations
 *
 * Can't put {@link Tree#mapTrees(com.google.common.base.Function)} here due to java type system limitations
 *
 * @author jkee
 */

public abstract class TreeLike<T, R extends TreeLike<T, R>> implements Serializable, Iterable<T> {

    private static final long serialVersionUID = 124356L;

    // ::::: Access operations

    public List<T> find(Predicate<T> predicate) {
        List<T> builder = new ArrayList<T>();
        for (T t : this) {
            if (predicate.apply(t)) builder.add(t);
        }
        return builder;
    }

    public T findOne(Predicate<T> predicate) {
        for (T t : this) {
            if (predicate.apply(t)) return t;
        }
        return null;
    }


    public abstract Iterator<Tree<T>> treeIterator();

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(treeIterator(), new Function<Tree<T>, T>() {
            @Override
            public T apply(Tree<T> input) {
                return input.getValue();
            }
        });
    }

    // ::::: Modification operations

    /**
     * Sorts each node list
     * @param comparator sorting comparator
     */
    public abstract void sort(final Comparator<T> comparator);

    /**
     * Does not modify source tree
     * Children of filtered nodes will be dropped
     */
    public abstract R filter(Predicate<T> predicate);

    public String toStringTree() {
        StringBuilder sb = new StringBuilder();
        appendToString(sb, 0);
        return sb.toString();
    }

    protected abstract void appendToString(StringBuilder sb, int depth);

}