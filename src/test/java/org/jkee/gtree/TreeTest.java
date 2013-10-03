package org.jkee.gtree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.jkee.gtree.builder.ParentLinkTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jkee
 */
public class TreeTest {

    static class TestEntity {
        int id;
        TestEntity parent;

        TestEntity(int id, TestEntity parent) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }

    ParentLinkTreeBuilder<TestEntity> builder;
    Tree<TestEntity> testTree;

    TestEntity te1  ;
    TestEntity te12 ;
    TestEntity te13 ;
    TestEntity te14 ;
    TestEntity te131;
    TestEntity te141;
    TestEntity te142;

    @Before
    public void setUp() throws Exception {
        builder = new ParentLinkTreeBuilder<TestEntity>(new ParentLinkTreeBuilder.Funnel<TestEntity>() {
            @Override
            public TestEntity getParent(TestEntity node) {
                return node.parent;
            }
        });
        //Test tree. It should contains nodes with no children, with one, and with multiple
        te1      = new TestEntity(1, null);
        te12     = new TestEntity(12, te1);
        te13     = new TestEntity(13, te1);
        te14     = new TestEntity(14, te1);
        te131    = new TestEntity(131, te13);
        te141    = new TestEntity(141, te14);
        te142    = new TestEntity(142, te14);

        testTree = builder.build(Lists.newArrayList(te1, te12, te13, te14, te131, te141, te142));
        testTree.sort(new Comparator<TestEntity>() {
            @Override
            public int compare(TestEntity o1, TestEntity o2) {
                return Ints.compare(o1.id, o2.id);
            }
        });
    }


    @Test
    public void testIterate() throws Exception {
        //depth-first
        List<TestEntity> required = Lists.newArrayList(te1, te12, te13, te131, te14, te141, te142);
        List<TestEntity> iterateOrder = Lists.newArrayList(testTree);
        assertEquals(required, iterateOrder);

        System.out.println(testTree.toStringTree());
    }

    @Test
    public void testFilter() throws Exception {
        Tree<TestEntity> filtered = testTree.filter(new Predicate<TestEntity>() {
            @Override
            public boolean apply(TestEntity input) {
                return input.id != 13;
            }
        });
        //should drop 13 and 131
        List<TestEntity> required = Lists.newArrayList(te1, te12, te14, te141, te142);
        List<TestEntity> iterateOrder = Lists.newArrayList(filtered);
        assertEquals(required, iterateOrder);
        //should not modify source tree
        List<TestEntity> requiredSrc = Lists.newArrayList(te1, te12, te13, te131, te14, te141, te142);
        List<TestEntity> iterateOrderSrc = Lists.newArrayList(testTree);
        assertEquals(requiredSrc, iterateOrderSrc);

    }

    @Test
    public void testMap() throws Exception {
        Function<TestEntity, String> mapFunction = new Function<TestEntity, String>() {
            @Override
            public String apply(TestEntity input) {
                return Integer.toString(input.id);
            }
        };
        Tree<String> stringTree = testTree.map(mapFunction);
        //string tree
        List<TestEntity> required = Lists.newArrayList(te1, te12, te13, te131, te14, te141, te142);
        List<String> strings = Lists.transform(required, mapFunction);
        List<String> iterateOrder = Lists.newArrayList(stringTree);
        assertEquals(strings, iterateOrder);
        //should not modify source tree
        List<TestEntity> iterateOrderSrc = Lists.newArrayList(testTree);
        assertEquals(required, iterateOrderSrc);

    }
}
