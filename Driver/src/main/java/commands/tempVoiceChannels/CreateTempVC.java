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

public class CreateTempVC extends SlashCommand {
    private HashMap<Guild, List<TempVoiceChannel>> GuildsTempVoiceChannels;
    public CreateTempVC(HashMap<Guild, List<TempVoiceChannel>> guildTempVoiceChannels) {
        name = "create_temp_vc";
        description = "Creates a Temporary Voice Channel Hub";
        options = new ArrayList<>();
        OptionData hostVC = new OptionData(OptionType.CHANNEL, "host_vc", "Host Voice Channel For TempVCs", true);
        OptionData copyVC = new OptionData(OptionType.CHANNEL, "copy_vc", "Voice Channel That Will Have Its Perms copied for TempVCs", true);
        OptionData defaultName = new OptionData(OptionType.STRING, "default_name", "Default names for TempVCs",true);
        options.add(hostVC);
        options.add(copyVC);
        options.add(defaultName);
        GuildsTempVoiceChannels = guildTempVoiceChannels;
    }

    @Override
    public void action(SlashCommandInteractionEvent event) {
        if (ClearanceChecks.isAdmin(event.getMember())) {
            List<TempVoiceChannel> tempVoiceChannels = GuildsTempVoiceChannels.get(event.getGuild());
            try {
                VoiceChannel hostVC = event.getOption("host_vc").getAsChannel().asVoiceChannel();
                VoiceChannel copyVC = event.getOption("copy_vc").getAsChannel().asVoiceChannel();
                String defaultTVCName = event.getOption("default_name").getAsString();
                TempVoiceChannel tempVoiceChannel = new TempVoiceChannel(hostVC, copyVC, defaultTVCName);
                tempVoiceChannels.add(tempVoiceChannel);
                TempVCChecks.updateTempVoiceChannelFile(event.getGuild(), tempVoiceChannels);
                event.reply("Temp Voice Channel Created").setEphemeral(true).queue();
            } catch (IllegalStateException e) {
                event.reply("Must Choose Voice Channels").setEphemeral(true).queue();
            }
        }
    }
}
