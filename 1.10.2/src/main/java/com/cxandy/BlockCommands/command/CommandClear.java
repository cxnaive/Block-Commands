package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.blockdatasave.blockdata;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by andychen on 17-2-6.
 */
public class CommandClear extends CommandBase {
    @Override
    public String getCommandName() {
        return "commandclear";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commandclear Clear all commands in this world.";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 0 ){
            throw new WrongUsageException("/commandclear Clear all commands in this world.");
        }
        blockdata data = blockdata.get(sender.getEntityWorld());
        data.clear();
        sender.addChatMessage(new TextComponentTranslation("Successfully cleared all commands in this world."));
    }
}