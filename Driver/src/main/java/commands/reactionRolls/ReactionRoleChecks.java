package commands.reactionRolls;

import information.FileAccessor;
import information.ServerStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReactionRoleChecks {
    public static void addRoleCheck(MessageReactionAddEvent event, List<ReactionRole> reactionRoles) {
        String messageId = event.getMessageId();
        Emoji emoji = event.getReaction().getEmoji();
        for (ReactionRole reactionRole : reactionRoles) {
            if (messageId.equalsIgnoreCase(reactionRole.getMessageId())
                    && emoji.equals(reactionRole.getEmoji())) {
                event.getGuild().addRoleToMember(event.getUser(), reactionRole.getRole()).queue();
            }
        }
    }

    public static void removeRollCheck(MessageReactionRemoveEvent event, List<ReactionRole> reactionRoles) {
        String messageId = event.getMessageId();
        Emoji emoji = event.getReaction().getEmoji();
        for (ReactionRole reactionRole : reactionRoles) {
            if (messageId.equalsIgnoreCase(reactionRole.getMessageId())
                    && emoji.equals(reactionRole.getEmoji())) {
                event.getGuild().removeRoleFromMember(event.getUser(), reactionRole.getRole()).queue();
            }
        }
    }
    public static void updateReactionRolesFile(Guild guild, List<ReactionRole> reactionRoles) {
        try {
            String filepath = ServerStorage.getInfoFilePath(guild, "reactionRoles.txt");
            List<String> reactionRoleStrings = new ArrayList<>();
            for (ReactionRole reactionRole : reactionRoles) {
                String reactionRoleString = reactionRole.getMessageId() + ","
                        + reactionRole.getEmoji().getFormatted() + ","
                        + reactionRole.getRole().getId();
                reactionRoleStrings.add(reactionRoleString);
            }
            FileAccessor.rewriteFile(filepath, reactionRoleStrings);
        } catch (IOException e) {}
    }
    public static void updateReactionRoles(Guild guild, List<ReactionRole> reactionRoles) {
        try {
            String filePath = ServerStorage.getInfoFilePath(guild, "reactionRoles.txt");
            List<String> reactionRoleStrings = FileAccessor.getFileList(filePath);
            for (String reactionRoleString : reactionRoleStrings) {
                String[] parts = reactionRoleString.split(",");
                String messageId = parts[0];
                Emoji emoji = Emoji.fromFormatted(parts[1]);
                Role role = guild.getRoleById(parts[2]);
                ReactionRole reactionRole = new ReactionRole(messageId, emoji, role);
                reactionRoles.add(reactionRole);
            }
        } catch (FileNotFoundException e) {}
    }
}
