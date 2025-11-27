package snowman.sizematters;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import snowman.sizematters.util.IEntityDataSaver;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wispforest.owo.config.annotation.Modmenu;


public class Sizematters implements ModInitializer {
	public static final String MOD_ID = "sizematters";
	public static final SizeMattersConfig CONFIG = SizeMattersConfig.createAndLoad();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Event<Disconnect> JOIN = EventFactory.createArrayBacked(Disconnect.class,
			callbacks -> (handler, sender, server) -> {
				for (Disconnect callback : callbacks) {
					callback.onPlayReady(handler, sender, server);
				}
			});

	@Override
	public void onInitialize() {
		LOGGER.info("Big or small its how you use it that counts!");
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            LOGGER.info("A Player Has Connected!");
			IEntityDataSaver saver = (IEntityDataSaver) handler.player;
            NbtCompound compound = saver.getPersistentData();	
			if (!compound.contains("player_height")) {
				var r = new Random();
				if (CONFIG.uuid_dependant()) {
					LOGGER.info("UUID Dependant Height Is Active");
					r.setSeed(handler.player.getUuid().getLeastSignificantBits());
					LOGGER.info("Using player UUID of "+handler.player.getUuid().getLeastSignificantBits()+" as height seed.");
				}
				var s = r.nextDouble();
				var min = CONFIG.min_height();
				var max = CONFIG.max_height();
				var newn = s*(max-min)+min;
				compound.putDouble("player_height", newn);
			}		
			double n = compound.getDouble("player_height");
			
			// gets the player name of the person who just connected (i dont know if this works when players make their own custom names)
			var name = handler.player.getName().getString();
			LOGGER.info("Player name is: "+name);
			LOGGER.info("Giving "+name+" a height mod of "+n);
			// waits 10 milliseconds to run the command, otherwise the command runs before the player is considered connected
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							try {
								var command = String.format("attribute %s minecraft:scale modifier add sizematters:rescaling %.2f add_multiplied_base", name, (float)n);
								server.getCommandManager().getDispatcher().execute(command,
										server.getCommandSource()); // RootCommandNode<ServerCommandSource>
								// handler.player.sendMessage(Text.of("Hello "+name+", you are "+1.8*n+" blocks tall. ("+180*n+" cm) or "+n*100+"% of normal size."));
							} catch (Exception e) {
								LOGGER.info("Encountered an issue, let Mr Snowman know");
								LOGGER.info(e.getMessage());
							}
						}
					}, 1000);

			// Says the name of the selected player in the chat for debug purposes
			// handler.player.sendMessage(Text.of(name));
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			IEntityDataSaver oldSaver = (IEntityDataSaver) oldPlayer;
			IEntityDataSaver newSaver = (IEntityDataSaver) newPlayer;
            NbtCompound oldCompound = oldSaver.getPersistentData();	
			var newCompound = newSaver.getPersistentData();
			double n = oldCompound.getDouble("player_height");
			newCompound.putDouble("player_height", n);
			
			// gets the player name of the person who just connected (i dont know if this works when players make their own custom names)
			var name = newPlayer.getName().getString();
			
			// waits 10 milliseconds to run the command, otherwise the command runs before the player is considered connected
			try {
				   var server = newPlayer.server;

				var command = String.format("attribute %s minecraft:scale modifier add sizematters:rescaling %.2f add_multiplied_base", name, n);
				server.getCommandManager().getDispatcher().execute(command,
						server.getCommandSource()); // RootCommandNode<ServerCommandSource>
				// handler.player.sendMessage(Text.of("Hello "+name+", you are "+1.8*n+" blocks tall. ("+180*n+" cm) or "+n*100+"% of normal size."));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
	}

	

	@FunctionalInterface
	public interface Disconnect {
		void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server);
	}
}