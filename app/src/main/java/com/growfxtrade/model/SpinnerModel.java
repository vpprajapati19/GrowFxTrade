package com.growfxtrade.model;

/**
 * Created by Wiz@rd on 7/20/2017.
 */
public class SpinnerModel {

    private String id;
    private int image;
    private String name;
    private String original;
    private boolean selected;
    private String data;
    private String img_url;

    public SpinnerModel(String id, int image) {
        this.id = id;
        this.image = image;
    }

    public SpinnerModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public SpinnerModel(String id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public SpinnerModel(String id, String data, String name, String original) {
        this.id = id;
        this.data = data;
        this.name = name;
        this.original = original;
    }

    public SpinnerModel(String id, String data, String name) {
        this.id = id;
        this.data = data;
        this.name = name;
    }

    public String getOriginal() {
        return original;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
