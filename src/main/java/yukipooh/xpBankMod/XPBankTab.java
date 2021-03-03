package yukipooh.xpBankMod;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

//CreativeTabsに新たなtabを追加するためのクラス(CreatuveTabsがreadonlyだったため作成)
public class XPBankTab extends CreativeTabs{
    final String tabLabel = "XPBankMod";    //タブのタイトル

    public XPBankTab(String label){
        super(label);
    }


    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(XPBankMod.blockXPBank);
    }

    @Override
    public String getTabLabel(){
        return this.tabLabel;
    }
}
