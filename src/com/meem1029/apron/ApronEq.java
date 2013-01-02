package com.meem1029.apron;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ApronEq{
	public static int LEATHER = 298;
	public static int CHAIN = 302;
	public static int IRON = 306;
	public static int DIAMOND = 310;
	public static int GOLD = 314;
	
	public static final int HEAD = 0;
	public static final int CHEST = 1;
	public static final int LEGS = 2;
	public static final int FEET = 3;
	
	private Slot slot;
	private ItemStack eq;
	
	public ApronEq(int mat, int slot){
		Slot newSlot = new Slot(slot);
		this.slot = newSlot;
		this.eq = new ItemStack(mat + slot,1);
	}
	
	public ApronEq(ItemStack material, int slot){
		this.slot = new Slot(slot);
		this.eq = material;
	}
	
	public boolean checkEquipment(Player player){
		return slot.getEquipment(player.getInventory()) == eq;
	}
	
	public void setEquipment(Player player){
		slot.setEquipment(player.getInventory(), eq);
	}
	
	public void manageEquipment(Player player){
		if(!this.checkEquipment(player)){
			this.setEquipment(player);
			player.updateInventory();
		}
	}

}
