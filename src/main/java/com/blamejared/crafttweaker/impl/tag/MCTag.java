package com.blamejared.crafttweaker.impl.tag;

import com.blamejared.crafttweaker.api.*;
import com.blamejared.crafttweaker.api.annotations.*;
import com.blamejared.crafttweaker.api.item.*;
import com.blamejared.crafttweaker.impl.actions.tags.*;
import com.blamejared.crafttweaker.impl.blocks.*;
import com.blamejared.crafttweaker.impl.entity.*;
import com.blamejared.crafttweaker.impl.fluid.*;
import com.blamejared.crafttweaker.impl.helper.*;
import com.blamejared.crafttweaker.impl.item.*;
import com.blamejared.crafttweaker.impl.util.*;
import com.blamejared.crafttweaker_annotations.annotations.*;
import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import org.openzen.zencode.java.*;

import java.util.*;


@ZenRegister
@ZenCodeType.Name("crafttweaker.api.tag.MCTag")
@Document("vanilla/api/tags/MCTag")
@ZenWrapper(wrappedClass = "net.minecraft.tags.Tag", conversionMethodFormat = "%s.getInternal()", displayStringFormat = "%s.getCommandString()")
public class MCTag implements IIngredient {
    
    private final ResourceLocation id;
    private ITag<Item> itemTag;
    private ITag<Block> blockTag;
    private ITag<EntityType<?>> entityTypeTag;
    private ITag<Fluid> fluidTag;
    
    public MCTag(ResourceLocation id) {
        this.id = id;
    }
    
    @ZenCodeType.Method
    public MCTag createItemTag() {
        CraftTweakerAPI.apply(new ActionTagCreate<>(TagCollectionManager.getManager()
                .getItemTags(), "Item", Tag.getTagFromContents(Sets.newHashSet()), id));
        return this;
    }
    
    @ZenCodeType.Method
    public MCTag createBlockTag() {
        CraftTweakerAPI.apply(new ActionTagCreate<>(TagCollectionManager.getManager()
                .getBlockTags(), "Block", Tag.getTagFromContents(Sets.newHashSet()), id));
        return this;
    }
    
    @ZenCodeType.Method
    public MCTag createFluidTag() {
        CraftTweakerAPI.apply(new ActionTagCreate<>(TagCollectionManager.getManager()
                .getFluidTags(), "Fluid", Tag.getTagFromContents(Sets.newHashSet()), id));
        return this;
    }
    
    @ZenCodeType.Method
    public MCTag createEntityTypeTag() {
        CraftTweakerAPI.apply(new ActionTagCreate<>(TagCollectionManager.getManager()
                .getEntityTypeTags(), "EntityType", Tag.getTagFromContents(Sets.newHashSet()), id));
        return this;
    }
    
    @Override
    public IItemStack[] getItems() {
        if(getItemTag() == null) {
            CraftTweakerAPI.logError("\"" + getCommandString() + "\" is not an ItemTag!");
            return new IItemStack[0];
        }
        
        List<IItemStack> returned = new ArrayList<>();
        for(Item element : itemTag.getAllElements()) {
            returned.add(new MCItemStack(new ItemStack(element)));
        }
        return returned.toArray(new IItemStack[0]);
    }
    
    @ZenCodeType.Getter("blocks")
    public MCBlock[] getBlocks() {
        if(getBlockTag() == null) {
            CraftTweakerAPI.logError("\"" + getCommandString() + "\" is not a BlockTag!");
            return new MCBlock[0];
        }
        
        List<MCBlock> returned = new ArrayList<>();
        for(Block element : blockTag.getAllElements()) {
            returned.add(new MCBlock(element));
        }
        return returned.toArray(new MCBlock[0]);
    }
    
    @ZenCodeType.Getter("fluids")
    public MCFluid[] getFluids() {
        if(getFluidTag() == null) {
            CraftTweakerAPI.logError("\"" + getCommandString() + "\" is not a FluidTag!");
            return new MCFluid[0];
        }
        return getFluidTag().getAllElements().stream().map(MCFluid::new).toArray(MCFluid[]::new);
    }
    
    @ZenCodeType.Getter("entityTypes")
    public MCEntityType[] getEntityTypes() {
        if(getEntityTypeTag() == null) {
            CraftTweakerAPI.logError("\"" + getCommandString() + "\" is not an EntityTypeTag!");
            return new MCEntityType[0];
        }
        
        List<MCEntityType> returned = new ArrayList<>();
        for(EntityType<?> element : entityTypeTag.getAllElements()) {
            returned.add(new MCEntityType(element));
        }
        return returned.toArray(new MCEntityType[0]);
    }
    
    @ZenCodeType.Getter("firstItem")
    public IItemStack getFirstItem() {
        if(getItemTag() == null) {
            throw new IllegalArgumentException("\"" + getCommandString() + "\" is not an ItemTag!");
        }
        return getItems()[0];
    }
    
    @ZenCodeType.Getter("firstBlock")
    public MCBlock getFirstBlock() {
        if(getBlockTag() == null) {
            throw new IllegalArgumentException("\"" + getCommandString() + "\" is not a BlockTag!");
        }
        return getBlocks()[0];
    }
    
