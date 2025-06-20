package com.Zephyr.Core.Features.Notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AlertMessagePopup {
    private String msg;
    private long displayTime;

    public void display(String msg, long displayTime) {
        this.msg = msg;
        this.displayTime = System.currentTimeMillis() + displayTime;
    }

    //Renders text on screen
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (System.currentTimeMillis() < displayTime && msg != null && !msg.isEmpty()) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            int screenWidth = res.getScaledWidth();
            int screenHeight = res.getScaledHeight();

            //Scaling
            float scale = 5.0F;
            int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(msg);
            int stringHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;

            // Calculate center position, then scale down
            float x = (screenWidth / 2.0f - stringWidth * scale / 2.0f) / scale;
            float y = (screenHeight / 2.0f - stringHeight * scale / 2.0f) / scale;

            //Shows scaled text on screen
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(EnumChatFormatting.DARK_AQUA + msg,
                    Math.round(x),
                    Math.round(y),
                    0);

            GlStateManager.popMatrix();
        }
    }
}
