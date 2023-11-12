package Commands;

import Commands.ReactionRolls.RRAdd;
import Commands.ReactionRolls.RRDelete;
import Commands.ReactionRolls.ReactionRole;
import Commands.ReactionRolls.ReactionRoleChecks;
import Main.MainCommands;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PriMain extends MainCommands {
    public List<ReactionRole> ReactionRoles;
    public PriMain(String version) {
        super(version);
        ReactionRoles = new ArrayList<>();
        prefixCommands.add(new RRAdd(ReactionRoles));
        prefixCommands.add(new RRDelete(ReactionRoles));
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        super.onGuildReady(event);
        ReactionRoleChecks.updateReactionRoles(event.getGuild(), ReactionRoles);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        ReactionRoleChecks.addRoleCheck(event, ReactionRoles);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        ReactionRoleChecks.removeRollCheck(event, ReactionRoles);
    }
}
