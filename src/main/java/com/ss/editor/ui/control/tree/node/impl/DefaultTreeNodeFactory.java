package com.ss.editor.ui.control.tree.node.impl;

import static com.ss.rlib.util.ClassUtils.unsafeCast;
import com.jme3.audio.AudioNode;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.ss.editor.extension.scene.SceneLayer;
import com.ss.editor.extension.scene.SceneNode;
import com.ss.editor.ui.control.layer.LayersRoot;
import com.ss.editor.ui.control.layer.node.LayersRootTreeNode;
import com.ss.editor.ui.control.layer.node.SceneLayerTreeNode;
import com.ss.editor.ui.control.model.node.spatial.*;
import com.ss.editor.ui.control.model.node.spatial.scene.SceneNodeTreeNode;
import com.ss.editor.ui.control.model.node.spatial.terrain.TerrainGridTreeNode;
import com.ss.editor.ui.control.model.node.spatial.terrain.TerrainQuadTreeNode;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.control.tree.node.TreeNodeFactory;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a tree node factory to make default nodes.
 *
 * @author JavaSaBr
 */
public class DefaultTreeNodeFactory implements TreeNodeFactory {

    @Override
    @Nullable
    public <T, V extends TreeNode<T>> V createFor(@Nullable final T element, final long objectId) {

        if (element instanceof LayersRoot) {
            return unsafeCast(new LayersRootTreeNode((LayersRoot) element, objectId));
        } else if (element instanceof TerrainGrid) {
            return unsafeCast(new TerrainGridTreeNode((TerrainGrid) element, objectId));
        } else if (element instanceof TerrainQuad) {
            return unsafeCast(new TerrainQuadTreeNode((TerrainQuad) element, objectId));
        } else if (element instanceof SceneNode) {
            return unsafeCast(new SceneNodeTreeNode((SceneNode) element, objectId));
        } else if (element instanceof SceneLayer) {
            return unsafeCast(new SceneLayerTreeNode((SceneLayer) element, objectId));
        } else if (element instanceof Mesh) {
            return unsafeCast(new MeshTreeNode((Mesh) element, objectId));
        } else if (element instanceof Geometry) {
            return unsafeCast(new GeometryTreeNode<>((Geometry) element, objectId));
        } else if (element instanceof AudioNode) {
            return unsafeCast(new AudioTreeNode((AudioNode) element, objectId));
        } else if (element instanceof AssetLinkNode) {
            return unsafeCast(new AssetLinkNodeTreeNode((AssetLinkNode) element, objectId));
        } else if (element instanceof Node) {
            return unsafeCast(new NodeTreeNode<>((Node) element, objectId));
        }

        return null;
    }
}
