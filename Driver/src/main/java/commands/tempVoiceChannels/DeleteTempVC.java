package commands.tempVoiceChannels;

import commands.abstracts.SlashCommand;
import main.ClearanceChecks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteTempVC extends SlashCommand {
    private HashMap<Guild, List<TempVoiceChannel>> GuildsTempVoiceChannels;

    public DeleteTempVC(HashMap<Guild, List<TempVoiceChannel>> guildsTempVoiceChannels) {
        name = "delete_temp_vc";
        description = "Deletes a Temporary Voice Channel Hub";
        options = new ArrayList<>();
        OptionData hostVC = new OptionData(OptionType.CHANNEL, "host_vc", "Host Voice Channel For TempVCs",true);
        options.add(hostVC);
        GuildsTempVoiceChannels = guildsTempVoiceChannels;
    }

    @Override
    public void action(SlashCommandInteractionEvent event) {
        if (ClearanceChecks.isAdmin(event.getMember())) {
            List<TempVoiceChannel> tempVoiceChannels = GuildsTempVoiceChannels.get(event.getGuild());
            try {
                VoiceChannel hostVC = event.getOption("host_vc").getAsChannel().asVoiceChannel();
                TempVoiceChannel deleteTempVoiceChannel = null;
                for (TempVoiceChannel tempVoiceChannel : tempVoiceChannels) {
                    if (tempVoiceChannel.getHostVC().equals(hostVC)) {
                        deleteTempVoiceChannel = tempVoiceChannel;
                    }
                }
                tempVoiceChannels.remove(deleteTempVoiceChannel);
                TempVCChecks.updateTempVoiceChannelFile(event.getGuild(), tempVoiceChannels);
                event.reply("Temp Voice Channel Deleted").setEphemeral(true).queue();
            } catch (IllegalStateException e) {
                event.reply("Must Choose Voice Channel").setEphemeral(true).queue();
            }
        }
    }
}
