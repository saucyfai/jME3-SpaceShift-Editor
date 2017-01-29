package com.ss.editor.ui.control.model.property.builder.impl;

import com.jme3.bullet.collision.shapes.*;
import com.jme3.bullet.collision.shapes.infos.ChildCollisionShape;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.property.control.DefaultModelSinglePropertyControl;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.AbstractPropertyBuilder;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.ui.util.FXUtils;

/**
 * The implementation of the {@link PropertyBuilder} to build property controls for {@link CollisionShape} objects.
 *
 * @author JavaSaBr
 */
public class CollisionShapePropertyBuilder extends AbstractPropertyBuilder<ModelChangeConsumer> {

    private static final PropertyBuilder INSTANCE = new CollisionShapePropertyBuilder();

    public static PropertyBuilder getInstance() {
        return INSTANCE;
    }

    private CollisionShapePropertyBuilder() {
        super(ModelChangeConsumer.class);
    }

    @Override
    protected void buildForImpl(@NotNull final Object object, @Nullable final Object parent,
                                @NotNull final VBox container, @NotNull final ModelChangeConsumer changeConsumer) {

        if (object instanceof ChildCollisionShape) {
            build((ChildCollisionShape) object, container, changeConsumer);
        }

        if (!(object instanceof CollisionShape)) return;

        if (object instanceof BoxCollisionShape) {
            build((BoxCollisionShape) object, container, changeConsumer);
        } else if (object instanceof SphereCollisionShape) {
            build((SphereCollisionShape) object, container, changeConsumer);
        } else if (object instanceof CapsuleCollisionShape) {
            build((CapsuleCollisionShape) object, container, changeConsumer);
        } else if (object instanceof ConeCollisionShape) {
            build((ConeCollisionShape) object, container, changeConsumer);
        } else if (object instanceof CylinderCollisionShape) {
            build((CylinderCollisionShape) object, container, changeConsumer);
        }

        build((CollisionShape) object, container, changeConsumer);
    }

    private void build(final @NotNull CylinderCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final Vector3f halfExtents = shape.getHalfExtents();
        final float margin = shape.getMargin();
        final int axis = shape.getAxis();

        final DefaultModelSinglePropertyControl<CylinderCollisionShape, Vector3f> halfExtentsControl =
                new DefaultModelSinglePropertyControl<>(halfExtents, "Half extents", changeConsumer);

        halfExtentsControl.setSyncHandler(CylinderCollisionShape::getHalfExtents);
        halfExtentsControl.setToStringFunction(Vector3f::toString);
        halfExtentsControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CylinderCollisionShape, Float> marginControl =
                new DefaultModelSinglePropertyControl<>(margin, "Margin", changeConsumer);

        marginControl.setSyncHandler(CylinderCollisionShape::getMargin);
        marginControl.setToStringFunction(value -> Float.toString(value));
        marginControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CylinderCollisionShape, Integer> axisControl =
                new DefaultModelSinglePropertyControl<>(axis, "Axis", changeConsumer);

        axisControl.setSyncHandler(CylinderCollisionShape::getAxis);
        axisControl.setToStringFunction(value -> Integer.toString(value));
        axisControl.setEditObject(shape);

        FXUtils.addToPane(halfExtentsControl, container);
        FXUtils.addToPane(marginControl, container);
        FXUtils.addToPane(axisControl, container);
    }

    private void build(final @NotNull ConeCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final float radius = shape.getRadius();
        final float height = shape.getHeight();

        final DefaultModelSinglePropertyControl<ConeCollisionShape, Float> radiusControl =
                new DefaultModelSinglePropertyControl<>(radius, "Radius", changeConsumer);

        radiusControl.setSyncHandler(ConeCollisionShape::getRadius);
        radiusControl.setToStringFunction(value -> Float.toString(value));
        radiusControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<ConeCollisionShape, Float> heightControl =
                new DefaultModelSinglePropertyControl<>(height, "Height", changeConsumer);

        heightControl.setSyncHandler(ConeCollisionShape::getHeight);
        heightControl.setToStringFunction(value -> Float.toString(value));
        heightControl.setEditObject(shape);

        FXUtils.addToPane(radiusControl, container);
        FXUtils.addToPane(heightControl, container);
    }

