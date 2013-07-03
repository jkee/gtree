package org.jkee.gtree.iterator;

import org.jkee.gtree.Tree;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author jkee
 */
public class LevelBFIterator<T> implements Iterator<T> {

    private final Stack<Integer> stack = new Stack<Integer>();
    final int level;
    private Tree<T> current;

    public LevelBFIterator(Tree<T> root, int level) {
        this.level = level;
        current = root;
        moveToGoodNode();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (current == null) throw new NoSuchElementException();
        Tree<T> toReturn = current;
        //current node is already our level so we can go higher only
        //so moving up
        boolean success = moveUpToNext();
        if (!success) return toReturn.getValue();
        //and moving down or up while we find our level
        moveToGoodNode();
        return toReturn.getValue();
    }

    private void moveToGoodNode() {
        while (stack.size() != level) {
            boolean found = tryMoveDownToNext();
            if (!found) { //no way down, try up
                boolean success = moveUpToNext();
                if (!success) return;
            }
        }
    }

    private boolean tryMoveDownToNext() {
        List<Tree<T>> children = current.getChildren();
        if (children != null && !children.isEmpty()) {
            //starting next level
            Tree<T> firstChild = children.get(0);
            stack.push(0);
            current = firstChild;
            return true;
        }
        return false;
    }

    private boolean moveUpToNext() {
        Tree<T> localCurrent = current;
        current = null;
        while (!stack.empty()) {
            Tree<T> parent = localCurrent.getParent();
            Integer parentIndex = stack.pop();
            int nextIndex = parentIndex + 1;
            if (nextIndex < parent.getChildren().size()) {
                stack.push(nextIndex);
                current = parent.getChildren().get(nextIndex);
                return true;
            } else {
                localCurrent = parent;
            }
        }
        return false;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
