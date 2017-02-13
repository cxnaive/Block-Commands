package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.capability.commandstore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by andychen on 17-2-10.
 */
public class CommandAdder extends CommandBase {
    @Override
    public String getCommandName() {
        return "commandadd";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commandadd [Notes] Enabled CommandAdder, used to update notes to a block's last added command. Can't use \"@Command\"";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1){
            throw new WrongUsageException("/commandadd [Notes] Enabled CommandAdder, used to update notes to a block's last added command. Can't use \"@Command\"");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < args.length; ++i){
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }
        String nowstr = stringBuilder.toString();
        CommandHelper nowadd = CommandHelper.ConvertStringFromSender(nowstr,CommandHelper.ConvertToAddMode);
        if(nowadd == null){
            throw new WrongUsageException("/commandadd [Notes] Enabled CommandAdder, used to update notes to a block's last added command. Can't use \"@Command\"");
        }
        EntityPlayerMP entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
        if(entityPlayerMP == sender && entityPlayerMP.hasCapability(CapabilityLoader.commandstorer,null)){
            commandstore nowcommand = entityPlayerMP.getCapability(CapabilityLoader.commandstorer,null);
            nowcommand.setcommand(nowadd);
            sender.addChatMessage(new TextComponentTranslation("New CommandAdder set:\"%1$s\".",nowadd.printString()));
        }
    }
}