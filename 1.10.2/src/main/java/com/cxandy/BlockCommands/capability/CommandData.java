package com.cxandy.BlockCommands.capability;

import com.cxandy.BlockCommands.command.CommandHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andychen on 17-2-5.
 */
public class CommandData {
    public static class Storage implements Capability.IStorage<commandstore>{
        @Override
        public NBTBase writeNBT(Capability<commandstore> capability, commandstore instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("commandString",instance.getcommand().toString());
            nbt.setLong("LastPos",instance.getlastpos());
            NBTTagList commandlists = new NBTTagList();
            for (CommandHelper o : instance.getcommandsforexcute()) commandlists.appendTag(new NBTTagString(o.toString()));
            nbt.setTag("ExcuteCommands",commandlists);
            return nbt;
        }
        @Override
        public void readNBT(Capability<commandstore> capability, commandstore instance, EnumFacing side, NBTBase nbt){
            NBTTagCompound nbtnow = (NBTTagCompound) nbt;
            instance.setcommand(CommandHelper.loadString(nbtnow.getString("commandString")));
            instance.setlastpos(nbtnow.getLong("LastPos"));
            NBTTagList commanlists = (NBTTagList) nbtnow.getTag("ExcuteCommands");
            ArrayList<CommandHelper> nowcommands = new ArrayList<CommandHelper>();
            for(int i = 0;i < commanlists.tagCount();++i){
                nowcommands.add(CommandHelper.loadString(commanlists.getStringTagAt(i)));
            }
            instance.setcommandsforexcute(nowcommands);
        }
    }
    public static class Implementation implements commandstore {
        private CommandHelper command = CommandHelper.DisableMode;
        private List<CommandHelper> excutecommands = new ArrayList<CommandHelper>();
        private long LastPos = -1;
        @Override
        public CommandHelper getcommand(){
            return command;
        }
        @Override
        public void setcommand(CommandHelper str){
            command = str;
        }
        @Override
        public long getlastpos() {
            return LastPos;
        }
        @Override
        public void setlastpos(long pos){
            LastPos = pos;
        }
        @Override
        public void addcommandforexcute(CommandHelper command){
            excutecommands.add(command);
        }
        @Override
        public void removecommandforexcute(CommandHelper command){
            excutecommands.remove(command);
        }
        @Override
        public List<CommandHelper> getcommandsforexcute(){
            return excutecommands;
        }
        @Override
        public void setcommandsforexcute(ArrayList<CommandHelper> commands){
            excutecommands = commands;
        }
    }
    public static class ProviderPlayer implements ICapabilitySerializable<NBTTagCompound>{
        private commandstore command = new Implementation();
        private Capability.IStorage<commandstore> storage =CapabilityLoader.commandstorer.getStorage();

        @Override
        public boolean hasCapability(Capability<?> capability,EnumFacing facing){
            return CapabilityLoader.commandstorer.equals(capability);
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing){
            if(CapabilityLoader.commandstorer.equals(capability)){
                @SuppressWarnings("unchecked")
                T result = (T) command;
                return result;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT(){
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("command",storage.writeNBT(CapabilityLoader.commandstorer,command,null));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound){
            NBTTagCompound str = (NBTTagCompound) compound.getTag("command");
            storage.readNBT(CapabilityLoader.commandstorer,command,null,str);
        }
    }
}
