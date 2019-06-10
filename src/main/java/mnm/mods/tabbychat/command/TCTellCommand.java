package mnm.mods.tabbychat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ComponentArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class TCTellCommand {

    private static final String TARGETS = "targets";
    private static final String CHANNEL = "channel";
    private static final String MESSAGE = "message";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(literal("tctell")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument(TARGETS, EntityArgument.players())
                        .then(argument(CHANNEL, StringArgumentType.string())
                                .then(argument(MESSAGE, ComponentArgument.component())
                                        .executes(TCTellCommand::execute)))));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {

        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(context, TARGETS);
        String channel = "#" + StringArgumentType.getString(context, CHANNEL);
        ITextComponent message = ComponentArgument.getComponent(context, MESSAGE);

        for (ServerPlayerEntity player : players) {
            TabbyChat.sendTo(player, channel, message);
        }

        return players.size();
    }

}
