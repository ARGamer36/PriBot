package commands.tempVoiceChannels;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;

public class TempVoiceChannel {
    private VoiceChannel HostVC;
    private VoiceChannel CopyVC;
    private String DefaultTVCName;
    private List<String> TempVCsIds;

    public TempVoiceChannel(VoiceChannel hostVC, VoiceChannel copyVC, String defaultTVCName) {
        HostVC = hostVC;
        CopyVC = copyVC;
        DefaultTVCName = defaultTVCName;
        TempVCsIds = new ArrayList<>();
    }

    public VoiceChannel getHostVC() {
        return HostVC;
    }

    public VoiceChannel getCopyVC() {
        return CopyVC;
    }

    public String getDefaultTVCName() {
        return DefaultTVCName;
    }

    public List<String> getTempVCsIds() {
        return TempVCsIds;
    }

    @Override
    public String toString() {
        String output = "Information: \n" +
                "Host VC: " + HostVC.getAsMention() + "\n" +
                "Copied VC: " + CopyVC.getAsMention() + "\n" +
                "Default name: " + DefaultTVCName;
        return output;
    }
}
