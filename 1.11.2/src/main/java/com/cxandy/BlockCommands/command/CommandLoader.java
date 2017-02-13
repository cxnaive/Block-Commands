package com.cxandy.BlockCommands.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by andychen on 17-2-5.
 */
public class CommandLoader {
    public CommandLoader(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandCreator());
        event.registerServerCommand(new CommandAdder());
        event.registerServerCommand(new CommandDeleter());
        event.registerServerCommand(new CommandClear());
        event.registerServerCommand(new DisableCommand());
        event.registerServerCommand(new CommandView());
        event.registerServerCommand(new CommandFinder());
        event.registerServerCommand(new CommandRemoveByPos());
    }
}
