package me.flashyreese.mods.ping.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PingWrapper {

    public static PingWrapper readFromBuffer(ByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        int color = buffer.readInt();
        PingType type = PingType.values()[buffer.readInt()];
        return new PingWrapper(new BlockPos(x, y, z), color, type);
    }

    public final BlockPos pos;
    public final int color;
    public final PingType type;
    public boolean isOffscreen = false;
    public float screenX;
    public float screenY;
    public int animationTimer = 20;
    public int timer;

    public PingWrapper(BlockPos pos, int color, PingType type) {
        this.pos = pos;
        this.color = color;
        this.type = type;
    }

    public Box getBox() {
        return new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    public void writeToBuffer(ByteBuf buffer) {
        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
        buffer.writeInt(color);
        buffer.writeInt(type.ordinal());
    }
}