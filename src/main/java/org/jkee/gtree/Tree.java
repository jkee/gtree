package org.jkee.gtree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.jkee.gtree.iterator.DFIterator;

import java.io.Serializable;
import java.util.*;

/**
 * Generic tree implementation
 * Iterable
 * Supports mapping, filtering, sorting, pretty-printing and more
 *
 * @author jkee
 */

public class Tree<T> implements Serializable, Iterable<T> {

    private static final long serialVersionUID = 124356L;

    private final T value;
    //nullable (if root)
    private Tree<T> parent;
    //nullable (if no children)
    private List<Tree<T>> chld;

    public Tree(T value) {
        this.value = value;
    }

    public Tree(T value, List<Tree<T>> children) {
        this.value = value;
        chld = children;
    }

    // ::::: Modification operations

    /**
     *
     * Maps (transforms) tree to another value types.
     * @param f mapping function
     * @param <K> output type
     * @return mapped tree
     */
    public <K> Tree<K> map(Function<T, K> f) {
        K transformed = f.apply(value);
        Tree<K> newTree = new Tree<K>(transformed);
        if (chld != null) for (Tree<T> t : chld) {
            Tree<K> mapped = t.map(f);
            mapped.setParent(newTree);
            newTree.addChild(mapped);
        }
        return newTree;
    }

    /**
     * Sorts each node list
     * @param comparator sorting comparator
     */
    public void sort(final Comparator<T> comparator) {
        if (chld != null) {
            Collections.sort(chld, new Comparator<Tree<T>>() {
                @Override
                public int compare(Tree<T> o1, Tree<T> o2) {
                    return comparator.compare(o1.value, o2.value);
                }
            });
            for (Tree<T> child : chld) {
                child.sort(comparator);
            }
        }
    }

    /**
     * Does not modify source tree
     * Children of filtered nodes will be dropped
     */
    public Tree<T> filter(Predicate<T> predicate) {
        if (!predicate.apply(value)) return null;
        Tree<T> newTree = new Tree<T>(value);
        if (chld == null) return newTree;
        List<Tree<T>> newChilds = new ArrayList<Tree<T>>();
        for (Tree<T> tTree : chld) {
            Tree<T> filtered = tTree.filter(predicate);
            if (filtered != null) {
                filtered.setParent(newTree);
                newChilds.add(filtered);
            }
        }
        if (newChilds.isEmpty()) return newTree;
        newTree.chld = newChilds;
        return newTree;
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

    public Tree<T> getParent() {
        return parent;
    }

    public void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    /**
     * Depth-first
     */
    @Override
    public Iterator<T> iterator() {
        return new DFIterator<T>(this);
    }

    /**
     * Breadth-first
     */
    /*public Iterator<T> breadthFirstIterator() {
        return new BFIterator();
    }*/

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

    @Override
    public String toString() {
        return "node{" +
                value +
                '}';
    }

    public String toStringTree() {
        StringBuilder sb = new StringBuilder();
        appendToString(sb, 0);
        return sb.toString();
    }

    private void appendToString(StringBuilder sb, int depth) {
        if (depth != 0) sb.append(System.getProperty("line.separator"));
        for (int i = 0; i < depth; i++) {
            sb.append('\t');
        }
        sb.append(value);
        if (chld != null) for (Tree<T> ts : chld) {
            ts.appendToString(sb, depth + 1);
        }
    }

}