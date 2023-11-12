package commands.reactionRolls;

import commands.abstracts.PrefixCommand;
import main.ClearanceChecks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class RRDelete extends PrefixCommand {
    HashMap<Guild, List<ReactionRole>> GuildsReactionRoles;

    public RRDelete(HashMap<Guild, List<ReactionRole>> guildsReactionRoles) {
        name = "rrDel";
        description = "Deletes a reaction role";
        GuildsReactionRoles = guildsReactionRoles;
    }

    @Override
    public void action(MessageReceivedEvent event) {
        if (ClearanceChecks.isAdmin(event.getMember())) {
            try {
                String[] commandInfo = event.getMessage().getContentRaw().substring(1).split(" ");
                String messageId = commandInfo[1];
                Role role = event.getGuild().getRoleById(commandInfo[2].replace("<@&", "").replace(">", ""));
                ReactionRole delete = null;
                List<ReactionRole> reactionRoles = GuildsReactionRoles.get(event.getGuild());
                for (ReactionRole reactionRole : reactionRoles) {
                    if (reactionRole.getMessageId().equalsIgnoreCase(messageId)
                            && reactionRole.getRole().equals(role)) {
                        delete = reactionRole;
                    }
                }
                reactionRoles.remove(delete);
                ReactionRoleChecks.updateReactionRolesFile(event.getGuild(), reactionRoles);
                event.getMessage().reply("A Reaction Role deleted").queue();
            } catch (IndexOutOfBoundsException e) {
                event.getMessage().reply("Command: !rrDel {messageId} @role").queue();
            }
        }
    }
}
