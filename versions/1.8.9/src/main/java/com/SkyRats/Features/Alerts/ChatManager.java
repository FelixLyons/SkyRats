package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.GUI.FeatureSettings;
import com.SkyRats.Features.Rift.StealOrSplitTracker;
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
        if(FeatureSettings.isFeatureEnabled("Alerts", "Mining Ability Cooldown Notification")) {
            MiningAbilityAlert.check(msg, alertMessagePopup);
        }

        if(FeatureSettings.isFeatureEnabled("Alerts", "Reminder to Switch Ability Notification")) {
            SwitchAlert.check(msg, alertMessagePopup);
        }

        if(FeatureSettings.isFeatureEnabled("Rift", "Split or Steal Tracker")) {
            StealOrSplitTracker.check(msg);
            StealOrSplitTracker.checkAlrCD(msg);
        }
    }

}
