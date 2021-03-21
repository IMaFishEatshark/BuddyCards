package com.wildcard.buddycards.container;

import com.wildcard.buddycards.blocks.BuddysteelVaultTile;
import com.wildcard.buddycards.items.CardItem;
import com.wildcard.buddycards.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class VaultContainer extends Container {
    Inventory inv;
    BuddysteelVaultTile tile;

    public VaultContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new BuddysteelVaultTile());
    }
    
    public VaultContainer(int id, PlayerInventory playerInv, BuddysteelVaultTile tileIn) {
        super(RegistryHandler.VAULT_CONTAINER.get(), id);
        tile = tileIn;
        inv = new Inventory(120);
        for (int i = 0; i < 120; i++) {
            inv.setInventorySlotContents(i, tile.inventory.getStackInSlot(i));
        }
        //Set up vault card slots
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 12; x++) {
                this.addSlot(new VaultContainer.CardSlot(inv, x + (y * 12), 8 + x * 18, 18 + y * 18));
            }
        }
        //Set up vault item slots
        for (int x = 0; x < 12; x++) {
            this.addSlot(new Slot(inv, x + 108, 8 + x * 18, 180));
        }
        //Set up slots for inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + (y * 9) + 9, 35 + x * 18, 212 + y * 18));
            }
        }
        //Set up slots for hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 35 + x * 18, 270));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public class CardSlot extends Slot {
        public CardSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let cards go into card slots
        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof CardItem;
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            stack = slot.getStack().copy();
            if (index < 120)
            {
                if(!this.mergeItemStack(slot.getStack(), 120, inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if(!this.mergeItemStack(slot.getStack(), 0, 36, false))
                return ItemStack.EMPTY;
            if(slot.getStack().isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return stack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.inv.closeInventory(playerIn);
    }
}
