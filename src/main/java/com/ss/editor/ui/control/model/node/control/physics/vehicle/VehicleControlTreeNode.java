package com.ss.editor.ui.control.model.node.control.physics.vehicle;

import com.jme3.bullet.control.VehicleControl;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.control.physics.PhysicsObjectTreeNode;
import com.ss.editor.ui.control.model.tree.action.control.physics.vehicle.CreateVehicleWheelAction;
import com.ss.editor.ui.control.tree.NodeTree;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of node to show {@link VehicleControl}.
 *
 * @author JavaSaBr
 */
public class VehicleControlTreeNode extends PhysicsObjectTreeNode<VehicleControl> {

    /**
     * Instantiates a new Vehicle control model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public VehicleControlTreeNode(@NotNull final VehicleControl element, final long objectId) {
        super(element, objectId);
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.VEHICLE_16;
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_VEHICLE_CONTROL;
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree,
                                @NotNull final ObservableList<MenuItem> items) {

        items.add(new CreateVehicleWheelAction(nodeTree, this));

        super.fillContextMenu(nodeTree, items);
    }
}
