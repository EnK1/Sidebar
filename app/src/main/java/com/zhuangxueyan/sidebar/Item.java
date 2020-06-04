package com.zhuangxueyan.sidebar;

public class Item {
    private String itemName;
    private int image;

    public Item(String itemName, int image) {
        this.itemName = itemName;
        this.image = image;
    }

    public Item() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "item{" +
                "itemName='" + itemName + '\'' +
                ", image=" + image +
                '}';
    }
}
