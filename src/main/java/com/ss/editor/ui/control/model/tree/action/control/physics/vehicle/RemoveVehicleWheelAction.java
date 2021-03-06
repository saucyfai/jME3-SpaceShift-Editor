package com.ss.editor.ui.control.model.tree.action.control.physics.vehicle;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.RemoveVehicleWheelOperation;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to remove a vehicle wheel from a control.
 *
 * @author JavaSaBr
 */
public class RemoveVehicleWheelAction extends AbstractNodeAction<ModelChangeConsumer> {

    /**
     * Instantiates a new Remove vehicle wheel action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public RemoveVehicleWheelAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_REMOVE;
    }

    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.REMOVE_12;
    }

    @FXThread
    @Override
    protected void process() {

        final TreeNode<?> node = getNode();
        final Object element = node.getElement();
        final TreeNode<?> nodeParent = notNull(node.getParent());
        final VehicleControl vehicleControl = (VehicleControl) nodeParent.getElement();
        final VehicleWheel vehicleWheel = (VehicleWheel) element;

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new RemoveVehicleWheelOperation(vehicleControl, vehicleWheel));
    }
}
