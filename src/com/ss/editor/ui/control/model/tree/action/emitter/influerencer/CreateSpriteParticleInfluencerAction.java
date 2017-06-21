package com.ss.editor.ui.control.model.tree.action.emitter.influerencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;

import org.jetbrains.annotations.NotNull;

import tonegod.emitter.Messages;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.SpriteInfluencer;

/**
 * The action to create a {@link SpriteInfluencer} for a {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class CreateSpriteParticleInfluencerAction extends AbstractCreateParticleInfluencerAction {

    /**
     * Instantiates a new Create sprite particle influencer action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public CreateSpriteParticleInfluencerAction(@NotNull final AbstractNodeTree<ModelChangeConsumer> nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.PARTICLE_INFLUENCER_SPRITE;
    }

    @NotNull
    @Override
    protected ParticleInfluencer createInfluencer() {
        return new SpriteInfluencer();
    }
}
