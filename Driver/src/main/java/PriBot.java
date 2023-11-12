import commands.PriMain;
import main.BotDriver;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class PriBot extends BotDriver {
    @Override
    protected void setup() {
        VERSION = "1.0";
        activity = Activity.watching("Bots Being Developed");
        TOKEN = "PRI_BOT";
    }

    public PriBot() throws LoginException {
        super();
        shardManager.addEventListener(new PriMain(VERSION));
    }

    public static void main(String[] args) {
        try {
            PriBot bot = new PriBot();
        } catch (LoginException e) {
            System.out.println("ERROR: provided bot token invalid");
        }
    }
}
