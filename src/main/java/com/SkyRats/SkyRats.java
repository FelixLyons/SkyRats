package com.SkyRats;

import com.SkyRats.Core.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SkyRats.MODID, version = SkyRats.VERSION)
public class SkyRats
{
    public static final String MODID = "skyrats";
    public static final String VERSION = "0.07";

    //Client Proxy. All client-sided related stuff will run on this proxy.
    public static ClientProxy proxy = new ClientProxy();

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
