package org.jkee.gtree.builder;

import com.google.common.collect.Lists;
import org.jkee.gtree.Tree;

import java.util.List;
import java.util.Map;

/**
 * Simple builder, requires hashing and parent extracting {@link Funnel}
 * Stateless
 * Actually using {@link KeyTreeBuilder} to build, using T as a link
 *
 * @author jkee
 */
public class ParentLinkTreeBuilder<T> {

    public static interface Funnel<T> {
        public T getParent(T node);
    }

    private final Funnel<T> funnel;

    public ParentLinkTreeBuilder(Funnel<T> funnel) {
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
    public List<Tree<T>> buildForest(Iterable<? extends T> values) {
        KeyTreeBuilder<T, T> keyTreeBuilder = new KeyTreeBuilder<T, T>(new KeyTreeBuilder.Funnel<T, T>() {
            @Override
            public T getKey(T node) {
                return node;
            }

            @Override
            public T getParentKey(T node) {
                return funnel.getParent(node);
            }
        });
        KeyTreeBuilder.BuildResult<T, T> result = keyTreeBuilder.build(values);
        Map<T, Tree<T>> roots = result.getRoots();
        if (roots.isEmpty())
            throw new IllegalStateException("No roots found, cycle links? Values size: " + result.getAll().size());
        return Lists.newArrayList(roots.values());
    }


}
