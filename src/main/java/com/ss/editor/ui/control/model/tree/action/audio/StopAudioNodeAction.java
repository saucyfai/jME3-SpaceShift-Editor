package com.ss.editor.ui.control.model.tree.action.audio;

import com.jme3.audio.AudioNode;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.spatial.AudioTreeNode;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.scene.image.Image;

/**
 * The action to stop an audio node.
 *
 * @author JavaSaBr
 */
public class StopAudioNodeAction extends AbstractNodeAction<ModelChangeConsumer> {

    /**
     * Instantiates a new Stop audio node action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public StopAudioNodeAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.STOP_16;
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_AUDIO_STOP;
    }

    @FXThread
    @Override
    protected void process() {
        super.process();

        final AudioTreeNode audioModelNode = (AudioTreeNode) getNode();
        final AudioNode audioNode = audioModelNode.getElement();

        EXECUTOR_MANAGER.addJMETask(audioNode::stop);
    }
}
