/**
 * MockWorld.java is a part of Joystick
 *
 * Copyright (c) 2016 Anand Kumar
 *
 * Joystick is a free software: You can redistribute it or modify it
 * under the terms of the GNU General Public License published by the Free
 * Software Foundation, either version 3 of the license of any later version.
 * 
 * Joystick is distributed in the intent of being useful. However, there
 * is NO WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You can view a copy of the GNU General Public License at 
 * <http://www.gnu.org/licenses/> if you have not received a copy.
 */
package com.valygard.aohruthless.util;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * @author Anand
 * 
 */
public class MockWorld implements World {

	private final String worldName;

	public MockWorld(String worldName) {
		this.worldName = worldName;
	}

	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {}

	@Override
	public Set<String> getListeningPluginChannels() {
		return null;
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		return null;
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		return false;
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		return new Location(this, x, y, z).getBlock();
	}

	@Override
	public Block getBlockAt(Location location) {
		return location.getBlock();
	}

	@Override
	public int getBlockTypeIdAt(int x, int y, int z) {
		return 0;
	}

	@Override
	public int getBlockTypeIdAt(Location location) {
		return 0;
	}

	@Override
	public int getHighestBlockYAt(int x, int z) {
		return 0;
	}

	@Override
	public int getHighestBlockYAt(Location location) {
		return 0;
	}

	@Override
	public Block getHighestBlockAt(int x, int z) {
		return null;
	}

	@Override
	public Block getHighestBlockAt(Location location) {
		return null;
	}

	@Override
	public Chunk getChunkAt(int x, int z) {
		return null;
	}

	@Override
	public Chunk getChunkAt(Location location) {
		return null;
	}

	@Override
	public Chunk getChunkAt(Block block) {
		return null;
	}

	@Override
	public boolean isChunkLoaded(Chunk chunk) {
		return false;
	}

	@Override
	public Chunk[] getLoadedChunks() {
		return null;
	}

	@Override
	public void loadChunk(Chunk chunk) {}

	@Override
	public boolean isChunkLoaded(int x, int z) {
		return false;
	}

	@Override
	public boolean isChunkInUse(int x, int z) {
		return false;
	}

	@Override
	public void loadChunk(int x, int z) {}

	@Override
	public boolean loadChunk(int x, int z, boolean generate) {
		return false;
	}

	@Override
	public boolean unloadChunk(Chunk chunk) {
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z) {
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save) {
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
		return false;
	}

	@Override
	public boolean unloadChunkRequest(int x, int z) {
		return false;
	}

	@Override
	public boolean unloadChunkRequest(int x, int z, boolean safe) {
		return false;
	}

	@Override
	public boolean regenerateChunk(int x, int z) {
		return false;
	}

	@Override
	public boolean refreshChunk(int x, int z) {
		return false;
	}

	@Override
	public Item dropItem(Location location, ItemStack item) {
		return null;
	}

	@Override
	public Item dropItemNaturally(Location location, ItemStack item) {
		return null;
	}

	@Override
	public Arrow spawnArrow(Location location, Vector direction, float speed,
			float spread) {
		return null;
	}

	@Override
	public <T extends Arrow> T spawnArrow(Location location, Vector direction,
			float speed, float spread, Class<T> clazz) {
		return null;
	}

	@Override
	public boolean generateTree(Location location, TreeType type) {
		return false;
	}

	@Override
	public boolean generateTree(Location loc, TreeType type,
			BlockChangeDelegate delegate) {

		return false;
	}

	@Override
	public Entity spawnEntity(Location loc, EntityType type) {
		return null;
	}

	@Override
	public LightningStrike strikeLightning(Location loc) {
		return null;
	}

	@Override
	public LightningStrike strikeLightningEffect(Location loc) {
		return null;
	}

	@Override
	public List<Entity> getEntities() {
		return null;
	}

	@Override
	public List<LivingEntity> getLivingEntities() {
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(
			@SuppressWarnings("unchecked") Class<T>... classes) {
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
		return null;
	}

	@Override
	public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
		return null;
	}

	@Override
	public List<Player> getPlayers() {
		return null;
	}

	@Override
	public Collection<Entity> getNearbyEntities(Location location, double x,
			double y, double z) {
		return null;
	}

	@Override
	public String getName() {
		return worldName;
	}

