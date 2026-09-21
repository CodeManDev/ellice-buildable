



package dev.felix.ellice.compat;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.core.Direction;
import java.util.Collection;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.function.Function;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.List;
import net.minecraft.world.phys.AABB;

public final class CompatForecastService
{
    private CompatForecastService() {
    }
    
    public static List<RotationVector> forecast(AABB move, final List<RotationVector> list, final double n, final boolean b, final double n2, final boolean b2, final Function<AABB, List<VoxelShape>> function, final Predicate<AABB> predicate) {
        final AABB aabb = move;
        final ArrayList coll = new ArrayList();
        coll.add(new RotationVector(0.0, 0.0, 0.0));
        double n3 = b ? 0.0 : ((n - Double.longBitsToDouble(4590429028186199163L)) * Double.longBitsToDouble(4607002274814922588L));
        boolean b3 = b;
        double n4 = (b2 && !b) ? Double.longBitsToDouble(4606371770867090719L) : 1.0;
        for (int i = 0; i < list.size(); ++i) {
            final RotationVector multiply = list.get(i).multiply(n4);
            final double n5 = b3 ? Double.longBitsToDouble(-4632943008668576645L) : n3;
            RotationVector mcon7oge5i83 = mcon7oge5i83(move, new RotationVector(multiply.x(), n5, multiply.z()), function);
            final boolean b4 = Math.abs(mcon7oge5i83.x() - multiply.x()) > Double.longBitsToDouble(4517329193108106637L) || Math.abs(mcon7oge5i83.z() - multiply.z()) > Double.longBitsToDouble(4517329193108106637L);
            if ((b3 || (n5 < 0.0 && Math.abs(mcon7oge5i83.y() - n5) > Double.longBitsToDouble(4517329193108106637L))) && b4 && n2 > 0.0) {
                final double y = mcon7oge5i83(move, new RotationVector(0.0, n2, 0.0), function).y();
                final AABB move2 = move.move(0.0, y, 0.0);
                final RotationVector mcon7oge5i84 = mcon7oge5i83(move2, new RotationVector(multiply.x(), 0.0, multiply.z()), function);
                final double y2 = mcon7oge5i83(move2.move(mcon7oge5i84.x(), 0.0, mcon7oge5i84.z()), new RotationVector(0.0, n5 - y, 0.0), function).y();
                if (mcon7oge5i84.x() * mcon7oge5i84.x() + mcon7oge5i84.z() * mcon7oge5i84.z() > mcon7oge5i83.x() * mcon7oge5i83.x() + mcon7oge5i83.z() * mcon7oge5i83.z()) {
                    mcon7oge5i83 = new RotationVector(mcon7oge5i84.x(), y + y2, mcon7oge5i84.z());
                }
            }
            move = move.move(mcon7oge5i83.x(), mcon7oge5i83.y(), mcon7oge5i83.z());
            if (!predicate.test(move)) {
                while (coll.size() <= list.size()) {
                    coll.add(coll.getLast());
                }
                break;
            }
            b3 = (n5 < 0.0 && Math.abs(mcon7oge5i83.y() - n5) > Double.longBitsToDouble(4517329193108106637L));
            if (!b3 && b2) {
                n4 *= Double.longBitsToDouble(4606371770867090719L);
            }
            n3 = ((b3 || Math.abs(mcon7oge5i83.y() - n5) > Double.longBitsToDouble(4517329193108106637L)) ? 0.0 : ((n5 - Double.longBitsToDouble(4590429028186199163L)) * Double.longBitsToDouble(4607002274814922588L)));
            coll.add(new RotationVector(move.minX - aabb.minX, move.minY - aabb.minY, move.minZ - aabb.minZ));
        }
        return (List<RotationVector>)List.copyOf((Collection<?>)coll);
    }
    
    private static RotationVector mcon7oge5i83(AABB aabb, final RotationVector rotationVector, final Function<AABB, List<VoxelShape>> function) {
        final List list = function.apply(aabb.expandTowards(rotationVector.x(), rotationVector.y(), rotationVector.z()).inflate(Double.longBitsToDouble(4547007122018943789L)));
        final double collide = Shapes.collide(Direction.Axis.Y, aabb, (Iterable)list, rotationVector.y());
        aabb = aabb.move(0.0, collide, 0.0);
        final double x = rotationVector.x();
        double a = rotationVector.z();
        final boolean b = Math.abs(x) < Math.abs(a);
        if (b) {
            a = Shapes.collide(Direction.Axis.Z, aabb, (Iterable)list, a);
            aabb = aabb.move(0.0, 0.0, a);
        }
        final double collide2 = Shapes.collide(Direction.Axis.X, aabb, (Iterable)list, x);
        aabb = aabb.move(collide2, 0.0, 0.0);
        if (!b) {
            a = Shapes.collide(Direction.Axis.Z, aabb, (Iterable)list, a);
        }
        return new RotationVector(collide2, collide, a);
    }
}
