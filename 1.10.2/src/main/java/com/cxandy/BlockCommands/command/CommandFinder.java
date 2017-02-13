package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.blockdatasave.blockdata;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by andychen on 17-2-6.
 */
public class CommandFinder extends CommandBase {
    @Override
    public String getCommandName() {
        return "commandfind";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commandfind [Distance] or /commandfind [PosX] [PosY] [PosZ] [Distance]";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int distance;
        BlockPos qpos;
        if(args.length == 4){
            int qposx,qposy,qposz;
            try{
                qposx = Integer.parseInt(args[0]);
                qposy = Integer.parseInt(args[1]);
                qposz = Integer.parseInt(args[2]);
                distance = Integer.parseInt(args[3]);
                qpos = new BlockPos(qposx,qposy,qposz);
            }
            catch (NumberFormatException ex){
                sender.addChatMessage(new TextComponentString("PosX,PosY,PosZ and Distance must be Intergers."));
                return;
            }
        }
        else if(args.length == 1){
            if(sender instanceof EntityPlayer){
                try {
                    distance = Integer.parseInt(args[0]);
                    EntityPlayer player = (EntityPlayer) sender;
                    qpos = player.getPosition();
                }
                catch (NumberFormatException ex){
                    ((EntityPlayer) sender).addChatComponentMessage(new TextComponentString("Distance must be a Interger."));
                    return;
                }
            }
            else{
                sender.addChatMessage(new TextComponentString("/commandfind [Distance] can only be excuted by player."));
                return;
            }
        }
        else{
            throw new WrongUsageException("/commandfind [Distance] or /commandfind [PosX] [PosY] [PosZ] [Distance]");
        }
        blockdata data = blockdata.get(sender.getEntityWorld());
        Set<Long> hasher = data.getallcommands().keySet();
        int count = 0;
        for(long o : hasher){
            BlockPos npos = BlockPos.fromLong(o);
            if(Math.abs(npos.getX()-qpos.getX()) <= distance && Math.abs(npos.getY()-qpos.getY()) <= distance && Math.abs(npos.getZ()-qpos.getZ()) <= distance){
                sender.addChatMessage(new TextComponentString("X:"+npos.getX()+" Y:"+npos.getY()+" Z:"+npos.getZ()+"."));
                ++count;
            }
        }
        sender.addChatMessage(new TextComponentString("Showd "+count+" blocks with commands in total."));
    }
}