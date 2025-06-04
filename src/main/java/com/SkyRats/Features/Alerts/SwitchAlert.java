package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.ChatUtils;
import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

// Alert user to switch back to Pickobulus after leaving mineshaft
public class SwitchAlert {
    private static boolean abilitySwitched = false;
    private static boolean enteredShaft = false;

    public static void check(String msg, AlertMessagePopup alert) {
        String playerLocation = PlayerLocationChecker.getLocation();

        // Check for ability changes
        // There prob a better way to code this ngl.
        if(msg.equalsIgnoreCase("You selected Mining Speed Boost as your Pickaxe Ability. This ability will apply to all of your pickaxes!")
        || msg.equalsIgnoreCase("You selected Anomalous Desire as your Pickaxe Ability. This ability will apply to all of your pickaxes!")
        || msg.equalsIgnoreCase("You selected Maniac Miner as your Pickaxe Ability. This ability will apply to all of your pickaxes!")
        || msg.equalsIgnoreCase("You selected Gemstone Infusion as your Pickaxe Ability. This ability will apply to all of your pickaxes!")
        || msg.equalsIgnoreCase("You selected Sheer Force as your Pickaxe Ability. This ability will apply to all of your pickaxes!")) {

            abilitySwitched = true;
        }else if(msg.equalsIgnoreCase("You selected Pickobulus as your Pickaxe Ability. This ability will apply to all of your pickaxes!")) {
            abilitySwitched = false;
        }

        // Check if player entered a shaft
        if(playerLocation.equalsIgnoreCase("Glacite Mineshafts")) {
            enteredShaft = true;
        }

        // Check player location.
        // Only alerts if they are in Dwarven Base Camp or the Glacite Tunnels after exiting a mineshaft
        if((playerLocation.equalsIgnoreCase("Glacite Tunnels") || playerLocation.equalsIgnoreCase("Dwarven Base Camp"))
                && enteredShaft && abilitySwitched) {

            alert.display("SWITCH", 3000);
            ChatUtils.sendClickableMessage("Reminder to switch pickaxe ability!", EnumChatFormatting.AQUA,
                    " OPEN HOTM", EnumChatFormatting.DARK_GREEN, "/hotm", "Runs /hotm");
            Minecraft.getMinecraft().thePlayer.playSound("random.anvil_land", 2.0F, 1.0F);
            enteredShaft = false;
        }
    }
}
