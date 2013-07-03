package org.jkee.gtree.iterator;

import org.jkee.gtree.Tree;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Depth-first
 * @author jkee
 */
public class DFIterator<T> implements Iterator<T> {

    private final Stack<Integer> stack = new Stack<Integer>();
    private Tree<T> current;

    public DFIterator(Tree<T> tree) {
        current = tree;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
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
                    return toReturn.getValue();
                }
                localCurrent = parent;
            }
            current = null;
        }
        return toReturn.getValue();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
