package me.abhi.nitrogen.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.beans.ConstructorProperties;

public class ChunkPos {
    private final int x;
    private final int z;
    private final World world;

    public int getAbsoluteX(final int relativeX) {
        return fromRelative(this.getX(), relativeX);
    }

    public int getAbsoluteZ(final int absoluteZ) {
        return fromRelative(this.getZ(), absoluteZ);
    }

    public static ChunkPos fromChunk(final Chunk chunk) {
        return new ChunkPos(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    public static ChunkPos fromLocation(final Location l) {
        return new ChunkPos(l.getBlockX() >> 4, l.getBlockZ() >> 4, l.getWorld());
    }

    public static int toRelative(final int absolute) {
        return absolute & 0xF;
    }

    public static int fromRelative(final int chunk, final int relative) {
        return chunk << 4 | (relative & 0xF);
    }

    public boolean isLoaded() {
        return this.getWorld().isChunkLoaded(this.getX(), this.getZ());
    }

    @ConstructorProperties({"x", "z", "world"})
    public ChunkPos(final int x, final int z, final World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public World getWorld() {
        return this.world;
    }
}
