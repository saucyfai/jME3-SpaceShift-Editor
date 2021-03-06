package com.ss.editor.ui.component.editor.impl.model;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.editor.util.EditorUtil.toAssetPath;
import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.asset.TextureKey;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.manager.ResourceManager;
import com.ss.editor.state.editor.impl.model.ModelEditor3DState;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.component.editor.EditorDescription;
import com.ss.editor.ui.component.editor.impl.AbstractFileEditor;
import com.ss.editor.ui.component.editor.impl.scene.AbstractSceneFileEditor;
import com.ss.editor.ui.component.editor.state.EditorState;
import com.ss.editor.ui.component.editor.state.impl.ModelFileEditorState;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.editor.util.MaterialUtils;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.ui.util.FXUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * The implementation of the {@link AbstractFileEditor} for working with {@link Spatial}.
 *
 * @author JavaSaBr
 */
public class ModelFileEditor extends AbstractSceneFileEditor<ModelFileEditor, Spatial, ModelEditor3DState, ModelFileEditorState> {

    @NotNull
    private static final String NO_FAST_SKY = Messages.MODEL_FILE_EDITOR_NO_SKY;

    /**
     * The constant DESCRIPTION.
     */
    @NotNull
    public static final EditorDescription DESCRIPTION = new EditorDescription();

    static {
        DESCRIPTION.setEditorName(Messages.MODEL_FILE_EDITOR_NAME);
        DESCRIPTION.setConstructor(ModelFileEditor::new);
        DESCRIPTION.setEditorId(ModelFileEditor.class.getSimpleName());
        DESCRIPTION.addExtension(FileExtensions.JME_OBJECT);
    }

    @NotNull
    private static final Array<String> FAST_SKY_LIST = ArrayFactory.newArray(String.class);

    static {
        FAST_SKY_LIST.add(NO_FAST_SKY);
        FAST_SKY_LIST.add("graphics/textures/sky/studio.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env1.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env2.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env3.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env4.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/outside.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/inside.hdr");
    }

    /**
     * The list of fast skies.
     */
    @Nullable
    private ComboBox<String> fastSkyComboBox;

    /**
     * The light toggle.
     */
    @Nullable
    private ToggleButton lightButton;

    private ModelFileEditor() {
        super();
    }

    @NotNull
    @Override
    protected ModelEditor3DState createEditor3DState() {
        return new ModelEditor3DState(this);
    }

    /**
     * @return the list of fast skies.
     */
    @NotNull
    private ComboBox<String> getFastSkyComboBox() {
        return notNull(fastSkyComboBox);
    }

    /**
     * @return the light toggle.
     */
    @NotNull
    private ToggleButton getLightButton() {
        return notNull(lightButton);
    }

    @FXThread
    @Override
    public void openFile(@NotNull final Path file) {
        super.openFile(file);

        final Path assetFile = notNull(getAssetFile(file), "Asset file for " + file + " can't be null.");
        final ModelKey modelKey = new ModelKey(toAssetPath(assetFile));

        final AssetManager assetManager = EDITOR.getAssetManager();
        final Spatial model = assetManager.loadAsset(modelKey);

        MaterialUtils.cleanUpMaterialParams(model);

        final ModelEditor3DState editor3DState = getEditor3DState();
        editor3DState.openModel(model);

        handleAddedObject(model);

        setCurrentModel(model);
        setIgnoreListeners(true);
        try {

            final ComboBox<String> fastSkyComboBox = getFastSkyComboBox();
            fastSkyComboBox.getSelectionModel().select(FAST_SKY_LIST.first());

            refreshTree();

        } finally {
            setIgnoreListeners(false);
        }

        EXECUTOR_MANAGER.addFXTask(this::loadState);
    }

    @Override
    protected void loadState() {
        super.loadState();

        final ModelFileEditorState editorState = notNull(getEditorState());

        final ComboBox<String> fastSkyComboBox = getFastSkyComboBox();
        fastSkyComboBox.getSelectionModel().select(editorState.getSkyType());

        final ToggleButton lightButton = getLightButton();
        lightButton.setSelected(editorState.isEnableLight());
    }

    @NotNull
    @Override
    protected Supplier<EditorState> getStateConstructor() {
        return ModelFileEditorState::new;
    }

    @Override
    protected void handleAddedObject(@NotNull final Spatial model) {
        super.handleAddedObject(model);

        final ModelEditor3DState editor3DState = getEditor3DState();
        final Array<Geometry> geometries = ArrayFactory.newArray(Geometry.class);

        NodeUtils.addGeometry(model, geometries);

        if (!geometries.isEmpty()) {
            geometries.forEach(geometry -> {
                if (geometry.getQueueBucket() == RenderQueue.Bucket.Sky) {
                    editor3DState.addCustomSky(geometry);
                }
            });
        }
    }

