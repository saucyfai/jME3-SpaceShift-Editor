package com.ss.editor.ui.control.model.node.spatial;

import static com.ss.editor.util.EditorUtil.*;
import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.extension.scene.SceneLayer;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.*;
import com.ss.editor.ui.control.model.tree.action.audio.CreateAudioNodeAction;
import com.ss.editor.ui.control.model.tree.action.geometry.CreateBoxAction;
import com.ss.editor.ui.control.model.tree.action.geometry.CreateQuadAction;
import com.ss.editor.ui.control.model.tree.action.geometry.CreateSphereAction;
import com.ss.editor.ui.control.model.tree.action.light.CreateAmbientLightAction;
import com.ss.editor.ui.control.model.tree.action.light.CreateDirectionLightAction;
import com.ss.editor.ui.control.model.tree.action.light.CreatePointLightAction;
import com.ss.editor.ui.control.model.tree.action.light.CreateSpotLightAction;
import com.ss.editor.ui.control.model.tree.action.operation.AddChildOperation;
import com.ss.editor.ui.control.model.tree.action.operation.MoveChildOperation;
import com.ss.editor.ui.control.model.tree.action.particle.emitter.CreateParticleEmitterAction;
import com.ss.editor.ui.control.model.tree.action.particle.emitter.ResetParticleEmittersAction;
import com.ss.editor.ui.control.model.tree.action.particle.emitter.toneg0d.CreateToneg0dParticleEmitterAction;
import com.ss.editor.ui.control.model.tree.action.particle.emitter.toneg0d.CreateToneg0dSoftParticleEmitterAction;
import com.ss.editor.ui.control.model.tree.action.terrain.CreateTerrainAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.util.UIUtils;
import com.ss.editor.util.GeomUtils;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

import java.nio.file.Path;
import java.util.List;

