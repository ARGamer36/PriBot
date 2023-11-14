package commands.reactionRolls;

import commands.abstracts.PrefixCommand;
import main.ClearanceChecks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.HashMap;
import java.util.List;

public class RRAdd extends PrefixCommand {
    private HashMap<Guild, List<ReactionRole>> GuildsReactionRoles;
    public RRAdd(HashMap<Guild, List<ReactionRole>> guildsReactionRoles) {
        name = "rrAdd";
        description = "Adds a reaction role";
        GuildsReactionRoles = guildsReactionRoles;
    }

    @Override
    public void action(MessageReceivedEvent event) {
        if (ClearanceChecks.isAdmin(event.getMember())) {
            try {
                String[] commandInfo = event.getMessage().getContentRaw().substring(1).split(" ");
                String messageId = commandInfo[1];
                Emoji emoji = Emoji.fromFormatted(commandInfo[2]);
                Role role = event.getGuild().getRoleById(commandInfo[3].replace("<@&", "").replace(">", ""));
                ReactionRole reactionRole = new ReactionRole(messageId, emoji, role);
                List<ReactionRole> reactionRoles = GuildsReactionRoles.get(event.getGuild());
                reactionRoles.add(reactionRole);
                List<TextChannel> textChannels = event.getGuild().getTextChannels();
                for (TextChannel textChannel : textChannels) {
                    try {
                        textChannel.addReactionById(messageId, emoji).queue(null, (exception) -> {
                        });
                    } catch (InsufficientPermissionException insufficientPermissionException) {}
                }
                ReactionRoleChecks.updateReactionRolesFile(event.getGuild(), reactionRoles);
                event.getMessage().reply("A Reaction Role was created for " + role.getAsMention() + " with " + emoji.getFormatted() + " on message " + messageId).queue();
            } catch (IndexOutOfBoundsException e) {
                event.getMessage().reply("Command: !rrAdd {messageId} {emoji} @role").queue();
            }
        }
    }
}
