package com.cxandy.BlockCommands.common;

import com.cxandy.BlockCommands.blockcommands;
import com.cxandy.BlockCommands.blockdatasave.blockdata;
import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.capability.CommandData;
import com.cxandy.BlockCommands.capability.commandstore;
import com.cxandy.BlockCommands.command.CommandHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by andychen on 17-2-5.
 */
public class EventLoader {
    public EventLoader(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof EntityPlayer) {
            EntityPlayer nowplayer = (EntityPlayer) event.getObject();
            if(nowplayer.getEntityWorld().isRemote) return;
            ICapabilitySerializable<NBTTagCompound> provider = new CommandData.ProviderPlayer();
            event.addCapability(new ResourceLocation(blockcommands.MODID + ":" + "command_store"), provider);
        }
    }
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        if(event.getWorld().isRemote) return;
        EntityPlayer player = event.getEntityPlayer();
        if(!player.getActiveHand().equals(event.getHand())) return;
        if(player.hasCapability(CapabilityLoader.commandstorer,null)){
            commandstore nowcommand = player.getCapability(CapabilityLoader.commandstorer,null);
            CommandHelper cmdstr = nowcommand.getcommand();
            if(cmdstr.isDisabled) return;
            BlockPos npos = event.getPos();
            long longpos = npos.toLong();
            blockdata data = blockdata.get(player.getEntityWorld());
            if(cmdstr.isAddingMode){
                String nowstr = data.getcommand(longpos);
                if(nowstr != null){
                    String[] cmds = nowstr.split("&&");
                    CommandHelper nowc = CommandHelper.loadString(cmds[cmds.length-1]);
                    if(nowc.MergeCommandFromAddingMode(cmdstr)){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i = 0;i<cmds.length-1;++i){
                            stringBuilder.append(cmds[i]);
                            stringBuilder.append("&&");
                        }
                        stringBuilder.append(nowc.toString());
                        data.update(longpos,stringBuilder.toString());
                        player.sendMessage(new TextComponentString("Successfully updated the last command on this block."));
                        player.sendMessage(new TextComponentString("Now the Command is:"));
                        player.sendMessage(new TextComponentString(nowc.printString()));
                    }
                    else{
                        player.sendMessage(new TextComponentString("Invalid Merge Request."));
                    }
                }
                else{
                    player.sendMessage(new TextComponentTranslation("This block dosen't have an command yet."));
                }
            }
            else if(cmdstr.isDeleteMode){
                if(data.getcommand(longpos) != null){
                    data.remove(longpos);
                    player.sendMessage(new TextComponentTranslation("Successfully removed command on this block."));
                }
                else{
                    player.sendMessage(new TextComponentTranslation("This block dosen't have an command yet."));
                }
            }
            else if(cmdstr.isViewMode){
                String datastr = data.getcommand(longpos);
                if(datastr == null) player.sendMessage(new TextComponentTranslation("This block dosen't have an command yet."));
                else{
                    player.sendMessage(new TextComponentTranslation("Commands on this block are:"));
                    String[] cmds = datastr.split("&&");
                    for(int i = 0;i<cmds.length;++i){
                        player.sendMessage(new TextComponentString(CommandHelper.loadString(cmds[i]).printString()));
                    }
                }
            }
            else{
                String nowcmd = data.getcommand(longpos);
                if(nowcmd == null) nowcmd = cmdstr.toString();
                else if(nowcmd.contains(cmdstr.toString())){
                    player.sendMessage(new TextComponentTranslation("This Block Already has this command."));
                    return;
                }
                else nowcmd = nowcmd + "&&" + cmdstr;
                data.update(longpos,nowcmd);
                player.sendMessage(new TextComponentTranslation("Successfully updated command on this block."));
            }
        }
    }
    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if(event.getWorld().isRemote) return;
        EntityPlayer player = event.getEntityPlayer();
        blockdata data = blockdata.get(player.getEntityWorld());
        String cmd = data.getcommand(event.getPos().toLong());
        if (cmd == null) return;
        String[] cmds = cmd.split("&&");
        ArrayList<CommandHelper> nowarr = new ArrayList<CommandHelper>();
        boolean excuting = true;
        for (int i = 0; i < cmds.length; ++i) {
            CommandHelper nowcmd = CommandHelper.loadString(cmds[i]);
            boolean isremove = false;
            if(nowcmd.isClick && excuting){
                boolean nowsuccess = nowcmd.PostExcute(player);
                if(nowcmd.isBreak && !nowsuccess) excuting = false;
                if(nowcmd.ExcuteAmountLimit == 0) isremove = true;
            }
            if(!isremove) nowarr.add(nowcmd);
        }
        if(nowarr.size() == 0) data.remove(event.getPos().toLong());
        else data.update(event.getPos().toLong(),CommandHelper.ListToString(nowarr));
        event.setCanceled(true);
        event.setResult(Event.Result.DENY);
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.getEntityLiving().getEntityWorld().isRemote) return;
        if(event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(player.hasCapability(CapabilityLoader.commandstorer,null)){
                commandstore nowcommand = player.getCapability(CapabilityLoader.commandstorer,null);
                ArrayList<CommandHelper> excutecommands = (ArrayList<CommandHelper>) nowcommand.getcommandsforexcute();
                ArrayList<CommandHelper> lastscommands = new ArrayList<CommandHelper>();
                long timenow = System.currentTimeMillis();
                if(excutecommands.size() != 0){
                    Iterator iterator = excutecommands.iterator();
                    while(iterator.hasNext()){
                        CommandHelper nowcmd = (CommandHelper) iterator.next();
                        if(nowcmd.TimerEnd <= timenow) {
                            nowcmd.TryExcute(player);
                        }
                        else lastscommands.add(nowcmd);
                    }
                    nowcommand.setcommandsforexcute(lastscommands);
                }
                BlockPos now = new BlockPos(player.posX,player.posY,player.posZ);
                if(nowcommand.getlastpos() != now.toLong()) {
                    nowcommand.setlastpos(now.toLong());
                    blockdata data = blockdata.get(player.getEntityWorld());
                    BlockPos feet = new BlockPos(now.getX(),now.getY()-1,now.getZ());
                    String cmd = data.getcommand(feet.toLong());
                    if (cmd == null) return;
                    String[] cmds = cmd.split("&&");
                    ArrayList<CommandHelper> nowarr = new ArrayList<CommandHelper>();
                    boolean excuting = true;
                    for (int i = 0; i < cmds.length; ++i) {
                        CommandHelper nowcmd = CommandHelper.loadString(cmds[i]);
                        boolean isremove = false;
                        if(nowcmd.isWalk && excuting){
                            boolean nowsuccess = nowcmd.PostExcute(player);
                            if(nowcmd.isBreak && !nowsuccess) excuting = false;
                            if(nowcmd.ExcuteAmountLimit == 0) isremove = true;
                        }
                        if(!isremove) nowarr.add(nowcmd);
                    }
                    if(nowarr.size() == 0) data.remove(feet.toLong());
                    else data.update(feet.toLong(),CommandHelper.ListToString(nowarr));
                }
            }
        }
    }
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        Capability<commandstore> capability = CapabilityLoader.commandstorer;
        Capability.IStorage<commandstore> storage = capability.getStorage();
        if (event.getOriginal().hasCapability(capability, null) && event.getEntityPlayer().hasCapability(capability, null)) {
            NBTBase nbt = storage.writeNBT(capability, event.getOriginal().getCapability(capability, null), null);
            storage.readNBT(capability, event.getEntityPlayer().getCapability(capability, null), null, nbt);
        }
    }
}
