package me.f0reach.holofans.auth;


import com.discordsrv.api.DiscordSRVApi;
import com.discordsrv.api.discord.entity.channel.DiscordMessageChannel;
import com.discordsrv.api.discord.entity.interaction.component.ComponentIdentifier;
import com.discordsrv.api.eventbus.Subscribe;
import com.discordsrv.api.events.discord.interaction.command.DiscordMessageContextInteractionEvent;
import com.discordsrv.api.events.discord.interaction.component.DiscordButtonInteractionEvent;
import com.discordsrv.api.events.message.receive.discord.DiscordChatMessageReceiveEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.interactions.components.buttons.Button;
import com.discordsrv.dependencies.net.dv8tion.jda.api.interactions.components.text.TextInput;
import com.discordsrv.dependencies.net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import com.discordsrv.dependencies.net.dv8tion.jda.api.interactions.modals.Modal;

public class DiscordSRVListener {
    private final AuthPlugin plugin;
    private final ComponentIdentifier connectButton = ComponentIdentifier.of("HoloFansAuth", "connect-discord");
    private final ComponentIdentifier formId = ComponentIdentifier.of("HoloFansAuth", "pin-form");
    private final ComponentIdentifier formCodeId = ComponentIdentifier.of("HoloFansAuth", "pin-input");

    public DiscordSRVListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onGuildMessageReceived(DiscordChatMessageReceiveEvent event) {
        var selfUser = DiscordSRVApi.get().jda().getSelfUser();
        if (!event.getMessage().getMentionedUsers().stream().anyMatch(user -> user.getId() == selfUser.getIdLong())) {
            // Not a mention for me
            return;
        }

        String message = event.getMessage().getContent();
        if (message != null && message.contains("post form")) {
            // Handle auth command
            sendAuthForm(event.getChannel());
        }
    }

    private void sendAuthForm(DiscordMessageChannel channel) {
        // Send auth form

        channel.getAsJDAMessageChannel().sendMessage("ロビーで指示されたらおすボタン")
                .setActionRow(
                        Button.primary(connectButton.getDiscordIdentifier(), "接続する")
                ).queue();
    }

    private Modal createAuthForm() {
        TextInput textInput = TextInput.create(formCodeId.getDiscordIdentifier(), "コードを入力してください", TextInputStyle.SHORT)
                .setPlaceholder("コード")
                .setRequiredRange(6, 6)
                .setRequired(true).build();
        return Modal.create(formId.getDiscordIdentifier(), "コード入力")
                .setTitle("Minecraftのコード")
                .addActionRow(textInput)
                .build();
    }

    @Subscribe
    public void onButtonClick(DiscordButtonInteractionEvent event) {
        // Due to bug, it is not working
        plugin.getLogger().info("Button clicked: " + event.asJDA().getComponentId());
        if (event.isFor(connectButton)) {
            // Handle button click
            event.asJDA().replyModal(createAuthForm());
        }
    }

    @Subscribe
    public void onMessageInteraction(GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            var buttonEvent = (ButtonInteractionEvent) event;
            var componentId = buttonEvent.getComponentId();
            if (componentId.equals(connectButton.getDiscordIdentifier())) {
                // Handle form submission
                buttonEvent.replyModal(createAuthForm()).queue();
            }
        }
        if (event instanceof ModalInteractionEvent) {
            var modalEvent = (ModalInteractionEvent) event;
            var modalId = modalEvent.getModalId();
            if (modalId.equals(formId.getDiscordIdentifier())) {
                // Handle form submission
            }
        }
    }
}
