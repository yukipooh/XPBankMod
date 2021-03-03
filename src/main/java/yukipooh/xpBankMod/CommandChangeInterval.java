package yukipooh.xpBankMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import tv.twitch.chat.Chat;

public class CommandChangeInterval extends CommandBase {
    @Override
    public String getCommandName() {
        return "changeInt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/changeInt";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            int tmp = XPBankBlock.expInterval;
            XPBankBlock.expInterval = Integer.parseInt(args[0]);
            player.addChatMessage(new ChatComponentText(
                    "Hey Yo! You changed XP-Interval from " +
                            Integer.toString(tmp) + " to " +
                            Integer.toString(XPBankBlock.expInterval)));
        }
    }
}
