package com.ss.editor.ui.control.model.node.control.anim;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.control.ControlTreeNode;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.animation.PlaySettingsAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

import java.util.Collection;

/**
 * The implementation of node to show {@link AnimControl}.
 *
 * @author JavaSaBr
 */
public class AnimationControlTreeNode extends ControlTreeNode<AnimControl> {

    /**
     * The loop mode.
     */
    @NotNull
    private LoopMode loopMode;

    /**
     * The animation speed.
     */
    private float speed;

    /**
     * Instantiates a new Animation control model node.
     *
     * @param element  the element
     * @param objectId the object id
     */
    public AnimationControlTreeNode(@NotNull final AnimControl element, final long objectId) {
        super(element, objectId);
        this.loopMode = LoopMode.Loop;
        this.speed = 1.0F;
    }

    @Override
    public void fillContextMenu(@NotNull final NodeTree<?> nodeTree,
                                @NotNull final ObservableList<MenuItem> items) {
        items.add(new PlaySettingsAction(nodeTree, this));
        super.fillContextMenu(nodeTree, items);
    }

    /**
     * Update settings.
     *
     * @param loopMode the loop mode.
     * @param speed    the animation speed.
     */
    public void updateSettings(@NotNull LoopMode loopMode, final float speed) {
        this.loopMode = loopMode;
        this.speed = speed;
    }

    /**
     * Gets speed.
     *
     * @return the animation speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Gets loop mode.
     *
     * @return the loop mode.
     */
    @NotNull
    public LoopMode getLoopMode() {
        return loopMode;
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_ANIM_CONTROL;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.ANIMATION_16;
    }

    @Override
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }

    @NotNull
    @Override
    public Array<TreeNode<?>> getChildren(@NotNull final NodeTree<?> nodeTree) {

        final Array<TreeNode<?>> result = ArrayFactory.newArray(TreeNode.class);

        final AnimControl element = getElement();
        final Collection<String> animationNames = element.getAnimationNames();
        animationNames.forEach(name -> result.add(FACTORY_REGISTRY.createFor(element.getAnim(name))));

        result.addAll(super.getChildren(nodeTree));

        return result;
    }

    @Override
    public void notifyChildPreAdd(@NotNull final TreeNode<?> treeNode) {

        final AnimationTreeNode animationModelNode = (AnimationTreeNode) treeNode;
        animationModelNode.setControl(getElement());
        animationModelNode.setControlModelNode(this);

        super.notifyChildPreAdd(treeNode);
    }
}