    private void build(final @NotNull CapsuleCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final float radius = shape.getRadius();
        final float height = shape.getHeight();

        final int axis = shape.getAxis();

        final DefaultModelSinglePropertyControl<CapsuleCollisionShape, Float> radiusControl =
                new DefaultModelSinglePropertyControl<>(radius, "Radius", changeConsumer);

        radiusControl.setSyncHandler(CapsuleCollisionShape::getRadius);
        radiusControl.setToStringFunction(value -> Float.toString(value));
        radiusControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CapsuleCollisionShape, Float> heightControl =
                new DefaultModelSinglePropertyControl<>(height, "Height", changeConsumer);

        heightControl.setSyncHandler(CapsuleCollisionShape::getHeight);
        heightControl.setToStringFunction(value -> Float.toString(value));
        heightControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CapsuleCollisionShape, Integer> axisControl =
                new DefaultModelSinglePropertyControl<>(axis, "Axis", changeConsumer);

        axisControl.setSyncHandler(CapsuleCollisionShape::getAxis);
        axisControl.setToStringFunction(value -> Integer.toString(value));
        axisControl.setEditObject(shape);

        FXUtils.addToPane(radiusControl, container);
        FXUtils.addToPane(heightControl, container);
        FXUtils.addToPane(axisControl, container);
    }

    private void build(final @NotNull SphereCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final float radius = shape.getRadius();

        final DefaultModelSinglePropertyControl<SphereCollisionShape, Float> radiusControl =
                new DefaultModelSinglePropertyControl<>(radius, "Radius", changeConsumer);

        radiusControl.setSyncHandler(SphereCollisionShape::getRadius);
        radiusControl.setToStringFunction(value -> Float.toString(value));
        radiusControl.setEditObject(shape);

        FXUtils.addToPane(radiusControl, container);
    }

    private void build(final @NotNull BoxCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final Vector3f halfExtents = shape.getHalfExtents();

        final DefaultModelSinglePropertyControl<BoxCollisionShape, Vector3f> halfExtentsControl =
                new DefaultModelSinglePropertyControl<>(halfExtents, "Half extents", changeConsumer);

        halfExtentsControl.setSyncHandler(BoxCollisionShape::getHalfExtents);
        halfExtentsControl.setToStringFunction(Vector3f::toString);
        halfExtentsControl.setEditObject(shape);

        FXUtils.addToPane(halfExtentsControl, container);
    }

    private void build(final @NotNull CollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final Vector3f scale = shape.getScale();
        final long objectId = shape.getObjectId();
        final float margin = shape.getMargin();

        final DefaultModelSinglePropertyControl<CollisionShape, Vector3f> scaleControl =
                new DefaultModelSinglePropertyControl<>(scale, "Scale", changeConsumer);

        scaleControl.setSyncHandler(CollisionShape::getScale);
        scaleControl.setToStringFunction(Vector3f::toString);
        scaleControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CollisionShape, Long> objectIdControl =
                new DefaultModelSinglePropertyControl<>(objectId, "Object id", changeConsumer);

        objectIdControl.setSyncHandler(CollisionShape::getObjectId);
        objectIdControl.setToStringFunction(String::valueOf);
        objectIdControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<CollisionShape, Float> marginControl =
                new DefaultModelSinglePropertyControl<>(margin, "Margin", changeConsumer);

        marginControl.setSyncHandler(CollisionShape::getMargin);
        marginControl.setToStringFunction(String::valueOf);
        marginControl.setEditObject(shape);

        FXUtils.addToPane(scaleControl, container);
        FXUtils.addToPane(marginControl, container);
        FXUtils.addToPane(objectIdControl, container);
    }

    private void build(final @NotNull ChildCollisionShape shape, final @NotNull VBox container,
                       final @NotNull ModelChangeConsumer changeConsumer) {

        final Vector3f location = shape.location;
        final Matrix3f rotation = shape.rotation;

        final DefaultModelSinglePropertyControl<ChildCollisionShape, Vector3f> locationControl = new DefaultModelSinglePropertyControl<>(location, "Location", changeConsumer);
        locationControl.setSyncHandler(collisionShape -> collisionShape.location);
        locationControl.setToStringFunction(Vector3f::toString);
        locationControl.setEditObject(shape);

        final DefaultModelSinglePropertyControl<ChildCollisionShape, Matrix3f> rotationControl = new DefaultModelSinglePropertyControl<>(rotation, "Rotation", changeConsumer);
        rotationControl.setSyncHandler(collisionShape -> collisionShape.rotation);
        rotationControl.setToStringFunction(matrix3f -> new Quaternion().fromRotationMatrix(matrix3f).toString());
        rotationControl.setEditObject(shape);
        rotationControl.reload();

        FXUtils.addToPane(locationControl, container);
        FXUtils.addToPane(rotationControl, container);
    }
}
