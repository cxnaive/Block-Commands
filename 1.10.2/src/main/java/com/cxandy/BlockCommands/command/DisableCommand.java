package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.capability.commandstore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by andychen on 17-2-6.
 */
public class DisableCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "commanddis";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commanddis Disable CommandSeter.";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 0 ){
            throw new WrongUsageException("/commanddis Disable CommandSeter.");
        }
        EntityPlayerMP entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
        if(entityPlayerMP == sender && entityPlayerMP.hasCapability(CapabilityLoader.commandstorer,null)){
            commandstore nowcommand = entityPlayerMP.getCapability(CapabilityLoader.commandstorer,null);
            nowcommand.setcommand(CommandHelper.DisableMode);
            sender.addChatMessage(new TextComponentTranslation("CommandSeter Disabled."));
        }
    }
}