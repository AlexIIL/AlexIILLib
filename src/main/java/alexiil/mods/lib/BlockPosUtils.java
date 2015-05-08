package alexiil.mods.lib;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockPosUtils {
    public static BlockPos readFromNBT(NBTTagCompound nbt, String tagPart) {
        int[] arr = nbt.getIntArray(tagPart);
        return new BlockPos(arr[0], arr[1], arr[2]);
    }

    public static BlockPos readFromNBT(NBTTagCompound nbt) {
        return readFromNBT(nbt, "pos");
    }

    public static NBTTagCompound saveToNBT(BlockPos pos, String tagPart) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setIntArray(tagPart, new int[] { pos.getX(), pos.getY(), pos.getZ() });
        return nbt;
    }

    public static NBTTagCompound saveToNBT(BlockPos pos) {
        return saveToNBT(pos, "pos");
    }

    public static BlockPos readFromByteBuf(ByteBuf buffer) {
        return new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public static void writeToByteBuf(ByteBuf buffer, BlockPos pos) {
        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
    }

    public static AxisAlignedBB getBB(BlockPos pos, int range) {
        double xMin = pos.getX() - range;
        return AxisAlignedBB.fromBounds(xMin, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
    }

    public static AxisAlignedBB getBB(Entity pos, int range) {
        return AxisAlignedBB.fromBounds(pos.posX - range, pos.posY - range, pos.posZ - range, pos.posX + range, pos.posY + range, pos.posZ + range);
    }

    public static AxisAlignedBB getBB(TileEntity tile, int range) {
        return getBB(tile.getPos(), range);
    }
}
