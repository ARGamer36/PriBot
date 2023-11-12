package Commands.ReactionRolls;

import Commands.Abstracts.PrefixCommand;
import Main.ClearanceChecks;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class RRDelete extends PrefixCommand {
    List<ReactionRole> ReactionRoles;

    public RRDelete(List<ReactionRole> reactionRoles) {
        name = "rrDel";
        description = "Deletes a reaction role";
        ReactionRoles = reactionRoles;
    }

    @Override
    public void action(MessageReceivedEvent event) {
        if (ClearanceChecks.isAdmin(event.getMember())) {
            try {
                String[] commandInfo = event.getMessage().getContentRaw().substring(1).split(" ");
                String messageId = commandInfo[1];
                Role role = event.getGuild().getRoleById(commandInfo[2].replace("<@&", "").replace(">", ""));
                ReactionRole delete = null;
                for (ReactionRole reactionRole : ReactionRoles) {
                    if (reactionRole.getMessageId().equalsIgnoreCase(messageId)
                            && reactionRole.getRole().equals(role)) {
                        delete = reactionRole;
                    }
                }
                ReactionRoles.remove(delete);
                ReactionRoleChecks.updateReactionRolesFile(event.getGuild(), ReactionRoles);
                event.getMessage().reply("A Reaction Role deleted").queue();
            } catch (IndexOutOfBoundsException e) {
                event.getMessage().reply("Command: !rrDel {messageId} @role").queue();
            }
        }
    }
}
