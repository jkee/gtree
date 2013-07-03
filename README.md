# GTree

## Java tree, for general purpose. Functional approach.
_Beware, it is not a binary tree_

### Features:

- Simple
- Iterable
- Modification: mapping, filtering, sorting
- Pretty-printing
- Different traversals - depth-first, breadth-first

### Builders

- Key builder
- Path builder

### Examples

#### 1. Build a tree from `id` - `parentId` mapping (adjacency list)
Assume you have objects like:

```java
public class Entity {
    public final int id;
    public final int parentId;
    ...
}
```
And assume that `id == 0` means no parent (e.g. root).
`KeyTreeBuilder` will be the right choice.

1. Create a `Funnel`. `Funnel` is an object which knows how to extract `id` and `parentId` from your object.
    
    ```java
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
    ```
2. Create a `KeyTreeBuilder`. Builder is stateless so you can create only one.
    
    ```java
    KeyTreeBuilder<Integer, Entity> builder = new KeyTreeBuilder<Integer, Entity>(funnel);
    ```
3. Build a tree!
    
    ```java
    Tree<Entity> tree = builder.buildTree(entities);
    ```

If you want a forest, or an `Integer -> Tree<Entity>` mapping, you can use `.build` method and get more results.
Full example available [here](https://github.com/jkee/gtree/blob/master/src/test/java/org/jkee/gtree/examples/ParentIdTree.java)

#### 2. Building url tree
Assume you have a batch of url and you want to build a tree structure. It's simple, really.
All we need is the `PathTreeBuilder`.

1. Create a `Funnel` which knows how to extract a path from your object. In case of URL it will be a split by `/` (simplistically).
    
    ```java
    PathTreeBuilder.Funnel<String, String> urlFunnel = new PathTreeBuilder.Funnel<String, String>() {
        @Override
        public List<String> getPath(String value) {
            return Lists.newArrayList(value.split("/"));
        }
    };
    ```
2. Create a `PathTreeBuilder`. Also stateless.
    
    ```java
    PathTreeBuilder<String, String> builder = new PathTreeBuilder<String, String>(urlFunnel);
    ```
3. Build a tree! Actually, not a tree - a forest. There are no guarantees of root uniqueness.
    
    ```java
    List<Tree<String>> build = builder.build(urls);
    ```

Full example available [here](https://github.com/jkee/gtree/blob/master/src/test/java/org/jkee/gtree/examples/UrlTree.java)
