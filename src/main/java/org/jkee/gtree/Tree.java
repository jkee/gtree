package org.jkee.gtree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jkee
 */

public class Tree<T> implements Serializable, Iterable<T> {

    private static final long serialVersionUID = 124356L;

    private final T value;
    private List<Tree<T>> chld;

    public Tree(T value) {
        this.value = value;
    }

    public Tree(T value, List<Tree<T>> children) {
        this.value = value;
        chld = children;
    }

    public void addChild(Tree<T> child) {
        if (chld == null) chld = new ArrayList<Tree<T>>();
        chld.add(child);
    }

    public List<Tree<T>> getChildren() {
        return chld;
    }

    public T getValue() {
        return value;
    }

    public <K> Tree<K> map(Function<T, K> f) {
        K transformed = f.apply(value);
        Tree<K> newTree = new Tree<K>(transformed);
        if (chld != null) for (Tree<T> t : chld) {
            newTree.addChild(t.map(f));
        }
        return newTree;
    }

    /**
     * Does not modify source tree
     * Children of filtered nodes will be dropped
     */
    @Nullable
    public Tree<T> filter(Predicate<T> predicate) {
        if (!predicate.apply(value)) return null;
        if (chld == null) return new Tree<T>(value);
        List<Tree<T>> newChilds = new ArrayList<Tree<T>>();
        for (Tree<T> tTree : chld) {
            Tree<T> filtered = tTree.filter(predicate);
            if (filtered != null) newChilds.add(filtered);
        }
        if (newChilds.isEmpty()) return new Tree<T>(value);
        return new Tree<T>(value, newChilds);
    }

    /**
     * Depth-first
     */
    @Override
    public Iterator<T> iterator() {
        if (chld == null) return Iterators.singletonIterator(value);
        //вот она, сила функционального подхода!
        return Iterators.concat(
                Iterators.singletonIterator(value),
                Iterables.concat(chld).iterator()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tree)) return false;

        Tree tree = (Tree) o;

        if (chld != null ? !chld.equals(tree.chld) : tree.chld != null) return false;
        if (!value.equals(tree.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (chld != null ? chld.hashCode() : 0);
        return result;
    }
}