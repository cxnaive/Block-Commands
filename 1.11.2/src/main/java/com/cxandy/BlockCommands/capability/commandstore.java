package com.cxandy.BlockCommands.capability;

import com.cxandy.BlockCommands.command.CommandHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andychen on 17-2-5.
 */
public interface commandstore {
    public CommandHelper getcommand();
    public void setcommand(CommandHelper command);
    public long getlastpos();
    public void setlastpos(long pos);
    public void addcommandforexcute(CommandHelper command);
    public void removecommandforexcute(CommandHelper command);
    public List<CommandHelper> getcommandsforexcute();
    public void setcommandsforexcute(ArrayList<CommandHelper> commands);
}
