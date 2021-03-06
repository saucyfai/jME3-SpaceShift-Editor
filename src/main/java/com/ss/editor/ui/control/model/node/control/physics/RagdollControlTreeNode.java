package com.ss.editor.ui.control.model.node.control.physics;

import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.bullet.control.VehicleControl;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.control.ControlTreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of node to show {@link VehicleControl}.
 *
 * @author JavaSaBr
 */
public class RagdollControlTreeNode extends ControlTreeNode<KinematicRagdollControl> {

    /**
     * Instantiates a new Ragdoll control model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public RagdollControlTreeNode(@NotNull final KinematicRagdollControl element, final long objectId) {
        super(element, objectId);
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.DOLL_16;
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_RAGDOLL_CONTROL;
    }
}
