package net.mcreator.brokendimensionbbs.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PortalBeaconBlock extends Block {
	public PortalBeaconBlock() {
		super(BlockBehaviour.Properties.of()
				.sound(SoundType.AMETHYST)
				.strength(3f, 6f)
				.lightLevel(s -> 7));
	}
}
