package com.SkyRats.Features.Mining;

import com.SkyRats.Core.Features.MineshaftTracker;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.Features.SettingsManager;
import com.SkyRats.Core.Features.ShaftTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ShaftDetector {
    private MineshaftTracker tracker;
    private boolean checked = false;
    private boolean wasInShaft = false;
    private boolean typeSent = false;
    private int tickCooldown = 0;
    private static final int COOLDOWN_TIME = 35;

    public ShaftDetector(MineshaftTracker tracker) {
        this.tracker = tracker;
    }

    //Detect blocks on player's screen
    public void detectGemstones() {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;

        Vec3 eyePos = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
        Vec3 lookVec = Minecraft.getMinecraft().thePlayer.getLook(1.0f);

        final int numRays = 35;          // More rays = wider coverage. Too much will cause lag so beware.
        final float coneAngle = 1.33f;    // Wider angle (in radians)
        final double maxDistance = 15.0; // Max block detection distance

        ShaftTypes detectedType = null;

        for (int i = 0; i < numRays; i++) {
            // Random direction slightly offset from main lookVec
            Vec3 randomVec = lookVec
                    .addVector(
                            (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5f) * coneAngle,
                            (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5f) * coneAngle,
                            (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5f) * coneAngle
                    )
                    .normalize();

            Vec3 rayEnd = eyePos.addVector(randomVec.xCoord * maxDistance, randomVec.yCoord * maxDistance, randomVec.zCoord * maxDistance);
            MovingObjectPosition hit = Minecraft.getMinecraft().theWorld.rayTraceBlocks(eyePos, rayEnd);

            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = hit.getBlockPos();
                IBlockState state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
                Block block = state.getBlock();

                ShaftTypes type = mapBlockToShaftType(block, state);
                if (type != null) {
                    detectedType = type;
                    break;
                }
            }
        }

        if (detectedType != null && !checked) {
            tracker.incrementShaft(detectedType);
            checked = true;
            //Combine color code + string then reset formatting for next
            String colorName = detectedType.getColor() + detectedType.name() + EnumChatFormatting.RESET;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SR ShaftDetection] Detected: " + colorName));
            typeSent = true;
        }
    }

    //Return type of Mineshaft from block type
    private ShaftTypes mapBlockToShaftType(Block block, IBlockState state) {
        int meta = block.getMetaFromState(state);
        // Maps gemstone blocks to ShaftTypes here
        if(block == Blocks.gold_block) {
            return ShaftTypes.VANGUARD;
        }else if(block == Blocks.stone) {
            //Polished Diorite
            if(meta == 4) {
                return ShaftTypes.TITANIUM;
            }
        }else if(block == Blocks.clay) {
            return ShaftTypes.TUNGSTEN;
        }else if(block == Blocks.stained_hardened_clay) {
            //Orange Terracotta
            if(meta == 1) {
                return ShaftTypes.UMBER;
            }
        }else if(block == Blocks.stained_glass) {
            //Gets the glass color
            switch(meta) {
                //White -> Opal
                case 0: return ShaftTypes.OPAL;
                //Orange -> Amber
                case 1: return ShaftTypes.AMBER;
                //Magenta -> Jasper
                case 2: return ShaftTypes.JASPER;
                //Light blue -> Sapphire
                case 3: return ShaftTypes.SAPPHIRE;
                //Yellow -> Topaz
                case 4: return ShaftTypes.TOPAZ;
                //Lime -> Jade
                case 5: return ShaftTypes.JADE;
                //Purple -> Amethyst
                case 10: return ShaftTypes.AMETHYST;
                //Blue -> Aquamarine
                case 11: return ShaftTypes.AQUAMARINE;
                //Brown -> Citrine
                case 12: return ShaftTypes.CITRINE;
                //Green -> Peridot
                case 13: return ShaftTypes.PERIDOT;
                //Red -> Ruby
                case 14: return ShaftTypes.RUBY;
                //Black -> Onyx
                case 15: return ShaftTypes.ONYX;
            }
        }
        return null;
    }

    //Check Mineshaft every 35 ticks (3.5 seconds) if player is in Mineshaft
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        //CHeck if player or world is not loaded.
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;
        //Tick Cooldown to help with performance
        if(tickCooldown > 0) {
            tickCooldown--;
            return;
        }

        //Reset cooldown
        tickCooldown = COOLDOWN_TIME;

        //Checks if Mineshaft Tracker feature is turned on.
        if(!SettingsManager.isFeatureEnabled("Mining", "Mineshaft Tracker")) return;
        //Get player's skyblock location
        String location = PlayerLocationChecker.getLocation();
        if(location.equalsIgnoreCase("N/A")) return;
        //Is player in mineshaft currently
        boolean isInShaft = location.equalsIgnoreCase("Glacite Mineshafts");
        //Player entered a shaft
        if(isInShaft && !wasInShaft) {
            checked = false;
        }

        if(isInShaft && !checked) {
            detectGemstones();
            Minecraft.getMinecraft().thePlayer.playSound("random_orb", 1.0F, 1.0F); //Play xp orb sound after detect
        }

        wasInShaft = isInShaft;

        //Additional condition to prevent double check if server lags/location flickering while inside Mineshaft
        if(location.equalsIgnoreCase("Glacite Tunnels") || location.equalsIgnoreCase("Dwarven Base Camp")) typeSent = false;
    }
}
