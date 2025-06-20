package com.skyrats.mixin.client;

import com.skyrats.Backend.GUIs.Features;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorizontalConnectingBlock.class)
public class GlassPaneHitboxMixin {

	@Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
	private void onGetOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (isGlassPane(state) && isCrossShape(state) && Features.isFeatureEnabled("Others", "1.8.9 Glass Pane")) {
			cir.setReturnValue(VoxelShapes.fullCube());
		}
	}

	@Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
	private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (isGlassPane(state) && isCrossShape(state) && Features.isFeatureEnabled("Others", "1.8.9 Glass Pane")) {
			cir.setReturnValue(VoxelShapes.fullCube());
		}
	}

	private boolean isGlassPane(BlockState state) {
		return state.getBlock() instanceof StainedGlassPaneBlock
				|| state.getBlock() == Blocks.GLASS_PANE;
	}

	private boolean isCrossShape(BlockState state) {
		// Connected on all four sides (north, south, east, west)
		return state.get(Properties.NORTH)
				&& state.get(Properties.SOUTH)
				&& state.get(Properties.EAST)
				&& state.get(Properties.WEST);
	}
}