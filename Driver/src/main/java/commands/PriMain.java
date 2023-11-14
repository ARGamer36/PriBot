package commands;

import commands.reactionRolls.RRAdd;
import commands.reactionRolls.RRDelete;
import commands.reactionRolls.ReactionRole;
import commands.reactionRolls.ReactionRoleChecks;
import commands.tempVoiceChannels.CreateTempVC;
import commands.tempVoiceChannels.DeleteTempVC;
import commands.tempVoiceChannels.TempVCChecks;
import commands.tempVoiceChannels.TempVoiceChannel;
import main.MainCommands;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PriMain extends MainCommands {
    public HashMap<Guild, List<ReactionRole>> GuildsReactionRoles;
    public HashMap<Guild, List<TempVoiceChannel>> GuildsTempVoiceChannels;
    public PriMain(String version) {
        super(version);
        GuildsReactionRoles = new HashMap<>();
        prefixCommands.add(new RRAdd(GuildsReactionRoles));
        prefixCommands.add(new RRDelete(GuildsReactionRoles));
        GuildsTempVoiceChannels = new HashMap<>();
        slashCommands.add(new CreateTempVC(GuildsTempVoiceChannels));
        slashCommands.add(new DeleteTempVC(GuildsTempVoiceChannels));
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        super.onGuildReady(event);
        List<ReactionRole> reactionRoles = new ArrayList<>();
        ReactionRoleChecks.updateReactionRoles(event.getGuild(), reactionRoles);
        GuildsReactionRoles.put(event.getGuild(), reactionRoles);
        List<TempVoiceChannel> tempVoiceChannels = new ArrayList<>();
        TempVCChecks.updateTempVoiceChannels(event.getGuild(), tempVoiceChannels);
        GuildsTempVoiceChannels.put(event.getGuild(), tempVoiceChannels);
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

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        List<TempVoiceChannel> tempVoiceChannels = GuildsTempVoiceChannels.get(event.getGuild());
        if (event.getChannelJoined() != null) {
            TempVCChecks.joinedCheck(event, tempVoiceChannels);
        }
        if (event.getChannelLeft() != null) {
            TempVCChecks.leftCheck(event, tempVoiceChannels);
        }
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        List<TempVoiceChannel> tempVoiceChannels = GuildsTempVoiceChannels.get(event.getGuild());
        TempVCChecks.channelCreatedCheck(event, tempVoiceChannels);
    }
}
