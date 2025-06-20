package com.skyrats;

import com.skyrats.Backend.GUIs.GuiOpener;
import net.fabricmc.api.ClientModInitializer;

public class SkyRatsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GuiOpener.register();
	}
}