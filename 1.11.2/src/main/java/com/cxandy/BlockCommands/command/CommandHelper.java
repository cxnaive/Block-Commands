package com.cxandy.BlockCommands.command;


import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.capability.commandstore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by andychen on 17-2-9.
 */
public class CommandHelper {
    private static final String StringSpliter ="@Split";
    private static final String CommandBeginNote="@Command";
    private static final String MessageSendNote ="@Message";
    private static final String PunishCommandBeginNote="@Punish";
    private static final String CommandBypassNote="@Bypass";
    private static final String CommandisWalkNote="@isWalk";
    private static final String CommandisClickNote="@isClick";
    private static final String CommandisBreakNote="@isBreak";
    private static final String CommandRequriedPlayerNote="@Player";
    private static final String CommandRequriedItemNote="@Item";
    private static final String CommandExcuteAmountLimitNote="@Amount";
    private static final String CommandCooldownSecondsNote="@Cooldown";
    private static final String CommandDelaySecondsNote="@Delay";
    private static final String NoSpecialItem = "@AnyItem";
    private static final String NoSpecialPlayer = "@Anyone";
    private static final String NullCommand="@NullCommand";
    private static final String NullMessage="@NullMessage";
    private static final String CommandDelMode = "CommandDelete";
    private static final String CommandViewMode = "CommandView";
    private static final String CommandDisableMode = "Disabled";
    public static final int Notdefined = -1;
    public String comandstr = NullCommand;
    public String messagestr = NullMessage;
    public String punishstr = NullCommand;
    public boolean isViewMode = false;
    public boolean isDeleteMode = false;
    public boolean isAddingMode = false;
    public boolean isCreatingMode = false;
    public boolean isDisabled = true;
    public boolean isBypass = false;
    public boolean isWalk = false;
    public boolean isClick = false;
    public boolean isBreak = false;
    public int ExcuteAmountLimit = Notdefined;
    public int CooldownSeconds = Notdefined;
    public int DelaySeconds = Notdefined;
    public int RequiredItemAmount = Notdefined;
    public long TimerBegin = Notdefined;
    public long TimerEnd = Notdefined;
    public String RequiredItemName = NoSpecialItem;
    public String RequiredPlayerName = NoSpecialPlayer;
    public static final boolean WalkPost = true;
    public static final boolean ClickPost = false;
    public static final boolean ConvertToCommand = true;
    public static final boolean ConvertToAddMode = false;
    public static final CommandHelper ViewMode = ConvertStringFromSender(CommandViewMode, ConvertToCommand);
    public static final CommandHelper DeleteMode = ConvertStringFromSender(CommandDelMode, ConvertToCommand);
    public static final CommandHelper DisableMode = ConvertStringFromSender(CommandDisableMode, ConvertToCommand);
    public CommandHelper(){}
    private static int GetNextIndex(int BeginIndex,int[] idxs){
        for(int i = 0;i<idxs.length;++i) {
            if(idxs[i] > BeginIndex) return idxs[i];
        }
        return Notdefined;
    }
    public static CommandHelper ConvertStringFromSender(String str,boolean type){
        CommandHelper now = new CommandHelper();
        now.isViewMode = now.isDeleteMode = now.isAddingMode = now.isDisabled = now.isCreatingMode = false;
        if(str.equals(CommandDelMode)) now.isDeleteMode = true;
        else if(str.equals(CommandViewMode)) now.isViewMode = true;
        else if(str.equals(CommandDisableMode)) now.isDisabled = true;
        else {
            try{
                if(type == ConvertToCommand) {
                    now.isAddingMode = false;
                    now.isCreatingMode = true;
                }
                else {
                    now.isAddingMode = true;
                    now.isCreatingMode = false;
                }
                int[] idxs = new int[13];
                int commandbeginidx = idxs[0] = str.indexOf(CommandBeginNote);
                int messagebeginidx = idxs[1] = str.indexOf(MessageSendNote);
                int punishbeginidx = idxs[2] = str.indexOf(PunishCommandBeginNote);
                int bypassidx = idxs[3] = str.indexOf(CommandBypassNote);
                int iswalkidx = idxs[4] = str.indexOf(CommandisWalkNote);
                int isClickidx = idxs[5] = str.indexOf(CommandisClickNote);
                int isBreakidx = idxs[6] = str.indexOf(CommandisBreakNote);
                int amountidx = idxs[7] = str.indexOf(CommandExcuteAmountLimitNote);
                int cooldownidx = idxs[8] = str.indexOf(CommandCooldownSecondsNote);
                int delayidx = idxs[9] = str.indexOf(CommandDelaySecondsNote);
                int itemidx = idxs[10] = str.indexOf(CommandRequriedItemNote);
                int playeridx = idxs[11] = str.indexOf(CommandRequriedPlayerNote);
                idxs[12] = str.length();
                Arrays.sort(idxs);
                if(now.isCreatingMode && commandbeginidx == Notdefined) return null;
                if(now.isAddingMode && commandbeginidx != Notdefined) return null;
                if(commandbeginidx != Notdefined){
                    commandbeginidx += CommandBeginNote.length()-1;
                    if(commandbeginidx + 2 >= str.length() || (str.charAt(commandbeginidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(commandbeginidx,idxs);
                    now.comandstr = str.substring(commandbeginidx+2,nextidx).trim();
                }
                if(messagebeginidx != Notdefined){
                    messagebeginidx += MessageSendNote.length()-1;
                    if(messagebeginidx + 2 >= str.length() || (str.charAt(messagebeginidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(messagebeginidx,idxs);
                    now.messagestr = str.substring(messagebeginidx+2,nextidx).trim();
                }
                if(punishbeginidx != Notdefined){
                    punishbeginidx += PunishCommandBeginNote.length()-1;
                    if(punishbeginidx + 2 >= str.length() || (str.charAt(punishbeginidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(punishbeginidx,idxs);
                    now.punishstr = str.substring(punishbeginidx+2,nextidx).trim();
                }
                if(amountidx != Notdefined){
                    amountidx += CommandExcuteAmountLimitNote.length()-1;
                    if(amountidx + 2 >= str.length() || (str.charAt(amountidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(amountidx,idxs);
                    String nstr = str.substring(amountidx+2,nextidx).trim();
                    now.ExcuteAmountLimit = Integer.parseInt(nstr);
                }
                if(cooldownidx != Notdefined){
                    cooldownidx += CommandCooldownSecondsNote.length()-1;
                    if(cooldownidx + 2 >= str.length() || (str.charAt(cooldownidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(cooldownidx,idxs);
                    String nstr = str.substring(cooldownidx+2,nextidx).trim();
                    now.CooldownSeconds = Integer.parseInt(nstr);
                }
                if(delayidx != Notdefined){
                    delayidx += CommandDelaySecondsNote.length()-1;
                    if(delayidx + 2 >= str.length() || (str.charAt(delayidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(delayidx,idxs);
                    String nstr = str.substring(delayidx+2,nextidx).trim();
                    now.DelaySeconds = Integer.parseInt(nstr);
                }
                if(itemidx != Notdefined){
                    itemidx += CommandRequriedItemNote.length()-1;
                    if(itemidx + 2 >= str.length() || (str.charAt(itemidx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(itemidx,idxs);
                    int nextsplitidx = str.indexOf(':',itemidx+2);
                    if(nextsplitidx == Notdefined || nextsplitidx >= nextidx) return null;
                    now.RequiredItemName = str.substring(itemidx+2,nextsplitidx).trim();
                    String nstr = str.substring(nextsplitidx+1,nextidx).trim();
                    now.RequiredItemAmount = Integer.parseInt(nstr);
                }
                if(playeridx != Notdefined){
                    playeridx += CommandRequriedPlayerNote.length()-1;
                    if(playeridx + 2 >= str.length() || (str.charAt(playeridx+1) != ':') ) return null;
                    int nextidx = GetNextIndex(playeridx,idxs);
                    now.RequiredPlayerName = str.substring(playeridx+2,nextidx).trim();
                }
                now.isBypass = bypassidx != Notdefined;
                now.isWalk = iswalkidx != Notdefined;
                now.isClick = isClickidx != Notdefined;
                now.isBreak = isBreakidx != Notdefined;
            }
            catch (Exception ex){
                return null;
            }
        }
        return now;
    }
    public static String ListToString(ArrayList<CommandHelper> arrayList){
        StringBuilder stringBuilder = new StringBuilder();
        for (CommandHelper anArrayList : arrayList) {
            stringBuilder.append(anArrayList.toString());
            stringBuilder.append("&&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
    public static CommandHelper loadString(String str){
        CommandHelper now = new CommandHelper();
        now.isViewMode = now.isDeleteMode = now.isAddingMode = now.isDisabled =now.isCreatingMode= false;
        if(str.equals(CommandDelMode)) now.isDeleteMode = true;
        else if(str.equals(CommandViewMode)) now.isViewMode = true;
        else if(str.equals(CommandDisableMode)) now.isDisabled = true;
        else {
            String settings[] = str.split(StringSpliter);
            now.comandstr = settings[0];
            now.messagestr = settings[1];
            now.punishstr= settings[2];
            now.isBypass = settings[3].equals("1");
            now.isClick = settings[4].equals("1");
            now.isWalk = settings[5].equals("1");
            now.ExcuteAmountLimit = Integer.parseInt(settings[6]);
            now.CooldownSeconds = Integer.parseInt(settings[7]);
            now.DelaySeconds = Integer.parseInt(settings[8]);
            now.TimerBegin = Long.parseLong(settings[9]);
            now.TimerEnd = Long.parseLong(settings[10]);
            now.RequiredPlayerName = settings[11];
            now.RequiredItemName = settings[12];
            now.RequiredItemAmount = Integer.parseInt(settings[13]);
            if(settings.length >= 15) now.isBreak = settings[14].equals("1");
            if(now.comandstr.equals(NullCommand)) now.isAddingMode = true;
            else now.isCreatingMode = true;
        }
        return now;
    }
    private void Excute(EntityPlayer player,String cmdstr){
        MinecraftServer nowserver = player.getServer();
        ICommandManager commandManager = nowserver.getCommandManager();
        boolean isOp = nowserver.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null;
        if(!isOp && isBypass) nowserver.getPlayerList().addOp(player.getGameProfile());
        commandManager.executeCommand(player,cmdstr);
        if(!isOp && isBypass) nowserver.getPlayerList().removeOp(player.getGameProfile());
    }
    public boolean PostExcute(EntityPlayer player){
        long timenow = System.currentTimeMillis();
        if(CooldownSeconds != CommandHelper.Notdefined && TimerEnd > timenow) return true;
        boolean FaildToExcuteCausedByPlayer = false;
        if((!RequiredPlayerName.equals(CommandHelper.NoSpecialPlayer)) && (!RequiredPlayerName.equals(player.getName()))) FaildToExcuteCausedByPlayer = true;
        if(!RequiredItemName.equals(NoSpecialItem)){
            Item required;
            try {
                required = CommandBase.getItemByText(player,RequiredItemName);
            }
            catch (NumberInvalidException ex){
                player.sendMessage(new TextComponentTranslation("Invaild Requried Item:\" %1$s \".",RequiredItemName));
                return true;
            }
            int itemcnt = 0;
            for(ItemStack stack:player.inventory.mainInventory){
                if(stack != null && stack.getItem().getClass() == required.getClass()){
                    itemcnt += stack.getCount();
                }
            }
            if(itemcnt == 0 || (RequiredItemAmount != Notdefined && itemcnt < RequiredItemAmount)){
                player.sendMessage(new TextComponentTranslation("Requried Item:\" %1$s \" needs at least %2$s.",RequiredItemName,RequiredItemAmount==Notdefined?"1":String.valueOf(RequiredItemAmount)));
                FaildToExcuteCausedByPlayer = true;
            }
            else if(RequiredItemAmount != Notdefined){
                itemcnt = RequiredItemAmount;
                for(ItemStack stack:player.inventory.mainInventory){
                    if(stack != null && stack.getItem().getClass() == required.getClass()){
                        int nowreduce = Math.min(itemcnt,stack.getCount());
                        stack.setCount(stack.getCount() - nowreduce);
                        itemcnt -= nowreduce;
                        if(itemcnt == 0) break;
                    }
                }
            }
        }
        if(!messagestr.equals(NullMessage)){
            player.sendMessage(new TextComponentString(messagestr));
        }
        CommandHelper postcommand = Copy();
        if(FaildToExcuteCausedByPlayer){
            if(postcommand.punishstr.equals(NullCommand)) return false;
            else postcommand.comandstr = NullCommand;
        }
        else postcommand.punishstr = NullCommand;
        if(DelaySeconds != CommandHelper.Notdefined){
            postcommand.TimerBegin = timenow;
            postcommand.TimerEnd = timenow + (long)DelaySeconds*1000;
            if(player.hasCapability(CapabilityLoader.commandstorer,null)){
                commandstore commands = player.getCapability(CapabilityLoader.commandstorer,null);
                commands.addcommandforexcute(postcommand);
            }
        }
        else{
            postcommand.TryExcute(player);
        }
        if(ExcuteAmountLimit != CommandHelper.Notdefined){
            ExcuteAmountLimit--;
        }
        if(CooldownSeconds != CommandHelper.Notdefined){
            TimerBegin = timenow;
            TimerEnd = timenow + (long)CooldownSeconds*1000;
        }
        return !FaildToExcuteCausedByPlayer;
    }
    public void TryExcute(EntityPlayer player) {
        if(!comandstr.equals(NullCommand) ){
            Excute(player,comandstr);
        }
        if(!punishstr.equals(NullCommand) ){
            Excute(player,punishstr);
        }
    }
    public boolean MergeCommandFromAddingMode(CommandHelper AddingMode){
        if(!isCreatingMode || !AddingMode.isAddingMode) return false;
        if(!AddingMode.messagestr.equals(NullMessage)) messagestr = AddingMode.messagestr;
        if(!AddingMode.punishstr.equals(NullCommand)) punishstr = AddingMode.punishstr;
        if(AddingMode.isBypass) isBypass = true;
        if(AddingMode.isWalk) isWalk = true;
        if(AddingMode.isClick) isClick = true;
        if(AddingMode.isBreak) isBreak = true;
        if(AddingMode.ExcuteAmountLimit != Notdefined) ExcuteAmountLimit = AddingMode.ExcuteAmountLimit;
        if(AddingMode.CooldownSeconds != Notdefined) CooldownSeconds = AddingMode.CooldownSeconds;
        if(AddingMode.DelaySeconds != Notdefined) DelaySeconds = AddingMode.DelaySeconds;
        if(AddingMode.RequiredItemAmount != Notdefined) RequiredItemAmount = AddingMode.RequiredItemAmount;
        if(!AddingMode.RequiredItemName.equals(NoSpecialItem)) RequiredItemName = AddingMode.RequiredItemName;
        if(!AddingMode.RequiredPlayerName.equals(NoSpecialPlayer)) RequiredPlayerName = AddingMode.RequiredPlayerName;
        return true;
    }
    public CommandHelper Copy(){
        CommandHelper now = new CommandHelper();
        now.isViewMode = isViewMode;
        now.isDeleteMode = isDeleteMode;
        now.isAddingMode = isAddingMode;
        now.isDisabled = isDisabled;
        now.comandstr = comandstr;
        now.messagestr = messagestr;
        now.punishstr = punishstr;
        now.isBypass = isBypass;
        now.isClick = isClick;
        now.isWalk = isWalk;
        now.isBreak = isBreak;
        now.ExcuteAmountLimit = ExcuteAmountLimit;
        now.CooldownSeconds = CooldownSeconds;
        now.DelaySeconds = DelaySeconds;
        now.TimerBegin = TimerBegin;
        now.TimerEnd = TimerEnd;
        now.RequiredPlayerName = RequiredPlayerName;
        now.RequiredItemName = RequiredItemName;
        now.RequiredItemAmount = RequiredItemAmount;
        return now;
    }
    public String printString(){
        if(isDisabled) return CommandDisableMode;
        else if(isDeleteMode) return CommandDelMode;
        else if(isViewMode) return CommandViewMode;
        else{
            StringBuilder stringBuilder = new StringBuilder();
            if(!comandstr.equals(NullCommand)) {
                stringBuilder.append(CommandBeginNote);
                stringBuilder.append(":");
                stringBuilder.append(comandstr);
                stringBuilder.append(" ");
            }
            if(!messagestr.equals(NullMessage)){
                stringBuilder.append(MessageSendNote);
                stringBuilder.append(":");
                stringBuilder.append(messagestr);
                stringBuilder.append(" ");
            }
            if(!punishstr.equals(NullCommand)){
                stringBuilder.append(PunishCommandBeginNote);
                stringBuilder.append(":");
                stringBuilder.append(punishstr);
                stringBuilder.append(" ");
            }
            if(isWalk) {
                stringBuilder.append(CommandisWalkNote);
                stringBuilder.append(" ");
            }
            if(isClick){
                stringBuilder.append(CommandisClickNote);
                stringBuilder.append(" ");
            }
            if(isBypass){
                stringBuilder.append(CommandBypassNote);
                stringBuilder.append(" ");
            }
            if(isBreak){
                stringBuilder.append(CommandisBreakNote);
                stringBuilder.append(" ");
            }
            if(ExcuteAmountLimit != Notdefined){
                stringBuilder.append(CommandExcuteAmountLimitNote);
                stringBuilder.append(":");
                stringBuilder.append(ExcuteAmountLimit);
                stringBuilder.append(" ");
            }
            if(CooldownSeconds != Notdefined){
                stringBuilder.append(CommandCooldownSecondsNote);
                stringBuilder.append(":");
                stringBuilder.append(CooldownSeconds);
                stringBuilder.append(" ");
            }
            if(DelaySeconds != Notdefined){
                stringBuilder.append(CommandDelaySecondsNote);
                stringBuilder.append(":");
                stringBuilder.append(DelaySeconds);
                stringBuilder.append(" ");
            }
            if(!RequiredItemName.equals(NoSpecialItem)){
                stringBuilder.append(CommandRequriedItemNote);
                stringBuilder.append(":");
                stringBuilder.append(RequiredItemName);
                stringBuilder.append(":");
                stringBuilder.append(RequiredItemAmount==Notdefined?"NotReduce":RequiredItemAmount);
                stringBuilder.append(" ");
            }
            if(!RequiredPlayerName.equals(NoSpecialPlayer)){
                stringBuilder.append(CommandRequriedPlayerNote);
                stringBuilder.append(":");
                stringBuilder.append(RequiredPlayerName);
            }
            return stringBuilder.toString();
        }
    }
    @Override
    public String toString(){
        if(isDisabled) return CommandDisableMode;
        else if(isDeleteMode) return CommandDelMode;
        else if(isViewMode) return CommandViewMode;
        else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(comandstr);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(messagestr);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(punishstr);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(isBypass?1:0);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(isClick?1:0);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(isWalk?1:0);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(ExcuteAmountLimit);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(CooldownSeconds);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(DelaySeconds);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(TimerBegin);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(TimerEnd);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(RequiredPlayerName);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(RequiredItemName);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(RequiredItemAmount);
            stringBuilder.append(StringSpliter);
            stringBuilder.append(isBreak?1:0);
            return stringBuilder.toString();
        }
    }
}