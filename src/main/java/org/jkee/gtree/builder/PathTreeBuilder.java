package org.jkee.gtree.builder;

import com.google.common.collect.Maps;
import org.jkee.gtree.Forest;
import org.jkee.gtree.Tree;

import java.util.*;

/**
 * If you can define element -> path relation, this builder might be helpful
 * @author jkee
 */

public class PathTreeBuilder<T, E> {

    public static interface Funnel<T, E> {
        public List<E> getPath(T value);
    }

    private final Funnel<T, E> funnel;

    public PathTreeBuilder(Funnel<T, E> funnel) {
        this.funnel = funnel;
    }

    public Forest<T> build(Iterable<T> values) {
        /*
        * 1. split by cardinality
        * 2. from low cardinality to high: append nodes
        * */

        Map<Integer, Map<List<E>, T>> byCardinality = Maps.newTreeMap();
        for (T value : values) {
            List<E> path = funnel.getPath(value);
            int cardinality = path.size();
            if (!byCardinality.containsKey(cardinality)) {
                byCardinality.put(cardinality, new LinkedHashMap<List<E>, T>());
            }
            //todo handle collision
            byCardinality.get(cardinality).put(path, value);
        }

        List<Tree<T>> roots = new ArrayList<Tree<T>>();
        Map<Integer, Map<List<E>, Tree<T>>> treesByCardinality = Maps.newHashMap();
        for (Map<List<E>, T> level : byCardinality.values()) {
            for (Map.Entry<List<E>, T> entry : level.entrySet()) {
                List<E> path = entry.getKey();
                T value = entry.getValue();
                Tree<T> valueTree = new Tree<T>(value);
                //searching parent
                boolean parentFound = false;
                for (int prefixCardinality = path.size() - 1; prefixCardinality > 0; prefixCardinality--) {
                    List<E> prefix = path.subList(0, prefixCardinality);
                    if (treesByCardinality.containsKey(prefixCardinality)) {
                        Map<List<E>, Tree<T>> rootsForCardinality = treesByCardinality.get(prefixCardinality);
                        if (rootsForCardinality.containsKey(prefix)) {
                            rootsForCardinality.get(prefix).addChild(valueTree);
                            parentFound = true;
                            break;
                        }
                    }
                }
                if (!parentFound) roots.add(valueTree);

                if (!treesByCardinality.containsKey(path.size())) {
                    treesByCardinality.put(path.size(), new LinkedHashMap<List<E>, Tree<T>>());
                }
                //todo handle collision
                treesByCardinality.get(path.size()).put(path, valueTree);
            }
        }

        return new Forest<T>(roots);
    }


}
