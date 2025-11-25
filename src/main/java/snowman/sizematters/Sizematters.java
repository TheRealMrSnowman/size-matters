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
		LOGGER.info(CONFIG.max_height()+" Is the max set height!");
		LOGGER.info("Loaded Sizes!");
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            
			IEntityDataSaver saver = (IEntityDataSaver) handler.player;
            NbtCompound compound = saver.getPersistentData();	
			if (!compound.contains("player_height")) {
				var r = new Random();
				if (CONFIG.uuid_dependant()) {
					r.setSeed(handler.player.getUuid().getLeastSignificantBits());
				}
				var s = r.nextDouble();
				var min = CONFIG.min_height();
				var max = CONFIG.max_height();
				var newn = s*(max-min)+min;
				compound.putDouble("player_height", newn);
			}		
			double n = compound.getDouble("player_height");
			LOGGER.info("Player Connected!");
			LOGGER.info(n+" IS THE PLAYERS SET HEIGHT");
			
			// gets the player name of the person who just connected (i dont know if this works when players make their own custom names)
			var name = handler.player.getEntityName();
			
			// waits 10 milliseconds to run the command, otherwise the command runs before the player is considered connected
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							try {
								var command = String.format("scale set pehkui:height %.2f %s", n, name);
								LOGGER.info(command);
								server.getCommandManager().getDispatcher().execute(command,
										server.getCommandSource()); // RootCommandNode<ServerCommandSource>
								server.getCommandManager().getDispatcher().execute("scale set pehkui:width "+n+" "+name,
										server.getCommandSource()); // RootCommandNode<ServerCommandSource>
								// handler.player.sendMessage(Text.of("Hello "+name+", you are "+1.8*n+" blocks tall. ("+180*n+" cm) or "+n*100+"% of normal size."));
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
					}, 100);

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
			LOGGER.info("Player Connected!");
			LOGGER.info(n+" IS THE PLAYERS SET HEIGHT");
			
			// gets the player name of the person who just connected (i dont know if this works when players make their own custom names)
			var name = newPlayer.getEntityName();
			
			// waits 10 milliseconds to run the command, otherwise the command runs before the player is considered connected
			try {
				   var server = newPlayer.server;

				var command = String.format("scale set pehkui:height %.2f %s", n, name);
				LOGGER.info(command);
				server.getCommandManager().getDispatcher().execute(command,
						server.getCommandSource()); // RootCommandNode<ServerCommandSource>
				server.getCommandManager().getDispatcher().execute("scale set pehkui:width "+n+" "+name,
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