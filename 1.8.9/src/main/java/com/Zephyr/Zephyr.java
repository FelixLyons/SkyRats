package com.Zephyr;

import com.Zephyr.Core.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Zephyr.MODID, version = Zephyr.VERSION)
public class Zephyr
{
    public static final String MODID = "zephyr";
    public static final String VERSION = "1.0.4";

    //Client Proxy. All client-sided related stuff will run on this proxy.
    public static ClientProxy proxy = new ClientProxy();

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
