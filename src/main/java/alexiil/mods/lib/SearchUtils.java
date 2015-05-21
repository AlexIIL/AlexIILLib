package alexiil.mods.lib;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;

import com.google.common.collect.Lists;

public class SearchUtils {
    public static Iterable<BlockPos> searchChunk(Chunk chunk) {
        BlockPos min = new BlockPos(chunk.xPosition << 2, 0, chunk.zPosition << 2);
        BlockPos max = min.add(15, 255, 15);
        return new SearchBox(min, max);
    }

    public static Iterable<BlockPos> searchAround(BlockPos pos) {
        List<BlockPos> positions = Lists.newArrayList();
        for (EnumFacing dir : EnumFacing.VALUES) {
            positions.add(BlockPosUtils.move(pos, dir));
        }
        return positions;
    }
}
