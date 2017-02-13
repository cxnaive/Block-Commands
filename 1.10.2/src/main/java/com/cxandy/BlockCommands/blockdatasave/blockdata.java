package com.cxandy.BlockCommands.blockdatasave;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andychen on 17-2-5.
 */
public class blockdata extends WorldSavedData {
    private HashMap<Long,String> mp = new HashMap<Long,String>();
    public blockdata(String name){
        super(name);
    }
    public void update(long pos, String cmd){
        mp.put(pos,cmd);
        this.markDirty();
    }
    public void remove(long pos){
        mp.remove(pos);
        this.markDirty();
    }
    public void clear(){
        mp.clear();
        this.markDirty();
    }
    public HashMap<Long,String> getallcommands(){
        return mp;
    }
    public String getcommand(long pos){
        return mp.get(pos);
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt){
        mp.clear();
        NBTTagList list = (NBTTagList) nbt.getTag("BlockCommandData");
        for(int i = list.tagCount() - 1;i >= 0;--i){
            NBTTagCompound compound = (NBTTagCompound) list.get(i);
            mp.put(compound.getLong("pos"),compound.getString("text"));
        }
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        NBTTagList list = new NBTTagList();
        Iterator iter = mp.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            NBTTagCompound compound = new NBTTagCompound();
            compound.setLong("pos",(Long)entry.getKey());
            compound.setString("text",(String)entry.getValue());
            list.appendTag(compound);
        }
        nbt.setTag("BlockCommandData",list);
        return nbt;
    }
    public static blockdata get(World world){
        WorldSavedData data = world.loadItemData(blockdata.class,"BlockCommandData");
        if(data == null){
            data = new blockdata("BlockCommandData");
            world.setItemData("BlockCommandData",data);
        }
        return (blockdata) data;
    }
}
