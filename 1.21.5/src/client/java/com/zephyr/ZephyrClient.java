package com.zephyr;

import com.zephyr.Backend.GUIs.GuiOpener;
import net.fabricmc.api.ClientModInitializer;

public class ZephyrClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GuiOpener.register();
	}
}