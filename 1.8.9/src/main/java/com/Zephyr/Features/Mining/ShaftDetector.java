package com.Zephyr.Features.Mining;

import com.Zephyr.Core.Features.Mineshafts.MineshaftTracker;
import com.Zephyr.Core.Features.PlayerLocationChecker;
import com.Zephyr.Core.GUI.FeatureSettings;
import com.Zephyr.Core.Features.Mineshafts.ShaftTypes;
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
    private int incrementJasp = 70;
    private int tickCooldown = 0;
    private static final int COOLDOWN_TIME = 20;

    public ShaftDetector(MineshaftTracker tracker) {
        this.tracker = tracker;
    }

    //Detect blocks on player's screen
    public void detectGemstones() {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;

        Vec3 eyePos = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
        Vec3 lookVec = Minecraft.getMinecraft().thePlayer.getLook(1.0f);

        final int numRays = 40;          // More rays = wider coverage. Too much will cause lag so beware.
        final float coneAngle = 1.35f;    // Wider angle (in radians)
        final double maxDistance = 16.0; // Max block detection distance

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
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SR ShaftDetection] Detected: "
                    + colorName));
            Minecraft.getMinecraft().thePlayer.playSound("random.levelup", 2.0F, 1.0F);
            typeSent = true;
        }
    }

    //Return type of Mineshaft from block type
    private ShaftTypes mapBlockToShaftType(Block block, IBlockState state) {
        int meta = block.getMetaFromState(state);
        // Maps gemstone blocks to ShaftTypes here
        if(block == Blocks.gold_block) {
            tracker.incrementSinceJasper();
            return ShaftTypes.VANGUARD;
        }else if(block == Blocks.stone) {
            //Polished Diorite
            if(meta == 4) {
                tracker.incrementSinceJasper();
                return ShaftTypes.TITANIUM;
            }
        }else if(block == Blocks.clay) {
            tracker.incrementSinceJasper();
            return ShaftTypes.TUNGSTEN;
        }else if(block == Blocks.stained_hardened_clay) {
            //Brown Terracotta
            if(meta == 12) {
                tracker.incrementSinceJasper();
                return ShaftTypes.UMBER;
            }
        }else if(block == Blocks.stained_glass || block == Blocks.stained_glass_pane) {
            //Gets the glass color
            switch(meta) {
                //White -> Opal
                case 0:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.OPAL;
                //Orange -> Amber
                case 1:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.AMBER;
                //Magenta -> Jasper
                case 2:
                    tracker.resetSinceJasper();
                    return ShaftTypes.JASPER;
                //Light blue -> Sapphire
                case 3:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.SAPPHIRE;
                //Yellow -> Topaz
                case 4:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.TOPAZ;
                //Lime -> Jade
                case 5:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.JADE;
                //Purple -> Amethyst
                case 10:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.AMETHYST;
                //Blue -> Aquamarine
                case 11:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.AQUAMARINE;
                //Brown -> Citrine
                case 12:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.CITRINE;
                //Green -> Peridot
                case 13:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.PERIDOT;
                //Red -> Ruby
                case 14:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.RUBY;
                //Black -> Onyx
                case 15:
                    tracker.incrementSinceJasper();
                    return ShaftTypes.ONYX;
            }
        }
        return null;
    }

    //Check Mineshaft every 20 ticks (1 seconds) if player is in Mineshaft
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
        if(!FeatureSettings.isFeatureEnabled("Mining", "Mineshaft Tracker")) return;
        //Get player's skyblock location
        String location = PlayerLocationChecker.getLocation();
        if(location.equalsIgnoreCase("N/A")) return;
        //Is player in mineshaft currently
        boolean isInShaft = location.equalsIgnoreCase("Glacite Mineshafts");
        //Player entered a shaft
        if(isInShaft && !wasInShaft) {
            checked = false;
        }

        if(isInShaft && !checked && !typeSent) {
            detectGemstones();
        }

        wasInShaft = isInShaft;

        //Additional condition to prevent double check if server lags/location flickering while inside Mineshaft
        if((location.equalsIgnoreCase("Glacite Tunnels") || location.equalsIgnoreCase("Dwarven Base Camp")) && typeSent) {
            typeSent = false;
            //If player didn't get Jasper in certain amount of mineshafts, make fun of them and give them the odds of that happens :)
            if(tracker.getSinceJasper() == 40) {
                Minecraft.getMinecraft().thePlayer.playSound("mob.villager.death", 2.0F, 1.0F);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("You didn't get a "
                        + EnumChatFormatting.LIGHT_PURPLE + "Jasper" + EnumChatFormatting.RESET + " in " + tracker.getSinceJasper() +
                        " mineshafts! That's ~ " + EnumChatFormatting.DARK_RED + "7.6%" + EnumChatFormatting.RESET +
                        "YIKES!"));
            }else if(tracker.getSinceJasper() == 72) {
                Minecraft.getMinecraft().thePlayer.playSound("mob.villager.death", 2.0F, 1.0F);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("You didn't get a "
                        + EnumChatFormatting.LIGHT_PURPLE + "Jasper" + EnumChatFormatting.RESET + " in " + tracker.getSinceJasper() +
                        " mineshafts! That's ~ " + EnumChatFormatting.DARK_RED + "1%" + EnumChatFormatting.RESET +
                        "YIKES!"));
            }else if(tracker.getSinceJasper() >= (incrementJasp + 30)) {
                incrementJasp += 30;
                Minecraft.getMinecraft().thePlayer.playSound("mob.villager.death", 2.0F, 1.0F);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("You didn't get a "
                        + EnumChatFormatting.LIGHT_PURPLE + "Jasper" + EnumChatFormatting.RESET + " in " + tracker.getSinceJasper() +
                        " mineshafts! That's less than or equal to " + EnumChatFormatting.DARK_RED + "0.26%" + EnumChatFormatting.RESET +
                        " or in fact theoretically impossible! Keep mining bucko."));
            }
        }
    }
}