/**
 * The implementation of the {@link SpatialTreeNode} for representing the {@link Node} in the editor.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
public class NodeTreeNode<T extends Node> extends SpatialTreeNode<T> {

    /**
     * Instantiates a new Node model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public NodeTreeNode(@NotNull final T element, final long objectId) {
        super(element, objectId);
    }

    @Nullable
    @Override
    protected Menu createToolMenu(final @NotNull NodeTree<?> nodeTree) {
        final Menu toolMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_TOOLS, new ImageView(Icons.INFLUENCER_16));
        toolMenu.getItems().addAll(new OptimizeGeometryAction(nodeTree, this));
        return toolMenu;
    }

    @Nullable
    @Override
    protected Menu createCreationMenu(@NotNull final NodeTree<?> nodeTree) {

        final Menu menu = super.createCreationMenu(nodeTree);
        if (menu == null) return null;

        final Menu createPrimitiveMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_CREATE_PRIMITIVE,
                new ImageView(Icons.ADD_12));
        createPrimitiveMenu.getItems()
                .addAll(new CreateBoxAction(nodeTree, this),
                        new CreateSphereAction(nodeTree, this),
                        new CreateQuadAction(nodeTree, this));

        final Menu addLightMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_LIGHT, new ImageView(Icons.ADD_12));
        addLightMenu.getItems()
                .addAll(new CreateSpotLightAction(nodeTree, this),
                        new CreatePointLightAction(nodeTree, this),
                        new CreateAmbientLightAction(nodeTree, this),
                        new CreateDirectionLightAction(nodeTree, this));

        menu.getItems().addAll(new CreateNodeAction(nodeTree, this),
                new LoadModelAction(nodeTree, this),
                new LinkModelAction(nodeTree, this),
                new CreateSkyAction(nodeTree, this),
                new CreateEditableSkyAction(nodeTree, this),
                new CreateToneg0dParticleEmitterAction(nodeTree, this),
                new CreateToneg0dSoftParticleEmitterAction(nodeTree, this),
                new CreateParticleEmitterAction(nodeTree, this),
                new CreateAudioNodeAction(nodeTree, this),
                new CreateTerrainAction(nodeTree, this),
                createPrimitiveMenu, addLightMenu);

        return menu;
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree,
                                @NotNull final ObservableList<MenuItem> items) {
        if (!(nodeTree instanceof ModelNodeTree)) return;

        final T element = getElement();
        final Spatial emitter = NodeUtils.findSpatial(element, ParticleEmitterNode.class::isInstance);

        if (emitter != null) {
            items.add(new ResetParticleEmittersAction(nodeTree, this));
        }

        super.fillContextMenu(nodeTree, items);
    }

    @Override
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }

    @NotNull
    @Override
    public Array<TreeNode<?>> getChildren(@NotNull final NodeTree<?> nodeTree) {

        final Array<TreeNode<?>> result = ArrayFactory.newArray(TreeNode.class);
        final List<Spatial> children = getSpatials();
        children.forEach(spatial -> result.add(FACTORY_REGISTRY.createFor(spatial)));
        result.addAll(super.getChildren(nodeTree));

        return result;
    }

    /**
     * Gets spatials.
     *
     * @return the spatials
     */
    @NotNull
    protected List<Spatial> getSpatials() {
        final Node element = getElement();
        return element.getChildren();
    }

    @Override
    public boolean canAccept(@NotNull final TreeNode<?> child, final boolean isCopy) {
        if (child == this) return false;

        final Object element = child.getElement();

        if (element instanceof Spatial) {
            return GeomUtils.canAttach(getElement(), (Spatial) element, isCopy);
        }

        return super.canAccept(child, isCopy);
    }

    @Override
    public void accept(@NotNull final ChangeConsumer changeConsumer, @NotNull final Object object,
                       final boolean isCopy) {

        final T newParent = getElement();

        if (object instanceof Spatial) {

            final Spatial spatial = (Spatial) object;

            if (isCopy) {

                final Spatial clone = spatial.clone();
                final SceneLayer layer = SceneLayer.getLayer(spatial);

                if (layer != null) {
                    SceneLayer.setLayer(layer, clone);
                }

                changeConsumer.execute(new AddChildOperation(clone, newParent, true));

            } else {
                final Node parent = spatial.getParent();
                final int childIndex = parent.getChildIndex(spatial);
                changeConsumer.execute(new MoveChildOperation(spatial, parent, newParent, childIndex));
            }
        }

        super.accept(changeConsumer, object, isCopy);
    }

    @Override
    public void add(@NotNull final TreeNode<?> child) {
        super.add(child);

        final Node node = getElement();
        final Object toAdd = child.getElement();

        if (toAdd instanceof Spatial) {
            node.attachChildAt((Spatial) toAdd, 0);
        }
    }

    @Override
    public void remove(@NotNull final TreeNode<?> child) {
        super.remove(child);

        final Node node = getElement();
        final Object toRemove = child.getElement();

        if (toRemove instanceof Spatial) {
            node.detachChild((Spatial) toRemove);
        }
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.NODE_16;
    }

    @Override
    public boolean canAcceptExternal(@NotNull final Dragboard dragboard) {
        return UIUtils.isHasFile(dragboard, FileExtensions.JME_OBJECT);
    }

    @Override
    public void acceptExternal(@NotNull final Dragboard dragboard, @NotNull final ChangeConsumer consumer) {
        UIUtils.handleDroppedFile(dragboard, FileExtensions.JME_OBJECT, getElement(), consumer, this::dropExternalObject);
    }

    /**
     * Add the external object to this node.
     *
     * @param node this node.
     * @param cons the change consumer.
     * @param path the path to the external object.
     */
    protected void dropExternalObject(@NotNull final T node, @NotNull final ChangeConsumer cons,
                                      @NotNull final Path path) {

        final SceneLayer defaultLayer = getDefaultLayer(cons);
        final Path assetFile = notNull(getAssetFile(path), "Not found asset file for " + path);
        final String assetPath = toAssetPath(assetFile);
        final ModelKey modelKey = new ModelKey(assetPath);

        final AssetManager assetManager = EDITOR.getAssetManager();
        final Spatial loadedModel = assetManager.loadModel(assetPath);
        final AssetLinkNode assetLinkNode = new AssetLinkNode(modelKey);
        assetLinkNode.attachLinkedChild(loadedModel, modelKey);

        if (defaultLayer != null) {
            SceneLayer.setLayer(defaultLayer, loadedModel);
        }

        cons.execute(new AddChildOperation(assetLinkNode, node));
    }
}
