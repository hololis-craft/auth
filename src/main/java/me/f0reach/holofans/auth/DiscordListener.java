package me.f0reach.holofans.auth;

import com.discordsrv.api.eventbus.EventListener;
import org.jetbrains.annotations.NotNull;

public class DiscordListener {
    private final AuthPlugin plugin;

    public DiscordListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

//    @Override
//    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
//        if (!event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
//            // Not a mention for me
//            return;
//        }
//
//        String message = event.getMessage().getContentRaw();
//        if (message.contains("post form")) {
//            // Handle auth command
//            sendAuthForm(event.getChannel());
//        }
//    }
//
//    private void sendAuthForm(MessageChannel channel) {
//        // Send auth form
//        channel.sendMessage("ロビーで指示されたらおすボタン")
//                .setActionRow(
//                        Button.primary("connect-discord", "接続する")
//                ).queue();
//    }
//
//    @Override
//    public void onButtonClick(@NotNull ButtonClickEvent event) {
//        if (event.getComponentId().equals("connect-discord")) {
//            var pinInput =
//        }
//    }
//
//    private
}
