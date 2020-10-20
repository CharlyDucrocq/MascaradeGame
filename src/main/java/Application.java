import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Application extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().contains("on peut sortir ?")) {
            event.getChannel().sendMessage("Non. #RestezChezVous").queue();
        }
    }

    public static void main(String[] argv) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(argv[1]);
        builder.addEventListeners(new Application());
        builder.build();
    }


}
