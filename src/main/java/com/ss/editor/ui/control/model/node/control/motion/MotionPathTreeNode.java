package com.ss.editor.ui.control.model.node.control.motion;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.cinematic.MotionPath;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.PositionTreeNode;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

/**
 * The implementation of the {@link TreeNode} to show a {@link MotionPath} in the tree.
 *
 * @author JavaSaBr
 */
public class MotionPathTreeNode extends TreeNode<MotionPath> {

    /**
     * Instantiates a new Motion path model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public MotionPathTreeNode(@NotNull final MotionPath element, final long objectId) {
        super(element, objectId);
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_MOTION_PATH;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.PATH_16;
    }

    @NotNull
    @Override
    public Array<TreeNode<?>> getChildren(@NotNull final NodeTree<?> nodeTree) {

        final MotionPath element = getElement();
        final int wayPoints = element.getNbWayPoints();

        final Array<TreeNode<?>> result = ArrayFactory.newArray(TreeNode.class);

        for (int i = 0; i < wayPoints; i++) {
            final PositionTreeNode modelNode = notNull(FACTORY_REGISTRY.createFor(element.getWayPoint(i)));
            modelNode.setName(Messages.MODEL_FILE_EDITOR_NODE_WAY_POINT + " #" + (i + 1));
            result.add(modelNode);
        }

        return result;
    }

    @Override
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }
}
