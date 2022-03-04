package me.aleiv.gameengine.utilities;

import me.aleiv.gameengine.Core;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ChatUtils {

    public static void askInput(Player player, String questionMessage, Consumer<String> callback) {
        Prompt nameQuestion = new StringPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes('&', questionMessage);
            }

            @Override
            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    player.sendMessage(ChatColor.RED + "Input cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }

                callback.accept(input);
                return Prompt.END_OF_CONVERSATION;
            }
        };

        ConversationFactory factory = new ConversationFactory(Core.getInstance());
        factory.withFirstPrompt(nameQuestion);
        Conversation conversation = factory.buildConversation(player);
        conversation.begin();
    }

}
