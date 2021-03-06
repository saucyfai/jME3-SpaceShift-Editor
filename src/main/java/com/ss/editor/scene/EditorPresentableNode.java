package com.ss.editor.scene;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.ss.editor.extension.scene.ScenePresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The node to present and edit objects on a scene which aren't models.
 *
 * @author JavaSaBr
 */
public class EditorPresentableNode extends Node {

    private class EditedNode extends Node implements VisibleOnlyWhenSelected, NoSelection, WrapperNode {

        private EditedNode(@NotNull final String name) {
            super(name);
        }

        @NotNull
        @Override
        public Object getWrappedObject() {
            return notNull(getObject());
        }

        @Override
        public void setCullHint(final CullHint hint) {
            super.setCullHint(hint);
            notNull(getModel()).setCullHint(hint);
        }
    }

    /**
     * The presented object.
     */
    @Nullable
    private ScenePresentable object;

    /**
     * The node to edit.
     */
    @NotNull
    private final EditedNode editedNode;

    /**
     * The view model.
     */
    @Nullable
    private Geometry model;

    /**
     * Instantiates a new Editor audio node.
     */
    public EditorPresentableNode() {
        this.editedNode = new EditedNode("EditedNode");
        attachChild(editedNode);
    }

    /**
     * Set an object.
     *
     * @param object the object.
     */
    public void setObject(@Nullable final ScenePresentable object) {
        this.object = object;
    }

    /**
     * Gets edited node.
     *
     * @return the edited node.
     */
    @NotNull
    public Node getEditedNode() {
        return editedNode;
    }

    /**
     * Get an object.
     *
     * @return the object.
     */
    @Nullable
    public ScenePresentable getObject() {
        return object;
    }

    /**
     * Get a model.
     *
     * @return the model.
     */
    @Nullable
    public Geometry getModel() {
        return model;
    }

    /**
     * Set a model.
     *
     * @param model the model.
     */
    public void setModel(@Nullable final Geometry model) {
        this.model = model;
    }

    @Override
    public void updateGeometricState() {

        final ScenePresentable object = getObject();

        if (object != null) {
            final Node editedNode = getEditedNode();
            object.setRotation(editedNode.getLocalRotation());
            object.setLocation(editedNode.getLocalTranslation());
            object.setScale(editedNode.getLocalScale());
        }

        super.updateGeometricState();
    }

    /**
     * Synchronize this node with presented object.
     */
    public void sync() {

        final ScenePresentable object = getObject();

        final Node editedNode = getEditedNode();
        editedNode.setLocalRotation(object.getRotation());
        editedNode.setLocalTranslation(object.getLocation());
        editedNode.setLocalScale(object.getScale());
    }

    /**
     * Update position and rotation of a model.
     */
    public void updateModel() {

        final ScenePresentable object = getObject();
        final Geometry model = getModel();
        if (model == null || object == null) return;

        // TODO implement getting parent
        /*final Node parent = object.getParent();

        if (parent != null) {
            setLocalTranslation(parent.getWorldTranslation());
            setLocalRotation(parent.getWorldRotation());
            setLocalScale(parent.getWorldScale());
        }*/

        final Node editedNode = getEditedNode();
        model.setLocalTranslation(editedNode.getWorldTranslation());
        model.setLocalRotation(editedNode.getWorldRotation());
        model.setLocalScale(editedNode.getWorldScale());
    }
}