    @NotNull
    @Override
    public EditorDescription getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected void createToolbar(@NotNull final HBox container) {
        super.createToolbar(container);

        final Label fastSkyLabel = new Label(Messages.MODEL_FILE_EDITOR_FAST_SKY + ":");

        fastSkyComboBox = new ComboBox<>();
        fastSkyComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> changeFastSky(newValue));

        final ObservableList<String> skyItems = fastSkyComboBox.getItems();
        skyItems.addAll(FAST_SKY_LIST);

        final ResourceManager resourceManager = ResourceManager.getInstance();
        final Array<Path> additionalEnvs = resourceManager.getAdditionalEnvs();
        additionalEnvs.forEach(path -> skyItems.add(path.toString()));

        FXUtils.addToPane(fastSkyLabel, container);
        FXUtils.addToPane(fastSkyComboBox, container);
    }

    @Override
    protected void createActions(@NotNull final HBox container) {
        super.createActions(container);

        lightButton = new ToggleButton();
        lightButton.setTooltip(new Tooltip(Messages.SCENE_FILE_EDITOR_ACTION_CAMERA_LIGHT));
        lightButton.setGraphic(new ImageView(Icons.LIGHT_16));
        lightButton.setSelected(true);
        lightButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> changeLight(newValue));

        DynamicIconSupport.addSupport(lightButton);

        FXUtils.addClassTo(lightButton, CSSClasses.FILE_EDITOR_TOOLBAR_BUTTON);
        FXUtils.addToPane(lightButton, container);
    }

    /**
     * Handle changing a sky.
     */
    private void changeFastSky(@NotNull final String newSky) {
        if (isIgnoreListeners()) return;

        final ModelEditor3DState editor3DState = getEditor3DState();

        if (NO_FAST_SKY.equals(newSky)) {
            editor3DState.changeFastSky(null);
            final ModelFileEditorState editorState = getEditorState();
            if (editorState != null) editorState.setSkyType(0);
            return;
        }

        final AssetManager assetManager = EDITOR.getAssetManager();

        final TextureKey key = new TextureKey(newSky, true);
        key.setGenerateMips(false);

        final Texture texture = assetManager.loadTexture(key);
        final Spatial newFastSky = SkyFactory.createSky(assetManager, texture, EnvMapType.EquirectMap);

        editor3DState.changeFastSky(newFastSky);

        final ComboBox<String> fastSkyComboBox = getFastSkyComboBox();
        final SingleSelectionModel<String> selectionModel = fastSkyComboBox.getSelectionModel();
        final int selectedIndex = selectionModel.getSelectedIndex();

        final ModelFileEditorState editorState = getEditorState();
        if (editorState != null) editorState.setSkyType(selectedIndex);
    }

    /**
     * Handle changing camera light visibility.
     */
    private void changeLight(@NotNull final Boolean newValue) {
        if (isIgnoreListeners()) return;

        final ModelEditor3DState editor3DState = getEditor3DState();
        editor3DState.updateLightEnabled(newValue);

        if (editorState != null) editorState.setEnableLight(newValue);
    }

    @Override
    public void notifyFXAddedChild(@NotNull final Object parent, @NotNull final Object added, final int index,
                                   final boolean needSelect) {
        super.notifyFXAddedChild(parent, added, index, needSelect);

        final ModelEditor3DState editor3DState = getEditor3DState();

        if (added instanceof Spatial) {

            final Spatial spatial = (Spatial) added;
            final boolean isSky = spatial.getQueueBucket() == RenderQueue.Bucket.Sky;

            if (isSky) {
                editor3DState.addCustomSky(spatial);
                editor3DState.updateLightProbe();
            }
        }
    }

    @Override
    public void notifyFXRemovedChild(@NotNull final Object parent, @NotNull final Object removed) {
        super.notifyFXRemovedChild(parent, removed);

        final ModelEditor3DState editor3DState = getEditor3DState();

        if (removed instanceof Spatial) {

            final Spatial spatial = (Spatial) removed;
            final boolean isSky = spatial.getQueueBucket() == RenderQueue.Bucket.Sky;

            if (isSky) {
                editor3DState.removeCustomSky(spatial);
                editor3DState.updateLightProbe();
            }
        }
    }

    @Override
    public String toString() {
        return "ModelFileEditor{" +
                "} " + super.toString();
    }
}
