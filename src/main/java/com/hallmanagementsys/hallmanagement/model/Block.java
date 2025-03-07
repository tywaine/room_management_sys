package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.BlockDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Block {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Character> name = new SimpleObjectProperty<>();
    private final IntegerProperty maxRooms = new SimpleIntegerProperty();

    private static final Map<Integer, Block> blocks = new HashMap<>();
    private static final ObservableList<Block> blockList = FXCollections.observableArrayList();

    public Block(Character name, Integer maxRooms) {
        this.name.set(name);
        this.maxRooms.set(maxRooms);
    }

    public Block(Integer id, Character name, Integer maxRooms) {
        this.id.set(id);
        this.name.set(name);
        this.maxRooms.set(maxRooms);

        addBlock(this);
    }

    // Getters
    public int getID() {
        return id.get();
    }

    public Character getName() {
        return name.get();
    }

    public int getMaxRooms() {
        return maxRooms.get();
    }

    // Setters
    public void setID(int id) {
        this.id.set(id);
    }

    public void setName(Character name) {
        this.name.set(name);
    }

    public void setMaxRooms(int maxRooms) {
        this.maxRooms.set(maxRooms);
    }

    // Property methods
    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Character> nameProperty() {
        return name;
    }

    public IntegerProperty maxRoomsProperty() {
        return maxRooms;
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + getID() +
                ", name=" + getName() +
                ", maxRooms=" + getMaxRooms() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        Block block = (Block) obj;
        return Objects.equals(getID(), block.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }

    // Convert Block -> BlockDTO
    public BlockDTO toDTO() {
        if(getID() == 0) {
            return new BlockDTO(null, getName(), getMaxRooms());
        }

        return new BlockDTO(getID(), getName(), getMaxRooms());
    }

    // Convert BlockDTO -> Block
    public static Block fromDTO(BlockDTO dto) {
        return new Block(dto.getId(), dto.getName(), dto.getMaxRooms());
    }

    public static void addBlock(Block block) {
        if(block == null){
            System.out.println("Block is null. Was not added to Map and List");
        }
        else{
            if(!containsBlock(block.getID())){
                blocks.put(block.getID(), block);
                blockList.add(block);
            }
            else{
                System.out.println("Block is already present!");
            }
        }
    }

    public static void update(Block block, Character name, Integer maxRooms) {
        if(block != null){
            block.setName(name);
            block.setMaxRooms(maxRooms);
        }
        else{
            System.out.println("Block value is null");
        }
    }

    public static void removeBlock(Block block) {
        if(isValidBlock(block)){
            blockList.remove(block);
            blocks.remove(block.getID());
        }
        else{
            System.out.println("Block ID not found");
        }
    }

    public static Block getBlock(Integer blockID) {
        if(containsBlock(blockID)){
            return blocks.get(blockID);
        }
        else{
            System.out.println("Block ID not found. Null was returned");
            return null;
        }
    }

    public static boolean isValidBlock(Block block) {
        return block != null && containsBlock(block.getID());
    }

    public static boolean containsBlock(Integer blockID) {
        return blocks.containsKey(blockID);
    }

    public static ObservableList<Block> getList(){
        return blockList;
    }

    public static void emptyBlocks() {
        blocks.clear();
        blockList.clear();
    }
}
