package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.AlertMessagePopup;
import com.SkyRats.Core.Features.SettingsManager;
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
        if(SettingsManager.isFeatureEnabled("Alerts", "Mining Ability Cooldown Notification")) {
            MiningAbilityAlert.check(msg, alertMessagePopup);
        }
    }

}
