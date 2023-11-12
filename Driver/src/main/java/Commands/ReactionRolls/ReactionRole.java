package Commands.ReactionRolls;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class ReactionRole {
    private String MessageId;
    private Emoji Emoji;
    private Role Role;

    public ReactionRole(String messageId, Emoji emoji, Role role) {
        MessageId = messageId;
        Emoji = emoji;
        Role = role;
    }

    public String getMessageId() {
        return MessageId;
    }

    public Emoji getEmoji() {
        return Emoji;
    }

    public Role getRole() {
        return Role;
    }
}
