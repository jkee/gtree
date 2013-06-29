package org.jkee.gtree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Simple builder, requires hashing and parent extracting {@link Funnel}
 * Stateless
 *
 * @author jkee
 */
public class SimpleTreeBuilder<T> {

    public static interface Funnel<T> {
        public T getParent(T node);
    }

    private final Funnel<T> funnel;

    public SimpleTreeBuilder(Funnel<T> funnel) {
        this.funnel = funnel;
    }

    /**
     *
     * @param values node values
     * @return built tree
     * @throws IllegalStateException if found no one or more than one root
     */
    public Tree<T> build(Iterable<? extends T> values) {
        List<Tree<T>> roots = buildForest(values);
        if (roots.size() > 1) throw new IllegalStateException("Multiple roots found, size: " + roots.size());
        return roots.get(0);
    }

    /**
     *
     * @param values node values
     * @return list of roots
     * @throws IllegalStateException if no roots found
     */
    private List<Tree<T>> buildForest(Iterable<? extends T> values) {
        Map<T, Tree<T>> nodes = Maps.newHashMap();
        for (T value : values) {
            nodes.put(value, new Tree<T>(value));
        }
        List<Tree<T>> roots = Lists.newArrayList();
        for (Map.Entry<T, Tree<T>> entry : nodes.entrySet()) {
            T value = entry.getKey();
            Tree<T> valueTree = entry.getValue();
            T parent = funnel.getParent(value);
            if (parent == null) { //root
                roots.add(valueTree);
            } else {
                Tree<T> parentNode = nodes.get(parent);
                if (parentNode != null) parentNode.addChild(valueTree);
            }
        }
        if (roots.isEmpty()) throw new IllegalStateException("No roots found, cycle links? Values size: " + nodes.size());
        return roots;
    }


}
