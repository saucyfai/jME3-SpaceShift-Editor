package com.ss.editor.ui.control.model.tree.action.particle.emitter.toneg0d.mesh;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.component.asset.tree.context.menu.action.DeleteFileAction;
import com.ss.editor.ui.component.asset.tree.context.menu.action.NewFileAction;
import com.ss.editor.ui.component.asset.tree.context.menu.action.RenameFileAction;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.particle.emitter.toneg0d.ChangeParticleMeshOperation;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.util.UIUtils;
import com.ss.editor.util.EditorUtil;
import com.ss.editor.util.NodeUtils;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.particle.ParticleDataMeshInfo;
import tonegod.emitter.particle.ParticleDataTemplateMesh;

import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * The action for switching the emitter shape of the {@link ParticleEmitterNode} to {@link ParticleDataTemplateMesh}.
 *
 * @author JavaSaBr
 */
public class LoadModelParticlesMeshAction extends AbstractNodeAction<ModelChangeConsumer> {

    private static final Predicate<Class<?>> ACTION_TESTER = type -> type == NewFileAction.class ||
            type == DeleteFileAction.class ||
            type == RenameFileAction.class;

    private static final Array<String> MODEL_EXTENSIONS = ArrayFactory.newArray(String.class);

    static {
        MODEL_EXTENSIONS.add(FileExtensions.JME_OBJECT);
    }

    /**
     * Instantiates a new Load model particles mesh action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public LoadModelParticlesMeshAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.OPEN_FILE_16;
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_PARTICLES_MESH_MODEL;
    }

    @FXThread
    @Override
    protected void process() {
        UIUtils.openAssetDialog(this::processOpen, MODEL_EXTENSIONS, ACTION_TESTER);
    }

    /**
     * The process of opening file.
     *
     * @param file the file
     */
    protected void processOpen(@NotNull final Path file) {

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());

        final Path assetFile = notNull(getAssetFile(file), "Not found asset file for " + file);
        final String assetPath = EditorUtil.toAssetPath(assetFile);

        final ModelKey modelKey = new ModelKey(assetPath);

        final AssetManager assetManager = EDITOR.getAssetManager();
        final Spatial loadedModel = assetManager.loadModel(modelKey);
        final Geometry geometry = NodeUtils.findGeometry(loadedModel);

        if (geometry == null) {
            LOGGER.warning(this, "not found a geometry in the model " + assetPath);
            return;
        }

        final TreeNode<?> treeNode = getNode();
        final ParticleEmitterNode emitterNode = (ParticleEmitterNode) treeNode.getElement();
        final ParticleGeometry particleGeometry = emitterNode.getParticleGeometry();
        final ParticleDataMeshInfo meshInfo = new ParticleDataMeshInfo(ParticleDataTemplateMesh.class, geometry.getMesh());

        changeConsumer.execute(new ChangeParticleMeshOperation(meshInfo, particleGeometry));
    }
}
