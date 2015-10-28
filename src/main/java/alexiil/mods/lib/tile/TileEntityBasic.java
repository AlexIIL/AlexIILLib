package alexiil.mods.lib.tile;

import com.google.common.base.Throwables;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/** This is the base class for tile entity's */
public abstract class TileEntityBasic extends TileEntity implements IUpdatePlayerListBox {
    @Override
    public final void update() {
        try {
            if (worldObj.isRemote) {
                onClientTick();
            }
            else {
                onTick();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public abstract void onTick();

    public void onClientTick() {}

    public void dropItems() {}
}
