package Commands;

import Commands.ReactionRolls.RRAdd;
import Commands.ReactionRolls.RRDelete;
import Commands.ReactionRolls.ReactionRole;
import Commands.ReactionRolls.ReactionRoleChecks;
import Main.MainCommands;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PriMain extends MainCommands {
    public HashMap<Guild, List<ReactionRole>> GuildsReactionRoles;
    public PriMain(String version) {
        super(version);
        GuildsReactionRoles = new HashMap<>();
        prefixCommands.add(new RRAdd(GuildsReactionRoles));
        prefixCommands.add(new RRDelete(GuildsReactionRoles));
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        super.onGuildReady(event);
        List<ReactionRole> reactionRoles = new ArrayList<>();
        ReactionRoleChecks.updateReactionRoles(event.getGuild(), reactionRoles);
        GuildsReactionRoles.put(event.getGuild(), reactionRoles);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        List<ReactionRole> reactionRoles = GuildsReactionRoles.get(event.getGuild());
        ReactionRoleChecks.addRoleCheck(event, reactionRoles);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        List<ReactionRole> reactionRoles = GuildsReactionRoles.get(event.getGuild());
        ReactionRoleChecks.removeRollCheck(event, reactionRoles);
    }
}