	@Override
	public UUID getUID() {
		return null;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public boolean setSpawnLocation(int x, int y, int z) {
		return false;
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public void setTime(long time) {}

	@Override
	public long getFullTime() {
		return 0;
	}

	@Override
	public void setFullTime(long time) {}

	@Override
	public boolean hasStorm() {
		return false;
	}

	@Override
	public void setStorm(boolean hasStorm) {}

	@Override
	public int getWeatherDuration() {
		return 0;
	}

	@Override
	public void setWeatherDuration(int duration) {}

	@Override
	public boolean isThundering() {
		return false;
	}

	@Override
	public void setThundering(boolean thundering) {}

	@Override
	public int getThunderDuration() {
		return 0;
	}

	@Override
	public void setThunderDuration(int duration) {}

	@Override
	public boolean createExplosion(double x, double y, double z, float power) {
		return false;
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power,
			boolean setFire) {
		return false;
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power,
			boolean setFire, boolean breakBlocks) {
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power) {
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power, boolean setFire) {
		return false;
	}

	@Override
	public Environment getEnvironment() {
		return null;
	}

	@Override
	public long getSeed() {
		return 0;
	}

	@Override
	public boolean getPVP() {
		return false;
	}

	@Override
	public void setPVP(boolean pvp) {}

	@Override
	public ChunkGenerator getGenerator() {
		return null;
	}

	@Override
	public void save() {}

	@Override
	public List<BlockPopulator> getPopulators() {
		return null;
	}

	@Override
	public <T extends Entity> T spawn(Location location, Class<T> clazz)
			throws IllegalArgumentException {
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, Material material,
			byte data) throws IllegalArgumentException {
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, int blockId,
			byte blockData) throws IllegalArgumentException {
		return null;
	}

	@Override
	public void playEffect(Location location, Effect effect, int data) {}

	@Override
	public void playEffect(Location location, Effect effect, int data,
			int radius) {}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data) {}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data,
			int radius) {}

	@Override
	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z,
			boolean includeBiome, boolean includeBiomeTempRain) {
		return null;
	}

	@Override
	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {}

	@Override
	public boolean getAllowAnimals() {
		return false;
	}

	@Override
	public boolean getAllowMonsters() {
		return false;
	}

	@Override
	public Biome getBiome(int x, int z) {
		return null;
	}

	@Override
	public void setBiome(int x, int z, Biome bio) {}

	@Override
	public double getTemperature(int x, int z) {
		return 0;
	}

	@Override
	public double getHumidity(int x, int z) {
		return 0;
	}

	@Override
	public int getMaxHeight() {
		return 256;
	}

	@Override
	public int getSeaLevel() {
		return 64;
	}

	@Override
	public boolean getKeepSpawnInMemory() {
		return false;
	}

	@Override
	public void setKeepSpawnInMemory(boolean keepLoaded) {}

	@Override
	public boolean isAutoSave() {
		return false;
	}

	@Override
	public void setAutoSave(boolean value) {}

	@Override
	public void setDifficulty(Difficulty difficulty) {}

	@Override
	public Difficulty getDifficulty() {
		return null;
	}

	@Override
	public File getWorldFolder() {
		return null;
	}

	@Override
	public WorldType getWorldType() {
		return null;
	}

	@Override
	public boolean canGenerateStructures() {
		return false;
	}

	@Override
	public long getTicksPerAnimalSpawns() {
		return 0;
	}

	@Override
	public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {}

	@Override
	public long getTicksPerMonsterSpawns() {
		return 0;
	}

	@Override
	public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {}

	@Override
	public int getMonsterSpawnLimit() {
		return 0;
	}

	@Override
	public void setMonsterSpawnLimit(int limit) {}

	@Override
	public int getAnimalSpawnLimit() {
		return 0;
	}

	@Override
	public void setAnimalSpawnLimit(int limit) {}

	@Override
	public int getWaterAnimalSpawnLimit() {
		return 0;
	}

	@Override
	public void setWaterAnimalSpawnLimit(int limit) {}

	@Override
	public int getAmbientSpawnLimit() {
		return 0;
	}

	@Override
	public void setAmbientSpawnLimit(int limit) {}

	@Override
	public void playSound(Location location, Sound sound, float volume,
			float pitch) {}

	@Override
	public void playSound(Location location, String sound, float volume,
			float pitch) {}

	@Override
	public String[] getGameRules() {
		return null;
	}

	@Override
	public String getGameRuleValue(String rule) {
		return null;
	}

	@Override
	public boolean setGameRuleValue(String rule, String value) {
		return false;
	}

	@Override
	public boolean isGameRule(String rule) {
		return false;
	}

	@Override
	public WorldBorder getWorldBorder() {
		return null;
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count) {}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z,
			int count) {}

	@Override
	public <T> void spawnParticle(Particle particle, Location location,
			int count, T data) {}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y,
			double z, int count, T data) {}

	@Override
	public void spawnParticle(Particle particle, Location location, int count,
			double offsetX, double offsetY, double offsetZ) {}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z,
			int count, double offsetX, double offsetY, double offsetZ) {}

	@Override
	public <T> void spawnParticle(Particle particle, Location location,
			int count, double offsetX, double offsetY, double offsetZ, T data) {}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y,
			double z, int count, double offsetX, double offsetY,
			double offsetZ, T data) {}

	@Override
	public void spawnParticle(Particle particle, Location location, int count,
			double offsetX, double offsetY, double offsetZ, double extra) {}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z,
			int count, double offsetX, double offsetY, double offsetZ,
			double extra) {}

	@Override
	public <T> void spawnParticle(Particle particle, Location location,
			int count, double offsetX, double offsetY, double offsetZ,
			double extra, T data) {}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y,
			double z, int count, double offsetX, double offsetY,
			double offsetZ, double extra, T data) {}

}
