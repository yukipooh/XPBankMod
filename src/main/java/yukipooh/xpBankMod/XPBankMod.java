package yukipooh.xpBankMod;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = XPBankMod.MODID,name = XPBankMod.MODNAME, version = XPBankMod.VERSION)
public class XPBankMod {
    @Mod.Instance("XPBankMod")
    public static XPBankMod instance;

    public static final String MODID = "XPBankMod";
    public static final String MODNAME = "XPBank Mod";
    public static final String VERSION = "1.0"; //Modのversion

    public static Block blockXPBank;

    public static CreativeTabs tabXPBank = new XPBankTab("XPBankTab");  //mod用に新しいtabを作成

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Blockを継承したクラスのインスタンスを生成し、代入する。
        blockXPBank = new XPBankBlock(Material.rock)
                //システム名の登録
                .setBlockName("blockXPBank")
                //テクスチャ名の登録
                .setBlockTextureName("xpbankmod:xpbank_block");
        //GameRegistryへの登録
        GameRegistry.registerBlock(blockXPBank,ItemXPBankBlock.class, "blockXPBank");
        GameRegistry.registerTileEntity(TileEntityXPBankBlock.class, "TileEntityXPBankBlock");
        LanguageRegistry.addName(blockXPBank,"XPBank");


    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //鉄インゴット8個とガラス瓶１個でXPBank1個を作るレシピ
        GameRegistry.addRecipe(new ItemStack(XPBankMod.blockXPBank),
                "XYX", "XXX", "XXX", 'X', Items.iron_ingot, 'Y', Items.glass_bottle);

        for(int i = 0; i < XPBankBlock.totalExperienceRequired.length;i++){
            if(0 <= i && i < 17){
                XPBankBlock.totalExperienceRequired[i] = 17 * i;
            }else if(17 <= i && i < 30){
                XPBankBlock.totalExperienceRequired[i] = (int)(1.5 * i * i - 29.5*i+360);
            }else{
                XPBankBlock.totalExperienceRequired[i] = (int)(3.5*i*i-151.5*i+2220);
            }
        }
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        System.out.println("=== FMLServerStartingEvent fired ===");
        event.registerServerCommand(new CommandChangeInterval());
        event.registerServerCommand(new CommandYokoi());
    }

}
