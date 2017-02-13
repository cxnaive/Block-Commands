package com.cxandy.BlockCommands;

import com.cxandy.BlockCommands.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by andychen on 17-2-5.
 */
@Mod(modid = blockcommands.MODID, name = blockcommands.NAME, version = blockcommands.VERSION,acceptableRemoteVersions="*", acceptedMinecraftVersions="[1.11.2]")
public class blockcommands {
    public static final String MODID = "blockcommands";
    public static final String NAME = "Block Commands";
    public static final String VERSION = "1.0.0";
    @Mod.Instance(blockcommands.MODID)
    public static blockcommands instance;
    public static CommonProxy proxy = new CommonProxy();
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Intalizing CommandBlocks.");
        proxy.preInit(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.serverStarting(event);
    }
}
