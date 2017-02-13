package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.blockdatasave.blockdata;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by andychen on 17-2-6.
 */
public class CommandRemoveByPos extends CommandBase {
    @Override
    public String getCommandName() {
        return "commandremove";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commandremove [PosX] [PosY] [PosZ].";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 3 ){
            throw new WrongUsageException("/commandremove [PosX] [PosY] [PosZ].");
        }
        int posx,posy,posz;
        try {
            posx = Integer.parseInt(args[0]);
            posy = Integer.parseInt(args[1]);
            posz = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException ex){
            sender.addChatMessage(new TextComponentString("PosX,PosY,PosZ must be Intergers."));
            return;
        }
        BlockPos npos = new BlockPos(posx,posy,posz);
        blockdata data = blockdata.get(sender.getEntityWorld());
        if(data.getcommand(npos.toLong()) != null){
            data.remove(npos.toLong());
            sender.addChatMessage(new TextComponentString("Successfully removed commands on block "+"X:"+npos.getX()+" Y:"+npos.getY()+" Z:"+npos.getZ()+"."));
        }
        else{
            sender.addChatMessage(new TextComponentString("No commands on block"+"X:"+npos.getX()+" Y:"+npos.getY()+" Z:"+npos.getZ()+"."));
        }

    }
}