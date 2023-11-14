package commands.tempVoiceChannels;

import information.FileAccessor;
import information.ServerStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TempVCChecks {
    private static Member TempVoiceChannelCreator;
    public static void joinedCheck(GuildVoiceUpdateEvent event, List<TempVoiceChannel> tempVoiceChannels) {
        String channelId = event.getChannelJoined().getId();
        VoiceChannel CopyVC = null;
        String defaultTVCName = null;
        for (TempVoiceChannel tempVoiceChannel : tempVoiceChannels) {
            if (channelId.equalsIgnoreCase(tempVoiceChannel.getHostVC().getId())) {
                CopyVC = tempVoiceChannel.getCopyVC();
                defaultTVCName = tempVoiceChannel.getDefaultTVCName();
                CopyVC.createCopy().setName(defaultTVCName).queue();
                TempVoiceChannelCreator = event.getMember();
            }
        }

    }
    public static void leftCheck(GuildVoiceUpdateEvent event, List<TempVoiceChannel> tempVoiceChannels) {
        String channelId = event.getChannelLeft().getId();
        for (TempVoiceChannel tempVoiceChannel : tempVoiceChannels) {
            for (String vcId : tempVoiceChannel.getTempVCsIds()) {
                if (vcId.equalsIgnoreCase(channelId) && event.getChannelLeft().getMembers().size() == 0) {
                    event.getChannelLeft().delete().queue();
                }
            }
            tempVoiceChannel.getTempVCsIds().remove(channelId);
        }
    }
    public static void channelCreatedCheck(ChannelCreateEvent event, List<TempVoiceChannel> tempVoiceChannels) {
        if (event.getChannel().getType().equals(ChannelType.VOICE)) {
            String channelName = event.getChannel().getName();
            for (TempVoiceChannel tempVoiceChannel : tempVoiceChannels) {
                if (channelName.equalsIgnoreCase(tempVoiceChannel.getDefaultTVCName())) {
                    tempVoiceChannel.getTempVCsIds().add(event.getChannel().getId());
                    event.getGuild().moveVoiceMember(TempVoiceChannelCreator, event.getChannel().asAudioChannel()).queue();
                    event.getChannel().asVoiceChannel().sendMessage(TempVoiceChannelCreator.getAsMention() + " this is a temp VC. \nIf you wish to rename it you can type: \n!rename {New Name}").queue();
                }
            }
        }
    }
    public static void updateTempVoiceChannelFile(Guild guild, List<TempVoiceChannel> tempVoiceChannels) {
        try {
            String filepath = ServerStorage.getInfoFilePath(guild, "tempVCs.txt");
            List<String> tempVCStrings = new ArrayList<>();
            for (TempVoiceChannel tempVoiceChannel : tempVoiceChannels) {
                String tempVCString = tempVoiceChannel.getHostVC().getId() + ","
                        + tempVoiceChannel.getCopyVC().getId() + ","
                        + tempVoiceChannel.getDefaultTVCName();
                tempVCStrings.add(tempVCString);
            }
            FileAccessor.rewriteFile(filepath, tempVCStrings);
        } catch (IOException e) {}
    }
    public static void updateTempVoiceChannels(Guild guild, List<TempVoiceChannel> tempVoiceChannels) {
        try {
            String filepath = ServerStorage.getInfoFilePath(guild, "tempVCs.txt");
            List<String> tempVCStrings = FileAccessor.getFileList(filepath);
            for (String tempVCString : tempVCStrings) {
                String[] parts = tempVCString.split(",");
                VoiceChannel hostVC = guild.getVoiceChannelById(parts[0]);
                VoiceChannel copyVC = guild.getVoiceChannelById(parts[1]);
                String defaultTVCName = parts[2];
                TempVoiceChannel tempVoiceChannel = new TempVoiceChannel(hostVC, copyVC, defaultTVCName);
                tempVoiceChannels.add(tempVoiceChannel);
            }
        } catch (FileNotFoundException e) {}
    }
}
