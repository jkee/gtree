package org.jkee.gtree;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.jkee.gtree.builder.SimpleTreeBuilder;
import org.jkee.gtree.iterator.BFIterator;
import org.jkee.gtree.iterator.DFIterator;
import org.jkee.gtree.iterator.LevelBFIterator;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jkee
 */
public class IteratorsTest {

    private static class TestEntity {
        int id;
        TestEntity parent;

        private TestEntity(int id, TestEntity parent) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }

    private SimpleTreeBuilder<TestEntity> builder;
    private Tree<TestEntity> testTree;

    TestEntity te1  ;
    TestEntity te12 ;
    TestEntity te13 ;
    TestEntity te14 ;
    TestEntity te131;
    TestEntity te141;
    TestEntity te142;

    @Before
    public void setUp() throws Exception {
        builder = new SimpleTreeBuilder<TestEntity>(new SimpleTreeBuilder.Funnel<TestEntity>() {
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
    public void testIterateDF() throws Exception {
        //depth-first
        List<TestEntity> required = Lists.newArrayList(te1, te12, te13, te131, te14, te141, te142);
        System.out.println(testTree.toString());

        DFIterator<TestEntity> elements = new DFIterator<TestEntity>(testTree);
        List<TestEntity> iterateBFS = Lists.newArrayList(elements);
        assertEquals(required, iterateBFS);
    }

    @Test
    public void testIterateBF() throws Exception {
        List<TestEntity> required = Lists.newArrayList(te1, te12, te13, te14, te131, te141, te142);
        System.out.println(testTree.toString());

        BFIterator<TestEntity> elements = new BFIterator<TestEntity>(testTree);
        List<TestEntity> iterateBFS = Lists.newArrayList(elements);
        assertEquals(required, iterateBFS);
    }

    @Test
    public void testIterateIDDF() throws Exception {
        {
            LevelBFIterator<TestEntity> elements = new LevelBFIterator<TestEntity>(testTree, -1);
            List<TestEntity> iterateBFS = Lists.newArrayList(elements);
            assertEquals(Lists.<TestEntity>newArrayList(), iterateBFS);
        }
        {
            LevelBFIterator<TestEntity> elements = new LevelBFIterator<TestEntity>(testTree, 0);
            List<TestEntity> iterateBFS = Lists.newArrayList(elements);
            assertEquals(Lists.newArrayList(te1), iterateBFS);
        }
        {
            LevelBFIterator<TestEntity> elements = new LevelBFIterator<TestEntity>(testTree, 1);
            List<TestEntity> iterateBFS = Lists.newArrayList(elements);
            assertEquals(Lists.newArrayList(te12, te13, te14), iterateBFS);
        }
        {
            LevelBFIterator<TestEntity> elements = new LevelBFIterator<TestEntity>(testTree, 2);
            List<TestEntity> iterateBFS = Lists.newArrayList(elements);
            assertEquals(Lists.newArrayList(te131, te141, te142), iterateBFS);
        }
        {
            LevelBFIterator<TestEntity> elements = new LevelBFIterator<TestEntity>(testTree, 3);
            List<TestEntity> iterateBFS = Lists.newArrayList(elements);
            assertEquals(Lists.<TestEntity>newArrayList(), iterateBFS);
        }
    }



}
