package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.AlertMessagePopup;
import net.minecraft.client.Minecraft;

//Pops an alert on user screen when mining ability is off cooldown
public class MiningAbilityAlert {
    public static void check(String msg, AlertMessagePopup alert) {
        if(msg.equalsIgnoreCase("Pickobulus is now available!") || msg.equalsIgnoreCase("Mining Speed Boost is now available!")) {
            alert.display("ABILITY", 2000);
            Minecraft.getMinecraft().thePlayer.playSound("mob.enderdragon.growl", 1.0F, 1.0F);
        }
    }
}
