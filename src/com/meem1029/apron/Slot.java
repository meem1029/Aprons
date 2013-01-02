package com.meem1029.apron;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Slot {

	private int type;
	public Slot(int type){
		this.type = type;
	}
	
	public int getNum(){
		return type;
	}
	
	public ItemStack getEquipment(PlayerInventory inv){
		switch(type){
		
			case ApronEq.HEAD: return inv.getHelmet();
		
			case ApronEq.CHEST: return inv.getChestplate();
		
			case ApronEq.LEGS: return inv.getLeggings();
		
			case ApronEq.FEET: return inv.getBoots();
		
			default: return null;
		}
	}
	
	public void setEquipment(PlayerInventory inv, ItemStack eq){
		switch(type){
		
			case ApronEq.HEAD: inv.setHelmet(eq); break;
			
			case ApronEq.CHEST: inv.setChestplate(eq); break;
			
			case ApronEq.LEGS: inv.setLeggings(eq); break;
			
			case ApronEq.FEET: inv.setBoots(eq); break;
		}
	}
}
