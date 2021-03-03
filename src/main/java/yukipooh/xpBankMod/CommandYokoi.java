package yukipooh.xpBankMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CommandYokoi extends CommandBase {
    @Override
    public String getCommandName() {
        return "yokoi";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/yokoi";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            player.addChatMessage(new ChatComponentText("yarunee."));
        }
    }
}
