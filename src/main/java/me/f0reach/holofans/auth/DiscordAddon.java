package me.f0reach.holofans.auth;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.storage.linking.LinkManager;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.UUID;

public class DiscordAddon extends ListenerAdapter {
    private static final String FORM_ID = "HoloFansAuth";
    private static final String FORM_CODE_ID = "pin-input";
    private static final String CONNECT_BUTTON_ID = "connect-discord";
    private final DiscordIntegration integration;

    public DiscordAddon(DiscordIntegration integration) {
        this.integration = integration;
        integration.getJDA().addEventListener(this);
    }

    public boolean hasLinked(UUID uuid) {
        return LinkManager.isPlayerLinked(uuid);
    }

    public String getVerifyCode(UUID uuid) {
        // @todo Implement for bedrock code
        return Integer.toString(LinkManager.genLinkNumber(uuid));
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
            try {
                var code = event.getInteraction().getValue(FORM_CODE_ID).getAsString();
                var intCode = Integer.parseInt(code);
                // Check if the user is already linked both in Java and Bedrock
                if (LinkManager.isDiscordUserLinkedToJava(event.getUser().getId()) &&
                        LinkManager.isDiscordUserLinkedToBedrock(event.getUser().getId())) {
                    event.reply("あなたはすでにアカウント情報が登録されています").queue();
                    return;
                }
                // Get link info from LinkManager.pendingLinks or LinkManager.pendingBedrockLinks
                var linkSuccessful = false;
                if (!LinkManager.isDiscordUserLinkedToJava(event.getUser().getId())) {
                    var link = LinkManager.pendingLinks.get(intCode);
                    if (link != null) {
                        linkSuccessful = LinkManager.linkPlayer(event.getUser().getId(), link.getValue());
                    }
                }
                if (!linkSuccessful && !LinkManager.isDiscordUserLinkedToBedrock(event.getUser().getId())) {
                    var link = LinkManager.pendingBedrockLinks.get(intCode);
                    if (link != null) {
                        linkSuccessful = LinkManager.linkBedrockPlayer(event.getUser().getId(), link.getValue());
                    }
                }
                if (linkSuccessful) {
                    LinkManager.save();
                    event.deferReply(true).addContent("接続が成功しました").queue();
                } else {
                    event.deferReply(true).addContent("接続が失敗しました。すでに他のアカウントで入っていませんか？").queue();
                }
            } catch (NumberFormatException e) {
                event.deferReply(true).addContent("コードが無効です").queue();
            }
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
        TextInput textInput = TextInput
                .create(FORM_CODE_ID, "コードを入力してください", TextInputStyle.SHORT)
                .setPlaceholder("コード")
                .setRequiredRange(5, 5)
                .setRequired(true).build();
        return Modal.create(FORM_ID, "コード入力")
                .setTitle("Minecraftのコード")
                .addActionRow(textInput)
                .build();
    }
}
