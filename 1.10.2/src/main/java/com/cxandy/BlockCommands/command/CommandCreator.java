package com.cxandy.BlockCommands.command;

import com.cxandy.BlockCommands.capability.CapabilityLoader;
import com.cxandy.BlockCommands.capability.commandstore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by andychen on 17-2-5.
 */
public class CommandCreator extends CommandBase{
    @Override
    public String getCommandName() {
        return "commandcreate";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/commandcreate @Command:CommandText @Message:MessageTest @Delay:DelayTime @Cooldown:Cooldowntime @Bypass(Excutes as OP) @isWalk @isClick @Player(Only Specific player can excute) @Item:Itemname:amount(Item Costs) @Amount:amount(Excute Amount Limit) @Punish:CommandText(will be excute when do not satisfy \"@Item\" restrictions ) @isBreak. All the \"@\" is optional, but for \"@command\" and \"@message\", you need to have at least one of them, same for \"@isWalk\" and \"@isClick\".";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < args.length; ++i){
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }
        CommandHelper command = CommandHelper.ConvertStringFromSender(stringBuilder.toString(),CommandHelper.ConvertToCommand);
        if(command == null){
            throw new WrongUsageException("/commandcreate @Command:CommandText @Message:MessageTest @Delay:DelayTime @Cooldown:Cooldowntime @Bypass(Excutes as OP) @isWalk @isClick @Player(Only Specific player can excute) @Item:Itemname:amount(Item Costs) @Amount:amount(Excute Amount Limit) @Punish:CommandText(will be excute when do not satisfy \"@Item\" restrictions ) @isBreak. All the \"@\" is optional, but for \"@command\" and \"@message\", you need to have at least one of them, same for \"@isWalk\" and \"@isClick\".");
        }
        EntityPlayerMP entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
        if(entityPlayerMP == sender && entityPlayerMP.hasCapability(CapabilityLoader.commandstorer,null)){
            commandstore nowcommand = entityPlayerMP.getCapability(CapabilityLoader.commandstorer,null);
            nowcommand.setcommand(command);
            sender.addChatMessage(new TextComponentTranslation("New CommandCreator set:\"%1$s\".",command.printString()));
        }
    }
}
