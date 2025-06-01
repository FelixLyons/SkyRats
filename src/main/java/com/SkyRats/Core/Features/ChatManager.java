package com.SkyRats.Core.Features;

import com.SkyRats.Features.Alerts.MiningAbilityAlert;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatManager {
    private final AlertMessagePopup alertMessagePopup;

    public ChatManager(AlertMessagePopup alertMessagePopup) {
        this.alertMessagePopup = alertMessagePopup;
    }
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();

        // Check for different alert types
        MiningAbilityAlert.check(msg, alertMessagePopup);
    }

}
