package com.ss.editor.ui.control.layer.node;

import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.layer.LayerNodeTree;
import com.ss.editor.ui.control.layer.LayersRoot;
import com.ss.editor.ui.control.model.tree.action.scene.CreateSceneLayerAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.extension.scene.SceneLayer;
import com.ss.editor.extension.scene.SceneNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

import java.util.List;

/**
 * The implementation of {@link TreeNode} to present {@link LayersRoot}.
 *
 * @author JavaSaBr
 */
public class LayersRootTreeNode extends TreeNode<LayersRoot> {

    /**
     * Instantiates a new Layers root model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public LayersRootTreeNode(@NotNull final LayersRoot element, final long objectId) {
        super(element, objectId);
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.SCENE_16;
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree, @NotNull final ObservableList<MenuItem> items) {
        items.add(new CreateSceneLayerAction(nodeTree, this));
    }

    @NotNull
    @Override
    public String getName() {
        final LayersRoot element = getElement();
        final SceneChangeConsumer changeConsumer = element.getChangeConsumer();
        final SceneNode sceneNode = changeConsumer.getCurrentModel();
        return sceneNode.getName();
    }

    @NotNull
    @Override
    public Array<TreeNode<?>> getChildren(@NotNull final NodeTree<?> nodeTree) {

        final LayersRoot element = getElement();
        final SceneChangeConsumer changeConsumer = element.getChangeConsumer();
        final SceneNode sceneNode = changeConsumer.getCurrentModel();
        final List<SceneLayer> layers = sceneNode.getLayers();

        final Array<TreeNode<?>> result = ArrayFactory.newArray(TreeNode.class);
        layers.forEach(layer -> result.add(FACTORY_REGISTRY.createFor(layer)));

        return result;
    }

    @Override
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof LayerNodeTree;
    }
}
