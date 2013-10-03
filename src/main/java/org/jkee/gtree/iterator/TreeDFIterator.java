package org.jkee.gtree.iterator;

import org.jkee.gtree.Tree;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Depth-first, on tree elements
 * @author jkee
 */
public class TreeDFIterator<T> implements Iterator<Tree<T>> {

    private final Stack<Integer> stack = new Stack<Integer>();
    private Tree<T> current;

    public TreeDFIterator(Tree<T> tree) {
        current = tree;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Tree<T> next() {
        if (current == null) throw new NoSuchElementException();
        Tree<T> toReturn = current;
        List<Tree<T>> children = current.getChildren();
        if (children != null && !children.isEmpty()) {
            //starting next level
            Tree<T> firstChild = children.get(0);
            stack.push(0);
            current = firstChild;
        } else {
            //moving up
            Tree<T> localCurrent = current;
            while (!stack.empty()) {
                Tree<T> parent = localCurrent.getParent();
                Integer parentIndex = stack.pop();
                int nextIndex = parentIndex + 1;
                if (nextIndex < parent.getChildren().size()) {
                    stack.push(nextIndex);
                    current = parent.getChildren().get(nextIndex);
                    return toReturn;
                }
                localCurrent = parent;
            }
            current = null;
        }
        return toReturn;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
