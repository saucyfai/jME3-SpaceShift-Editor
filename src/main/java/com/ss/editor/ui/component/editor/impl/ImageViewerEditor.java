package com.ss.editor.ui.component.editor.impl;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.editor.Editor;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.manager.JavaFXImageManager;
import com.ss.editor.ui.component.editor.EditorDescription;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.event.impl.FileChangedEvent;
import com.ss.rlib.plugin.annotation.PluginDescription;
import com.ss.rlib.ui.util.FXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The implementation of the {@link Editor} to view image files.
 *
 * @author JavaSaBr
 */
public class ImageViewerEditor extends AbstractFileEditor<VBox> {

    /**
     * The constant DESCRIPTION.
     */
    @NotNull
    public static final EditorDescription DESCRIPTION = new EditorDescription();

    @PluginDescription
    private static final JavaFXImageManager JAVA_FX_IMAGE_MANAGER = JavaFXImageManager.getInstance();

    private static final int IMAGE_SIZE = 512;

    static {
        DESCRIPTION.setConstructor(ImageViewerEditor::new);
        DESCRIPTION.setEditorName(Messages.IMAGE_VIEWER_EDITOR_NAME);
        DESCRIPTION.setEditorId(ImageViewerEditor.class.getSimpleName());
        DESCRIPTION.setExtensions(FileExtensions.IMAGE_EXTENSIONS);
    }

    /**
     * The image view.
     */
    @Nullable
    private ImageView imageView;

    @NotNull
    @Override
    protected VBox createRoot() {
        return new VBox();
    }

    @Override
    protected void createContent(@NotNull final VBox root) {

        imageView = new ImageView();

        FXUtils.addToPane(imageView, root);
        FXUtils.addClassTo(root, CSSClasses.IMAGE_VIEW_EDITOR_CONTAINER);
    }

    /**
     * @return the image view.
     */
    @NotNull
    private ImageView getImageView() {
        return notNull(imageView);
    }

    @Override
    protected void processChangedFile(@NotNull final FileChangedEvent event) {
        final Path file = event.getFile();
        if (!getEditFile().equals(file)) return;
        EXECUTOR_MANAGER.schedule(() -> EXECUTOR_MANAGER.addFXTask(() -> showImage(file)), 1000);
    }

    private void showImage(@NotNull final Path file) {
        final Image preview = JAVA_FX_IMAGE_MANAGER.getImagePreview(file, IMAGE_SIZE, IMAGE_SIZE);
        final ImageView imageView = getImageView();
        imageView.setImage(preview);
    }

    @FXThread
    @Override
    public void openFile(@NotNull final Path file) {
        super.openFile(file);
        showImage(file);
    }

    @NotNull
    @Override
    public EditorDescription getDescription() {
        return DESCRIPTION;
    }
}
