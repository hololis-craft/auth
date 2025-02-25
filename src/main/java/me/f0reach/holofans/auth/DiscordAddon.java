package me.f0reach.holofans.auth;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class DiscordAddon extends ListenerAdapter {
    private static final String FORM_ID = "HoloFansAuth";
    private static final String FORM_CODE_ID = "pin-input";
    private static final String CONNECT_BUTTON_ID = "connect-discord";
    private final DiscordIntegration integration;

    public DiscordAddon(DiscordIntegration integration) {
        this.integration = integration;
        integration.getJDA().addEventListener(this);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals(CONNECT_BUTTON_ID)) {
            event.replyModal(createAuthForm()).queue(); // send a message in the channel
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals(FORM_ID)) {
            var code = event.getInteraction().getValue(FORM_CODE_ID).getAsString();
            event.reply("コードを受け取りました: " + code).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        var member = integration.getMemberById(event.getAuthor().getId());
        if (member == null) return;
        if (!integration.hasAdminRole(member.getRoles())) return;

        var message = event.getMessage();
        if (!message.getMentions().isMentioned(integration.getJDA().getSelfUser())) {
            return;
        }
        if (message.getContentRaw().contains("post form")) {
            event.getChannel().sendMessage("ロビーで指示されたらおすボタン")
                    .setActionRow(
                            Button.primary(CONNECT_BUTTON_ID, "接続する")
                    ).queue();
        }
    }

    private Modal createAuthForm() {
        TextInput textInput = TextInput.create(FORM_CODE_ID, "コードを入力してください", TextInputStyle.SHORT)
                .setPlaceholder("コード")
                .setRequiredRange(6, 6)
                .setRequired(true).build();
        return Modal.create(FORM_ID, "コード入力")
                .setTitle("Minecraftのコード")
                .addActionRow(textInput)
                .build();
    }
}
