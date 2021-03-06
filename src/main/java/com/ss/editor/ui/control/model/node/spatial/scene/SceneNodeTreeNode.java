package com.ss.editor.ui.control.model.node.spatial.scene;

import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.spatial.NodeTreeNode;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.RenameNodeAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.extension.scene.SceneNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

/**
 * The implementation of the {@link NodeTreeNode} for representing the {@link SceneNode} in the editor.
 *
 * @author JavaSaBr
 */
public class SceneNodeTreeNode extends NodeTreeNode<SceneNode> {

    /**
     * Instantiates a new Scene node model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public SceneNodeTreeNode(@NotNull final SceneNode element, final long objectId) {
        super(element, objectId);
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree, @NotNull final ObservableList<MenuItem> items) {
        if (!(nodeTree instanceof ModelNodeTree)) return;

        final Menu createMenu = createCreationMenu(nodeTree);

        items.add(createMenu);
        items.add(new RenameNodeAction(nodeTree, this));
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.SCENE_16;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canAccept(@NotNull final TreeNode<?> child, final boolean isCopy) {
        return false;
    }

    @Override
    public boolean canCopy() {
        return false;
    }
}
