package org.jkee.gtree.builder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jkee.gtree.Forest;
import org.jkee.gtree.Tree;

import java.util.Map;

/**
 * Builder for key-value relation
 * Useful when you need to build a tree from adjacency list
 * For example, using id and parentId fields in your database
 * Take a look to ParentIdTree example from test sources
 *
 * @author jkee
 */

public class KeyTreeBuilder<K, T> {

    public static interface Funnel<K, T> {
        public K getKey(T node);
        public K getParentKey(T node);
    }

    private final Funnel<K, T> funnel;

    public KeyTreeBuilder(Funnel<K, T> funnel) {
        this.funnel = funnel;
    }

    public Tree<T> buildTree(Iterable<? extends T> values) {
        BuildResult<K, T> result = build(values);
        Map<K, Tree<T>> roots = result.getRoots();
        if (roots.isEmpty())
            throw new IllegalStateException("No roots found, cycle links? Values size: " + result.getAll().size());
        if (roots.size() > 1) throw new IllegalStateException("Multiple roots found, size: " + roots.size());
        return Iterables.getOnlyElement(roots.values());
    }

    public BuildResult<K, T> build(Iterable<? extends T> values) {
        Map<K, Tree<T>> nodes = Maps.newHashMap();
        for (T value : values) {
            nodes.put(funnel.getKey(value), new Tree<T>(value));
        }
        Map<K, Tree<T>> roots = Maps.newHashMap();
        for (Map.Entry<K, Tree<T>> entry : nodes.entrySet()) {
            Tree<T> valueTree = entry.getValue();
            T value = valueTree.getValue();
            K parentKey = funnel.getParentKey(value);
            if (parentKey == null) { //root
                roots.put(entry.getKey(), valueTree);
            } else {
                Tree<T> parentNode = nodes.get(parentKey);
                if (parentNode != null) {
                    parentNode.addChild(valueTree);
                    valueTree.setParent(parentNode);
                }
            }
        }
        return new BuildResult<K, T>(nodes, roots);
    }

    public static class BuildResult<K, T> {
        private final Map<K, Tree<T>> all;
        private final Map<K, Tree<T>> roots;

        BuildResult(Map<K, Tree<T>> all, Map<K, Tree<T>> roots) {
            this.all = all;
            this.roots = roots;
        }

        public Map<K, Tree<T>> getAll() {
            return all;
        }

        public Map<K, Tree<T>> getRoots() {
            return roots;
        }

        public Forest<T> getForest() {
            return new Forest<T>(Lists.newArrayList(roots.values()));
        }
    }

}
