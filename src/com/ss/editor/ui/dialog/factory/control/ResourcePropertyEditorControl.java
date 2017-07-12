package com.ss.editor.ui.dialog.factory.control;

import static com.ss.rlib.util.ClassUtils.unsafeCast;
import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.dialog.factory.PropertyDefinition;
import com.ss.rlib.ui.util.FXUtils;
import com.ss.rlib.util.VarTable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * The control to edit resource values.
 *
 * @author JavaSaBr
 */
public abstract class ResourcePropertyEditorControl<T> extends PropertyEditorControl<T> {

    /**
     * The constant BUTTON_OFFSET.
     */
    @NotNull
    private static final Insets BUTTON_OFFSET = new Insets(0, 0, 0, 3);

    /**
     * The constant NOT_SELECTED.
     */
    @NotNull
    protected static final String NOT_SELECTED = Messages.RESOURCE_PROPERTY_EDIT_CONTROL_NOTHING_IS_SELECTED;

    /**
     * The label with name of the resource.
     */
    @Nullable
    private Label resourceLabel;

    protected ResourcePropertyEditorControl(@NotNull final VarTable vars, @NotNull final PropertyDefinition definition,
                                            @NotNull final Runnable validationCallback) {
        super(vars, definition, validationCallback);
        setOnDragOver(this::dragOver);
        setOnDragDropped(this::dragDropped);
        setOnDragExited(this::dragExited);
    }

    @Override
    protected void createComponents() {
        super.createComponents();

        resourceLabel = new Label(NOT_SELECTED);
        resourceLabel.setId(CSSIds.ABSTRACT_PARAM_CONTROL_ELEMENT_LABEL);

        final Button changeButton = new Button();
        changeButton.setId(CSSIds.ABSTRACT_PARAM_CONTROL_ELEMENT_BUTTON);
        changeButton.setGraphic(new ImageView(Icons.ADD_16));
        changeButton.setOnAction(event -> processSelect());

        resourceLabel.prefWidthProperty().bind(widthProperty().multiply(0.5)
                .subtract(changeButton.widthProperty())
                .subtract(BUTTON_OFFSET.getLeft()));

        FXUtils.addToPane(resourceLabel, this);
        FXUtils.addToPane(changeButton, this);

        HBox.setMargin(changeButton, BUTTON_OFFSET);

        FXUtils.addClassTo(resourceLabel, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(changeButton, CSSClasses.FLAT_BUTTON);
        FXUtils.addClassTo(changeButton, CSSClasses.FILE_EDITOR_TOOLBAR_BUTTON);
    }

    /**
     * Process select a resource.
     */
    protected void processSelect() {

    }

    /**
     * Handle grad exiting.
     */
    private void dragExited(@NotNull final DragEvent dragEvent) {
    }

    /**
     * Handle dropped files to editor.
     */
    private void dragDropped(@NotNull final DragEvent dragEvent) {

        final Dragboard dragboard = dragEvent.getDragboard();
        final List<File> files = unsafeCast(dragboard.getContent(DataFormat.FILES));

        if (files == null || files.size() != 1) {
            return;
        }

        final File file = files.get(0);
        if (!canAccept(file)) return;

        handleFile(file);
    }

    /**
     * Handle a dropped file.
     *
     * @param file the dropped file.
     */
    protected void handleFile(@NotNull final File file) {
    }

    /**
     * Handle drag over.
     */
    private void dragOver(@NotNull final DragEvent dragEvent) {

        final Dragboard dragboard = dragEvent.getDragboard();
        final List<File> files = unsafeCast(dragboard.getContent(DataFormat.FILES));

        if (files == null || files.size() != 1) {
            return;
        }

        final File file = files.get(0);
        if (!canAccept(file)) return;

        final Set<TransferMode> transferModes = dragboard.getTransferModes();
        final boolean isCopy = transferModes.contains(TransferMode.COPY);

        dragEvent.acceptTransferModes(isCopy ? TransferMode.COPY : TransferMode.MOVE);
        dragEvent.consume();
    }

    protected boolean canAccept(@NotNull final File file) {
        return false;
    }

    /**
     * @return the label with name of the resource.
     */
    @NotNull
    protected Label getResourceLabel() {
        return notNull(resourceLabel);
    }
}
