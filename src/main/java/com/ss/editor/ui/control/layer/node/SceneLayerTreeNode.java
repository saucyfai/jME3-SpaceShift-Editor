package com.ss.editor.ui.control.layer.node;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.scene.Spatial;
import com.ss.editor.extension.scene.SceneLayer;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.spatial.NodeTreeNode;
import com.ss.editor.ui.control.model.property.operation.ModelPropertyOperation;
import com.ss.editor.ui.control.model.tree.action.RenameNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.RenameNodeOperation;
import com.ss.editor.ui.control.model.tree.action.operation.scene.ChangeVisibleSceneLayerOperation;
import com.ss.editor.ui.control.model.tree.action.scene.RemoveSceneLayerAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.HideableNode;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link NodeTreeNode} for representing the {@link SceneLayer} in the editor.
 *
 * @author JavaSaBr
 */
public class SceneLayerTreeNode extends TreeNode<SceneLayer> implements HideableNode<SceneChangeConsumer> {

    /**
     * Instantiates a new Scene layer model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public SceneLayerTreeNode(@NotNull final SceneLayer element, final long objectId) {
        super(element, objectId);
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree, @NotNull final ObservableList<MenuItem> items) {
        super.fillContextMenu(nodeTree, items);

        final SceneLayer layer = getElement();

        if (!layer.isBuiltIn()) {
            items.add(new RenameNodeAction(nodeTree, this));
            items.add(new RemoveSceneLayerAction(nodeTree, this));
        }
    }

    @Override
    public void changeName(@NotNull final NodeTree<?> nodeTree, @NotNull final String newName) {

        final SceneLayer element = getElement();

        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new RenameNodeOperation(element.getName(), newName, element));
    }

    @Override
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return true;
    }

    @NotNull
    @Override
    public Array<TreeNode<?>> getChildren(@NotNull final NodeTree<?> nodeTree) {

        final SceneLayer element = getElement();

        final Array<TreeNode<?>> result = ArrayFactory.newArray(TreeNode.class);
        final ModelChangeConsumer changeConsumer = (ModelChangeConsumer) notNull(nodeTree.getChangeConsumer());

        final Spatial currentModel = changeConsumer.getCurrentModel();
        currentModel.depthFirstTraversal(spatial -> {
            final SceneLayer layer = SceneLayer.getLayer(spatial);
            if(layer == element) {
                result.add(FACTORY_REGISTRY.createFor(spatial));
            }
        });

        return result;
    }

    @Override
    public boolean canAccept(@NotNull final TreeNode<?> child, final boolean isCopy) {
        final Object element = child.getElement();
        return element instanceof Spatial && SceneLayer.getLayer((Spatial) element) != getElement();
    }

    @Override
    public void accept(@NotNull final ChangeConsumer changeConsumer, @NotNull final Object object,
                       final boolean isCopy) {

        final SceneLayer targetLayer = getElement();

        if (object instanceof Spatial && !isCopy) {

            final Spatial spatial = (Spatial) object;
            final SceneLayer currentLayer = SceneLayer.getLayer(spatial);

            final ModelPropertyOperation<Spatial, SceneLayer> operation =
                    new ModelPropertyOperation<>(spatial, SceneLayer.KEY, targetLayer, currentLayer);

            operation.setApplyHandler((sp, layer) -> SceneLayer.setLayer(layer, sp));

            changeConsumer.execute(operation);
        }

        super.accept(changeConsumer, object, isCopy);
    }

    @NotNull
    @Override
    public String getName() {
        final String name = getElement().getName();
        return name == null ? "name is null" : name;
    }

    @Override
    public boolean canEditName() {
        return !getElement().isBuiltIn();
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.LAYERS_16;
    }

    @Override
    public boolean isHided() {
        return !getElement().isShowed();
    }

    @Override
    public void show(@NotNull final NodeTree<SceneChangeConsumer> nodeTree) {
        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new ChangeVisibleSceneLayerOperation(getElement(), true));
    }

    @Override
    public void hide(@NotNull final NodeTree<SceneChangeConsumer> nodeTree) {
        final ChangeConsumer consumer = notNull(nodeTree.getChangeConsumer());
        consumer.execute(new ChangeVisibleSceneLayerOperation(getElement(), false));
    }
}
