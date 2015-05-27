package alexiil.mods.lib;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;

import com.google.common.collect.Lists;

import alexiil.mods.lib.SearchBox.SearchBoxIterator;
import alexiil.version.api.VersionedApi;

public class SearchUtils {
    @VersionedApi.Final
    public static SearchBox searchChunk(Chunk chunk) {
        BlockPos min = new BlockPos(chunk.xPosition << 2, 0, chunk.zPosition << 2);
        BlockPos max = min.add(15, 255, 15);
        return new SearchBox(min, max);
    }

    @VersionedApi.Final
    public static Iterable<BlockPos> searchFaces(BlockPos pos) {
        List<BlockPos> positions = Lists.newArrayList();
        for (EnumFacing dir : EnumFacing.VALUES) {
            positions.add(BlockPosUtils.move(pos, dir));
        }
        return positions;
    }

    @VersionedApi.Final
    public static Iterable<BlockPos> searchAround(BlockPos pos, int radius) {
        BlockPos min = pos.add(-radius, -radius, -radius);
        BlockPos max = pos.add(radius, radius, radius);
        SearchBox sb = new SearchBox(min, max);
        return new ExcludingIterable(sb, pos);
    }

    private static class ExcludingIterable implements Iterable<BlockPos> {
        private final SearchBox box;
        private final BlockPos excluded;

        public ExcludingIterable(SearchBox box, BlockPos positions) {
            this.box = box;
            excluded = positions;
        }

        @Override
        public Iterator<BlockPos> iterator() {
            return new ExcludingIterator();
        }

        private class ExcludingIterator implements Iterator<BlockPos> {
            private final SearchBoxIterator sbi = box.iterator();

            @Override
            public boolean hasNext() {
                if (!sbi.hasNext())
                    return false;
                BlockPos next = sbi.peek();
                if (next.equals(excluded))
                    next();
                return sbi.hasNext();
            }

            @Override
            public BlockPos next() {
                BlockPos pos = sbi.next();
                if (pos.equals(excluded)) {
                    return sbi.next();
                }
                return pos;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
