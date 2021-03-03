package yukipooh.xpBankMod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityXPBankBlock extends TileEntity {

    protected int storedXP; //保存したり呼び出したするデータ

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("storedXP",storedXP);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        storedXP = nbt.getInteger("storedXP");
    }

    public int getStoredXP(){
        return this.storedXP;
    }

    public void setStoredXP(int xp){
        this.storedXP = xp;
    }

}
