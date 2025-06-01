package com.SkyRats.Features.Mining;

import com.SkyRats.Core.Features.MineshaftTracker;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.Features.ShaftTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ShaftDetector {
    private MineshaftTracker tracker;

    public ShaftDetector(MineshaftTracker tracker) {
        this.tracker = tracker;
    }

    public void detectGemstones() {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;

        // Create frustum from current camera info. Frustum is used for what the player sees on screen.
        Frustum frustum = new Frustum();
        frustum.setPosition(Minecraft.getMinecraft().getRenderViewEntity().posX, Minecraft.getMinecraft().getRenderViewEntity().posY, Minecraft.getMinecraft().getRenderViewEntity().posZ);

        BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();

        ShaftTypes detectedType = null;

        // Check blocks in a radius around the player
        int radius = 6;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    IBlockState state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
                    Block block = state.getBlock();

                    // Get the bounding box of the block
                    AxisAlignedBB aabb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1
                    );


                    // Check if the block is in the camera frustum
                    if (frustum.isBoundingBoxInFrustum(aabb)) {
                        ShaftTypes type = mapBlockToShaftType(block, state);
                        if (type != null) {
                            detectedType = type;
                            break;
                        }
                    }
                }
                if (detectedType != null) break;
            }
            if (detectedType != null) break;
        }

        if (detectedType != null) {
            tracker.incrementShaft(detectedType);
            System.out.println("Detected gemstone on screen: " + detectedType);
        }
    }

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

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        //Stops if player or world is null.
        if(Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        //Get player's skyblock location
        String location = PlayerLocationChecker.getLocation();
        if(location != null && location.equalsIgnoreCase("Glacite Mineshaft")) {
            detectGemstones();
        }
    }
}
