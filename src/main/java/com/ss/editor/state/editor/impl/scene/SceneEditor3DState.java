package com.ss.editor.state.editor.impl.scene;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.scene.Node;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.config.EditorConfig;
import com.ss.editor.extension.scene.SceneNode;
import com.ss.editor.extension.scene.app.state.SceneAppState;
import com.ss.editor.extension.scene.filter.SceneFilter;
import com.ss.editor.ui.component.editor.impl.scene.SceneFileEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link AbstractSceneEditor3DState} for the {@link SceneFileEditor}.
 *
 * @author JavaSaBr
 */
public class SceneEditor3DState extends AbstractSceneEditor3DState<SceneFileEditor, SceneNode> {

    /**
     * The flag of showing light models.
     */
    private boolean lightShowed;

    /**
     * The flag of showing audio models.
     */
    private boolean audioShowed;

    /**
     * Instantiates a new Scene editor app state.
     *
     * @param fileEditor the file editor
     */
    public SceneEditor3DState(@NotNull final SceneFileEditor fileEditor) {
        super(fileEditor);

        this.lightShowed = true;
        this.audioShowed = true;

        final Node stateNode = getStateNode();
        stateNode.attachChild(getModelNode());
        stateNode.attachChild(getToolNode());
    }

    @Override
    protected int getGridSize() {
        return 1000;
    }

    @Override
    protected void attachModel(@NotNull final SceneNode model, @NotNull final Node modelNode) {
    }

    @Override
    protected void detachPrevModel(@NotNull final Node modelNode, @Nullable final SceneNode currentModel) {
    }

    @Override
    public void initialize(@NotNull final AppStateManager stateManager, @NotNull final Application application) {
        super.initialize(stateManager, application);

        final SceneNode currentModel = getCurrentModel();

        if (currentModel != null) {
            getModelNode().attachChild(currentModel);
        }

        final FXAAFilter fxaaFilter = EDITOR.getFXAAFilter();
        fxaaFilter.setEnabled(false);

        final ToneMapFilter toneMapFilter = EDITOR.getToneMapFilter();
        toneMapFilter.setEnabled(false);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        final SceneNode currentModel = getCurrentModel();

        if (currentModel != null) {
            getModelNode().detachChild(currentModel);
        }

        final EditorConfig editorConfig = EditorConfig.getInstance();

        final FXAAFilter fxaaFilter = EDITOR.getFXAAFilter();
        fxaaFilter.setEnabled(editorConfig.isFXAA());

        final ToneMapFilter toneMapFilter = EDITOR.getToneMapFilter();
        toneMapFilter.setEnabled(editorConfig.isToneMapFilter());
    }

    /**
     * Add a scene app state.
     *
     * @param appState the scene app state.
     */
    @FromAnyThread
    public void addAppState(@NotNull final SceneAppState appState) {
        EXECUTOR_MANAGER.addJMETask(() -> addAppStateImpl(appState));
    }

    private void addAppStateImpl(@NotNull final SceneAppState appState) {
        final AppStateManager stateManager = EDITOR.getStateManager();
        stateManager.attach(appState);
    }

    /**
     * Remove a scene app state.
     *
     * @param appState the scene app state.
     */
    @FromAnyThread
    public void removeAppState(@NotNull final SceneAppState appState) {
        EXECUTOR_MANAGER.addJMETask(() -> removeAppStateImpl(appState));
    }

    private void removeAppStateImpl(@NotNull final SceneAppState appState) {
        final AppStateManager stateManager = EDITOR.getStateManager();
        stateManager.detach(appState);
    }

    /**
     * Add a scene filter.
     *
     * @param sceneFilter the scene filter.
     */
    @FromAnyThread
    public void addFilter(@NotNull final SceneFilter sceneFilter) {
        EXECUTOR_MANAGER.addJMETask(() -> addFilterImpl(sceneFilter));
    }

    private void addFilterImpl(@NotNull final SceneFilter sceneFilter) {
        final FilterPostProcessor postProcessor = EDITOR.getPostProcessor();
        postProcessor.addFilter(sceneFilter.get());
    }

    /**
     * Remove a scene filter.
     *
     * @param sceneFilter the scene filter.
     */
    @FromAnyThread
    public void removeFilter(@NotNull final SceneFilter sceneFilter) {
        EXECUTOR_MANAGER.addJMETask(() -> removeFilterImpl(sceneFilter));
    }

    private void removeFilterImpl(@NotNull final SceneFilter sceneFilter) {
        final FilterPostProcessor postProcessor = EDITOR.getPostProcessor();
        postProcessor.removeFilter(sceneFilter.get());
    }

    /**
     * @return true if need to show light models.
     */
    private boolean isLightShowed() {
        return lightShowed;
    }

    /**
     * @param lightShowed true if need to show light models.
     */
    private void setLightShowed(final boolean lightShowed) {
        this.lightShowed = lightShowed;
    }

    /**
     * @return true if need to show audio models.
     */
    private boolean isAudioShowed() {
        return audioShowed;
    }

    /**
     * @param audioShowed true if need to show audio models.
     */
    private void setAudioShowed(final boolean audioShowed) {
        this.audioShowed = audioShowed;
    }

    /**
     * Change light showing.
     *
     * @param showed the showed
     */
    public void updateLightShowed(final boolean showed) {
        EXECUTOR_MANAGER.addJMETask(() -> updateLightShowedImpl(showed));
    }

    /**
     * The process to change light showing.
     */
    private void updateLightShowedImpl(final boolean showed) {
        if (showed == isLightShowed()) return;

        final Node lightNode = getLightNode();
        final Node modelNode = getModelNode();

        if (showed) {
            modelNode.attachChild(lightNode);
        } else {
            modelNode.detachChild(lightNode);
        }

        setLightShowed(showed);
    }

    /**
     * Change audio showing.
     *
     * @param showed the showed
     */
    public void updateAudioShowed(final boolean showed) {
        EXECUTOR_MANAGER.addJMETask(() -> updateAudioShowedImpl(showed));
    }

    /**
     * The process to change audio showing.
     */
    private void updateAudioShowedImpl(final boolean showed) {
        if (showed == isAudioShowed()) return;

        final Node audioNode = getAudioNode();
        final Node modelNode = getModelNode();

        if (showed) {
            modelNode.attachChild(audioNode);
        } else {
            modelNode.detachChild(audioNode);
        }

        setAudioShowed(showed);
    }
}