    @ZenCodeType.Getter("firstFluid")
    public MCFluid getFirstFluid() {
        if(getFluidTag() == null) {
            throw new IllegalArgumentException("\"" + getCommandString() + "\" is not a FluidTag!");
        }
        return getFluids()[0];
    }
    
    @ZenCodeType.Getter("firstEntityType")
    public MCEntityType getFirstEntityType() {
        if(getEntityTypeTag() == null) {
            throw new IllegalArgumentException("\"" + getCommandString() + "\" is not an EntityTypeTag!");
        }
        return getEntityTypes()[0];
    }
    
    /**
     * Adds items to this tag, will fail if this is not a tag that can hold items
     *
     * @param items Items to add to the tag
     *
     * @docParam items <item:minecraft:dirt>
     */
    @ZenCodeType.Method
    public void addItems(IItemStack... items) {
        CraftTweakerAPI.apply(new ActionTagAdd<Item>(getItemTag(), CraftTweakerHelper.getItems(items), id));
    }
    
    /**
     * removes items from this tag, will fail if this is not a tag that can hold items
     *
     * @param items Items to remove from the tag
     *
     * @docParam items <item:minecraft:dirt>
     */
    @ZenCodeType.Method
    public void removeItems(IItemStack... items) {
        CraftTweakerAPI.apply(new ActionTagRemove<>(getItemTag(), CraftTweakerHelper.getItems(items), id));
    }
    
    
    @ZenCodeType.Method
    public void addBlocks(MCBlock... blocks) {
        CraftTweakerAPI.apply(new ActionTagAdd<>(getBlockTag(), CraftTweakerHelper.getBlocks(blocks), id));
    }
    
    @ZenCodeType.Method
    public void removeBlocks(MCBlock... blocks) {
        CraftTweakerAPI.apply(new ActionTagRemove<>(getBlockTag(), CraftTweakerHelper.getBlocks(blocks), id));
    }
    
    @ZenCodeType.Method
    public void addFluids(MCFluid... fluids) {
        CraftTweakerAPI.apply(new ActionTagAdd<>(getFluidTag(), CraftTweakerHelper.getFluids(fluids), id));
    }
    
    @ZenCodeType.Method
    public void removeFluids(MCFluid... fluids) {
        CraftTweakerAPI.apply(new ActionTagRemove<>(getFluidTag(), CraftTweakerHelper.getFluids(fluids), id));
    }
    
    @ZenCodeType.Method
    public void addEntityTypes(MCEntityType... entities) {
        CraftTweakerAPI.apply(new ActionTagAdd<>(getEntityTypeTag(), CraftTweakerHelper.getEntityTypes(entities), id));
    }
    
    @ZenCodeType.Method
    public void removeEntityTypes(MCEntityType... entities) {
        CraftTweakerAPI.apply(new ActionTagRemove<>(getEntityTypeTag(), CraftTweakerHelper.getEntityTypes(entities), id));
    }
    
    @Override
    public boolean matches(IItemStack stack, boolean ignoreDamage) {
        if(!stack.isEmpty()) {
            for(IItemStack item : getItems()) {
                if(item.matches(stack, ignoreDamage)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @ZenCodeType.Getter("isItemTag")
    public boolean isItemTag() {
        return getItemTag() != null;
    }
    
    @ZenCodeType.Getter("isBlockTag")
    public boolean isBlockTag() {
        return getBlockTag() != null;
    }
    
    @ZenCodeType.Getter("isFluidTag")
    public boolean isFluidTag() {
        return getFluidTag() != null;
    }
    
    @ZenCodeType.Getter("isEntityTypeTag")
    public boolean isEntityTypeTag() {
        return getEntityTypeTag() != null;
    }
    
    
    @Override
    public Ingredient asVanillaIngredient() {
        return Ingredient.fromTag(getItemTag());
    }
    
    public ITag<Item> getItemTag() {
        if(itemTag == null) {
            itemTag = TagCollectionManager.getManager().getItemTags().get(id);
        }
        return itemTag;
    }
    
    public ITag<Block> getBlockTag() {
        if(blockTag == null) {
            blockTag = TagCollectionManager.getManager().getBlockTags().get(id);
        }
        return blockTag;
    }
    
    public ITag<Fluid> getFluidTag() {
        if(fluidTag == null) {
            fluidTag = TagCollectionManager.getManager().getFluidTags().get(id);
        }
        return fluidTag;
    }
    
    public ITag<EntityType<?>> getEntityTypeTag() {
        if(entityTypeTag == null) {
            entityTypeTag = TagCollectionManager.getManager().getEntityTypeTags().get(id);
        }
        return entityTypeTag;
    }
    
    @ZenCodeType.Getter("id")
    public MCResourceLocation getId() {
        return new MCResourceLocation(id);
    }
    
    @Override
    public String getCommandString() {
        return String.format("<tag:%s>", this.id);
    }
    
    @Override
    public String toString() {
        return getCommandString();
    }
    
    
    public ResourceLocation getInternalID(){
        return this.id;
    }
}
