package org.jkee.gtree.examples;

import com.google.common.collect.Lists;
import org.jkee.gtree.Tree;
import org.jkee.gtree.builder.KeyTreeBuilder;

import java.util.List;

/**
 * This example shows how to build a tree from simple id -> parentId mapping
 *
 * It takes integer as key and assume 0 as no parent
 *
 * @author jkee
 */

public class ParentIdTree {

    public static class Entity {
        public final int id;
        public final int parentId;
        public String somePayload;

        public Entity(int id, int parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "id=" + id +
                    ", parentId=" + parentId +
                    '}';
        }
    }

    public static void main(String[] args) {
        Entity te1      = new Entity(1, 0);
        Entity te12     = new Entity(12, 1);
        Entity te13     = new Entity(13, 1);
        Entity te14     = new Entity(14, 1);
        Entity te131    = new Entity(131, 13);
        Entity te141    = new Entity(141, 14);
        Entity te142    = new Entity(142, 14);

        List<Entity> entities = Lists.newArrayList(te1, te12, te13, te14, te131, te141, te142);

        KeyTreeBuilder.Funnel<Integer, Entity> funnel = new KeyTreeBuilder.Funnel<Integer, Entity>() {
            @Override
            public Integer getKey(Entity node) {
                return node.id;
            }

            @Override
            public Integer getParentKey(Entity node) {
                //so lets take 0 as no parent
                if (node.parentId == 0) return null;
                return node.parentId;
            }
        };

        KeyTreeBuilder<Integer, Entity> builder = new KeyTreeBuilder<Integer, Entity>(funnel);
        Tree<Entity> tree = builder.buildTree(entities);
        System.out.println(tree);

    }

}
