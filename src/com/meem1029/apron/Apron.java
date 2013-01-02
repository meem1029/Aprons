package com.meem1029.apron;
/*
 * Apron Plugin
 * Written by meem1029.
 */

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Apron extends JavaPlugin implements Listener{
	public Map<String, ApronEq> players;
	private String chainTeamName = "Chainmail Children";
	private String goldTeamName = "Golden Grannies";
	private Logger log;
	private Map<String, Integer> materialMap;
	private Map<String, Integer> slotMap;
	private Map<String, Color> colorMap;
	
	private ApronEq fishNetEq = new ApronEq(ApronEq.CHAIN,ApronEq.HEAD);
	private ApronEq butterHeadEq = new ApronEq(ApronEq.GOLD,ApronEq.HEAD);
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		players = new HashMap<String, ApronEq>();
		log = Logger.getLogger("Minecraft");
		log.info("Apron Plugin enabled!");
		writeMaps();
	}
	
	private void writeMaps(){
		materialMap =  new HashMap<String, Integer>();
		materialMap.put("gold", ApronEq.GOLD);
		materialMap.put("diamond", ApronEq.DIAMOND);
		materialMap.put("leather", ApronEq.LEATHER);
		materialMap.put("chain", ApronEq.CHAIN);
		materialMap.put("iron", ApronEq.IRON);
		
		slotMap = new HashMap<String, Integer>();
		slotMap.put("head", ApronEq.HEAD);
		slotMap.put("helmet", ApronEq.HEAD);
		slotMap.put("hat", ApronEq.HEAD);
		slotMap.put("chest", ApronEq.CHEST);
		slotMap.put("apron", ApronEq.CHEST);
		slotMap.put("legs", ApronEq.LEGS);
		slotMap.put("leggings", ApronEq.LEGS);
		slotMap.put("feet", ApronEq.FEET);
		slotMap.put("boots", ApronEq.FEET);
		
		colorMap = new HashMap<String,Color>();
		colorMap.put("WHITE", Color.WHITE);
		colorMap.put("SILVER", Color.SILVER);
		colorMap.put("GRAY", Color.GRAY);
		colorMap.put("BLACK", Color.BLACK);
		colorMap.put("RED", Color.RED);
		colorMap.put("MAROON", Color.MAROON);
		colorMap.put("YELLOW", Color.YELLOW);
		colorMap.put("OLIVE", Color.OLIVE);
		colorMap.put("LIME", Color.LIME);
		colorMap.put("GREEN", Color.GREEN);
		colorMap.put("AQUA", Color.AQUA);
		colorMap.put("TEAL", Color.TEAL);
		colorMap.put("BLUE", Color.BLUE);
		colorMap.put("NAVY", Color.NAVY);
		colorMap.put("FUCHSIA", Color.FUCHSIA);
		colorMap.put("PURPLE", Color.PURPLE);
		colorMap.put("ORANGE", Color.ORANGE);
	}
	
	public void onDisable(){
		log.info("Apron Plugin disabled. :(");
	}
	
	@EventHandler
	public void inventoryEvent(InventoryCloseEvent event){
		HumanEntity p = event.getPlayer();
		if( p instanceof Player){
			Player player = (Player) p;
			updateInventory(player);	
		}
	}
	
	@EventHandler
	public void invUpdater(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		updateInventory(player);
	}
	
	@EventHandler
	public void respawnEvent(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		updateInventory(player);
	}
	
	private void updateInventory(Player player){
		ApronEq playerEq = players.get(player.getName());
		if(playerEq != null){
			playerEq.manageEquipment(player);
		}
	}
	
	@EventHandler
	public void signPlaceEvent(SignChangeEvent e){
		Player p = e.getPlayer();
		if(p.isOp()){
			return;
		}
		String s = e.getLine(0);
		if("[Apron]".equalsIgnoreCase(s) || "[DewHelmet]".equalsIgnoreCase(s)){
			p.sendMessage("Sorry, only ops are allowed to place Apron signs.");
			log.info("Player " + p.getName() + " attempted to place an Apron compatible sign without op.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void signClickEvent(PlayerInteractEvent event){
		Material blockType;
		try{
		Block block = event.getClickedBlock();
		if(block != null){
			blockType = block.getType();
		}
		else{
			return;
		}
		Player player = event.getPlayer();
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
				(blockType == Material.WALL_SIGN ||
				blockType == Material.SIGN_POST)){
			Sign sign = (Sign) block.getState();
			String[] lines = sign.getLines();
			parseSign(lines,player);
			updateInventory(player);
			}
		}
		catch(Exception e){
			log.info("Error: "+ e.getMessage());
		}
	}
	
	private void parseSign(String[] lines, Player player){
		int material;
		int slot;
		String team;
		ApronEq eq;
		if(lines[0].equalsIgnoreCase("[Apron]")){
			try{
				material = materialMap.get(lines[1].toLowerCase());
				slot = slotMap.get(lines[2].toLowerCase());
				if(material == ApronEq.LEATHER){
					try{
						String colorString = lines[3];
						ItemStack armor = new ItemStack(material+slot,1);
						Color color = readColorString(colorString);
						LeatherArmorMeta lam = (LeatherArmorMeta) armor.getItemMeta();
						lam.setColor(color);
						armor.setItemMeta(lam);
						addToTeam(player,new ApronEq(armor,slot),lines[3]);
						return;
					}
					catch(Exception e){
						//log.info("Error: " + e.toString());
					}
				}
				addToTeam(player,new ApronEq(material,slot), lines[3]);
			}
			catch(Exception e){
				//log.info("Error Parsing sign. :(");
			}
		}
		//Note: These lines will be changed soon once I create the ability to do custom signs.
		else if(lines[0].equalsIgnoreCase("[DewHelmet]")){
			if(lines[1].equalsIgnoreCase("Fish Nets")){
				addToTeam(player,fishNetEq,"Fish Nets");
			}
			if(lines[1].equalsIgnoreCase("Butter Heads")){
				addToTeam(player,butterHeadEq,"Butter Heads");
			}
		}	
	}
	
	private void addToTeam(Player player, ApronEq eq, String teamName){
		players.put(player.getName(), eq);
		player.sendMessage("Welcome to team " + teamName + "!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("reset-teams")){
			if(sender.isOp()){
				players.clear();
			}
			else{
				sender.sendMessage("Sorry, only ops can reset teams.");
			}
		}
		return true;
	}
	
	private Color readColorString(String colorString){
		if(colorString.startsWith("Color:")){
			colorString = colorString.split("Color:")[1];
			return Color.fromRGB(Integer.parseInt(colorString, 16));
		}
		else{
			return colorMap.get(colorString.toUpperCase());
		}
	}
	
}
