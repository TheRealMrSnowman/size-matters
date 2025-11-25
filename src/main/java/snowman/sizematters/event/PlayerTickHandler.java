package snowman.sizematters.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import snowman.sizematters.util.IEntityDataSaver;


public class PlayerTickHandler implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            IEntityDataSaver saver = (IEntityDataSaver) player;
            NbtCompound compound = saver.getPersistentData();
            compound.putInt("player_height", 1);
        }
    }
}