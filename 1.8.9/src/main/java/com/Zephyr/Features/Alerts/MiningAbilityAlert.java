package com.Zephyr.Features.Alerts;

import com.Zephyr.Core.Features.Notifications.AlertMessagePopup;
import net.minecraft.client.Minecraft;

//Pops an alert on user screen when mining ability is off cooldown
public class MiningAbilityAlert {
    public static void check(String msg, AlertMessagePopup alert) {
        if(msg.equalsIgnoreCase("Pickobulus is now available!") || msg.equalsIgnoreCase("Mining Speed Boost is now available!")
            || msg.equalsIgnoreCase("Anomalous Desire Pickaxe Ability!") || msg.equalsIgnoreCase("Maniac Miner is now available!")
            || msg.equalsIgnoreCase("Gemstone Infusion is now available!") || msg.equalsIgnoreCase("Sheer Force is now available!")) {

            alert.display("ABILITY", 2000);
            Minecraft.getMinecraft().thePlayer.playSound("fireworks.launch", 1.0F, 1.0F);
        }
    }
}
