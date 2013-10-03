package org.jkee.gtree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author jkee
 */

public class Forest<T> extends TreeLike<T, Forest<T>> {

    private List<Tree<T>> chld;

    public Forest(List<Tree<T>> chld) {
        this.chld = chld;
    }

    @Override
    public Iterator<Tree<T>> treeIterator() {
        Iterator<Iterator<Tree<T>>> treeIterators = Iterators.transform(chld.iterator(), new Function<Tree<T>, Iterator<Tree<T>>>() {
            @Override
            public Iterator<Tree<T>> apply(Tree<T> input) {
                return input.treeIterator();
            }
        });
        return Iterators.concat(treeIterators);
    }

    @Override
    public void sort(Comparator<T> comparator) {
        for (Tree<T> ts : chld) {
            ts.sort(comparator);
        }
    }

    @Override
    public Forest<T> filter(Predicate<T> predicate) {
        List<Tree<T>> newList = Lists.newArrayList();
        for (Tree<T> ts : chld) {
            Tree<T> filtered = ts.filter(predicate);
            if (filtered != null) newList.add(filtered);
        }
        return new Forest<T>(newList);
    }

    @Override
    protected void appendToString(StringBuilder sb, int depth) {
        for (int i = 0; i < chld.size(); i++) {
            if (i != 0) sb.append(System.getProperty("line.separator"));
            chld.get(i).appendToString(sb, depth);
        }
    }

    /**
     *
     * Maps (transforms) forest to another value types.
     * @param f mapping function
     * @param <K> output type
     * @return mapped tree
     */
    public <K> Forest<K> map(final Function<T, K> f) {
        return new Forest<K>(Lists.newArrayList(Lists.transform(chld, new Function<Tree<T>, Tree<K>>() {
            @Override
            public Tree<K> apply(org.jkee.gtree.Tree<T> input) {
                return input.map(f);
            }
        })));
    }

    /**
     * Alias to {@link Tree#map(com.google.common.base.Function)}
     */
    public <K> Forest<K> transform(Function<T, K> f) {
        return map(f);
    }

    /**
     * Provides ability to map tree nodes using additional info - parents, children
     * @param f mapping function
     * @param <K> output type
     * @return mapped tree
     */
    public <K> Forest<K> mapTrees(final Function<Tree<T>, K> f) {
        return new Forest<K>(Lists.newArrayList(Lists.transform(chld, new Function<Tree<T>, Tree<K>>() {
            @Override
            public Tree<K> apply(org.jkee.gtree.Tree<T> input) {
                return input.mapTrees(f);
            }
        })));
    }

    public List<Tree<T>> getChld() {
        return chld;
    }

    public int size() {
        return chld.size();
    }

    public boolean isEmpty() {
        return chld.isEmpty();
    }

    public boolean add(Tree<T> ts) {
        return chld.add(ts);
    }

    public Tree<T> get(int index) {
        return chld.get(index);
    }
}
