package org.jkee.gtree;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.jkee.gtree.builder.ParentLinkTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.jkee.gtree.TreeTest.TestEntity;
import static org.junit.Assert.assertEquals;

/**
 * @author jkee
 */

public class ForestTest {

    ParentLinkTreeBuilder<TestEntity> builder;

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

    }

    @Test
    public void testIterate() throws Exception {
        Tree<TestEntity> testTree = builder.build(Lists.newArrayList(te1, te12, te13, te14, te131, te141, te142));
        testTree.sort(new Comparator<TestEntity>() {
            @Override
            public int compare(TestEntity o1, TestEntity o2) {
                return Ints.compare(o1.id, o2.id);
            }
        });
        Tree<TestEntity> testTree2 = builder.build(Lists.newArrayList(te1, te12, te13, te14, te131, te141, te142));
        testTree2.sort(new Comparator<TestEntity>() {
            @Override
            public int compare(TestEntity o1, TestEntity o2) {
                return Ints.compare(o1.id, o2.id);
            }
        });
        Forest<TestEntity> forest = new Forest<TestEntity>(Lists.newArrayList(testTree, testTree2));
        List<TestEntity> required = Lists.newArrayList(
                te1, te12, te13, te131, te14, te141, te142,
                te1, te12, te13, te131, te14, te141, te142
        );
        List<TestEntity> iterateOrder = Lists.newArrayList(forest);
        assertEquals(required, iterateOrder);
    }
}
