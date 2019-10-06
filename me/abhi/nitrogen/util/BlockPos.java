package me.abhi.nitrogen.util;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.beans.ConstructorProperties;
import java.util.concurrent.atomic.AtomicReference;

public class BlockPos {
    private final int x;
    private final int y;
    private final int z;
    private final World world;
    private final AtomicReference<Object> chunkPos;

    public BlockPos(final Location l) {
        this(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld());
    }

    public Location toLocation() {
        return new Location(this.getWorld(), (double) this.getX(), (double) this.getY(), (double) this.getZ());
    }

    public int distanceSquared(final BlockPos other) {
        Preconditions.checkArgument(other.getWorld().equals(this.getWorld()), (Object) "Can't compare the distances of different worlds");
        return square(this.x - other.x) + square(this.y - other.y) + square(this.z - other.z);
    }

    public Material getTypeAt() {
        return Material.getMaterial(this.getWorld().getBlockTypeIdAt(this.getX(), this.getY(), this.getZ()));
    }

    public byte getDataAt() {
        return this.getWorld().getBlockAt(this.getX(), this.getY(), this.getZ()).getData();
    }

    private static int square(final int i) {
        return i * i;
    }

    @ConstructorProperties({"x", "y", "z", "world"})
    public BlockPos(final int x, final int y, final int z, final World world) {
        this.chunkPos = new AtomicReference<Object>();
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockPos)) {
            return false;
        }
        final BlockPos other = (BlockPos) o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        if (this.getZ() != other.getZ()) {
            return false;
        }
        final Object this$world = this.getWorld();
        final Object other$world = other.getWorld();
        if (this$world == null) {
            if (other$world == null) {
                return true;
            }
        } else if (this$world.equals(other$world)) {
            return true;
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BlockPos;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getZ();
        final Object $world = this.getWorld();
        result = result * 59 + (($world == null) ? 0 : $world.hashCode());
        return result;
    }

    public BlockPos withX(final int x) {
        return (this.x == x) ? this : new BlockPos(x, this.y, this.z, this.world);
    }

    public BlockPos withY(final int y) {
        return (this.y == y) ? this : new BlockPos(this.x, y, this.z, this.world);
    }

    public BlockPos withZ(final int z) {
        return (this.z == z) ? this : new BlockPos(this.x, this.y, z, this.world);
    }

    public ChunkPos getChunkPos() {
        Object value = this.chunkPos.get();
        if (value == null) {
            synchronized (this.chunkPos) {
                value = this.chunkPos.get();
                if (value == null) {
                    final ChunkPos actualValue = new ChunkPos(this.getX() >> 4, this.getZ() >> 4, this.getWorld());
                    value = ((actualValue == null) ? this.chunkPos : actualValue);
                    this.chunkPos.set(value);
                }
            }
        }
        return (ChunkPos) ((value == this.chunkPos) ? null : value);
    }
}
