package me.aleiv.core.paper.worlds;

import java.util.Random;

import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

    @Override
    @NonNull
    public ChunkData generateChunkData(@NonNull World world, @NonNull Random random, int x, int z, @NonNull BiomeGrid biome) {
        return createChunkData(world);
    }

}