package com.ss.editor.ui.control.model.tree.action.operation;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;

import org.jetbrains.annotations.NotNull;

import tonegod.emitter.filter.TonegodTranslucentBucketFilter;

/**
 * The implementation of the {@link AbstractEditorOperation} to add a new {@link Spatial} to a {@link Node}.
 *
 * @author JavaSaBr
 */
public class AddChildOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The new child.
     */
    @NotNull
    private final Spatial newChild;

    /**
     * The parent.
     */
    @NotNull
    private final Node parent;

    /**
     * The flag to select added child.
     */
    private final boolean needSelect;

    /**
     * Instantiates a new Add child operation.
     *
     * @param newChild the new child
     * @param parent   the parent
     */
    public AddChildOperation(@NotNull final Spatial newChild, @NotNull final Node parent) {
        this(newChild, parent, true);
    }

    /**
     * Instantiates a new Add child operation.
     *
     * @param newChild the new child
     * @param parent   the parent
     */
    public AddChildOperation(@NotNull final Spatial newChild, @NotNull final Node parent, boolean needSelect) {
        this.newChild = newChild;
        this.parent = parent;
        this.needSelect = needSelect;
    }

    @Override
    protected void redoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJMETask(() -> {
            parent.attachChildAt(newChild, 0);

            final TonegodTranslucentBucketFilter filter = EDITOR.getTranslucentBucketFilter();
            filter.refresh();

            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyFXAddedChild(parent, newChild, 0, needSelect));
        });
    }

    @Override
    protected void undoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJMETask(() -> {
            parent.detachChild(newChild);
            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyFXRemovedChild(parent, newChild));
        });
    }
}
