package com.cxandy.BlockCommands.common;
import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.command.CommandLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by andychen on 17-2-5.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        new CapabilityLoader(event);
    }
    public void init(FMLInitializationEvent event) {
        new EventLoader();
    }
    public void postInit(FMLPostInitializationEvent event) {

    }
    public void serverStarting(FMLServerStartingEvent event) {
        new CommandLoader(event);
    }
}
