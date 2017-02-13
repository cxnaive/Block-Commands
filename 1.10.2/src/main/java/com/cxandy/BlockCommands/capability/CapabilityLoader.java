package com.cxandy.BlockCommands.capability;

/**
 * Created by andychen on 17-2-5.
 */
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CapabilityLoader {
    @CapabilityInject(commandstore.class)
    public static Capability<commandstore> commandstorer;
    public CapabilityLoader(FMLPreInitializationEvent event){
        CapabilityManager.INSTANCE.register(commandstore.class,new CommandData.Storage(),CommandData.Implementation.class);
    }
}
