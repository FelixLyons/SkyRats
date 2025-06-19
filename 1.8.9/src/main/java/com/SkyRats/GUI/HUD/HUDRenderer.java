package com.SkyRats.GUI.HUD;

import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.GUI.FeatureSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Renders HUD if active
public class HUDRenderer {
    private final ShaftDisplay shaftDisplay;
    private final SplitOrStealDisplay sosDisplay;

    public HUDRenderer(ShaftDisplay shaftDisplay, SplitOrStealDisplay sosDisplay) {
        this.shaftDisplay = shaftDisplay;
        this.sosDisplay = sosDisplay;
    }

    // Renders the HUD
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

        // Only render if feature is turned on
        if(FeatureSettings.isFeatureEnabled("HUD","Mineshaft Tracker HUD")) {
            // Only render if player is in certain location
            if(PlayerLocationChecker.getLocation().equalsIgnoreCase("Glacite Tunnels")
                    || PlayerLocationChecker.getLocation().equalsIgnoreCase("Dwarven Base Camp")
                    || PlayerLocationChecker.getLocation().equalsIgnoreCase("Glacite Mineshafts")) {
                shaftDisplay.render(Minecraft.getMinecraft());
            }
        }
        // Only render if feature is turned on
        if(FeatureSettings.isFeatureEnabled("HUD","Split or Steal HUD")) {
            sosDisplay.render(Minecraft.getMinecraft());
        }
    }
}
