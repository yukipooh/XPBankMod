package yukipooh.xpBankMod;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;

public class XPBankBlock extends Block implements ITileEntityProvider {
    final int BLOCK_TEXTURE_NUM = 3;
    final String blockName = "XPBank";
    public static int expInterval = 10;    //1回の経験値のやりとりに使う経験値
    private IIcon[] iicon = new IIcon[BLOCK_TEXTURE_NUM];    //下,上,横

    public static int[] totalExperienceRequired = new int[1000];

    public XPBankBlock(Material material) {
        super(material);
        //クリエイティブタブの登録
        this.setCreativeTab(XPBankMod.tabXPBank);
        //硬さの設定
        this.setHardness(3.0F);
        //爆破耐性の設定
        this.setResistance(100.0F);
        //ブロックの上を歩いた時の音を登録する。
        this.setStepSound(Block.soundTypeMetal);
        //回収するのに必要なツールを設定する。
        this.setHarvestLevel("pickaxe", 1);
        //明るさの設定
        this.setLightLevel(0.0F);
        //ブロックの名前を設定
        this.setBlockName(blockName);
        //TileEntityを持つかどうか
        isBlockContainer = true;

    }

    /**メタデータ違いのテクスチャを登録する*/
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        for (int i = 0; i < BLOCK_TEXTURE_NUM; i ++) {
            this.iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityXPBankBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        switch (side){
            case 0: //下
                return iicon[0];

            case 1: //上
                return iicon[1];
            case 2:
            case 3:
            case 4:
            case 5: //横全部
                return iicon[2];
        }

        return iicon[meta];
    }

    //ブロックが右クリックされたら
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || !(tileEntity instanceof TileEntityXPBankBlock)){
            return false;
        }

        TileEntityXPBankBlock xpBankBlock = (TileEntityXPBankBlock) tileEntity;
        if(!world.isRemote){    //クライアント側で
            DepositXP(expInterval,player,world,x,y,z,xpBankBlock);
            player.addChatMessage(new ChatComponentText("Your current total experience is " + Integer.toString(player.experienceTotal)));
            player.addChatMessage(new ChatComponentText("Current Stored XP =  " + Integer.toString(xpBankBlock.getStoredXP())));
        }

        return true;
    }

    //ブロックが左クリックされたら
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || !(tileEntity instanceof TileEntityXPBankBlock)){
            return;
        }

        TileEntityXPBankBlock xpBankBlock = (TileEntityXPBankBlock) tileEntity;


        if(!world.isRemote){    //クライアント側で
            WithdrawXP(expInterval,player,world,x,y,z,xpBankBlock); //引き出す
            player.addChatMessage(new ChatComponentText("Your current total experience is " + Integer.toString(player.experienceTotal)));
            player.addChatMessage(new ChatComponentText("Current Stored XP =  " + Integer.toString(xpBankBlock.getStoredXP())));

        }
    }

    //ブロックが置かれたら
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || !(tileEntity instanceof TileEntityXPBankBlock)){
            return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
        }
        TileEntityXPBankBlock xpBankBlock = (TileEntityXPBankBlock) tileEntity;
        xpBankBlock.setStoredXP(0);

        return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
    }

    //ブロックが破壊されたら
    @Override
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        TileEntityXPBankBlock xpBankBlock = (TileEntityXPBankBlock) tileEntity;

        dropXpOnBlockBreak(world,x,y,z,xpBankBlock.getStoredXP());
        super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
    }

    void DepositXP(int exp, EntityPlayer player, World world, int x, int y, int z,TileEntityXPBankBlock tileEntityXPBankBlock){   //経験値を預ける
        if(player.experienceTotal > exp){
            player.addExperience(-exp);
            int currentStoredXP = tileEntityXPBankBlock.getStoredXP();
            int setData = currentStoredXP + exp;
            tileEntityXPBankBlock.setStoredXP(setData);

            int currentLevel;
            float currentExperienceOnBar;

            for(int i = 0; i < totalExperienceRequired.length - 1;i++){
                if(totalExperienceRequired[i] <= player.experienceTotal
                        && player.experienceTotal < totalExperienceRequired[i + 1]){
                    currentLevel = i;
                    player.experienceLevel = currentLevel;
                    currentExperienceOnBar = (float)(player.experienceTotal - totalExperienceRequired[i])/(float)player.xpBarCap();
                    player.experience = currentExperienceOnBar;
                    break;
                }
            }
        }
    }

    void WithdrawXP(int exp, EntityPlayer player,World world,int x,int y,int z,TileEntityXPBankBlock tileEntityXPBankBlock){  //経験値を引き出す
        //引き出したい経験値量よりもたまっている量が多かったら
        if(tileEntityXPBankBlock.getStoredXP() - exp >= 0){
            player.addExperience(exp);
            int currentStoredXP = tileEntityXPBankBlock.getStoredXP();
            int setData = currentStoredXP - exp;
            tileEntityXPBankBlock.setStoredXP(setData);
        }
    }
}
