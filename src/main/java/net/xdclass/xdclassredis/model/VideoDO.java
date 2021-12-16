package net.xdclass.xdclassredis.model;

import java.io.Serializable;

public class VideoDO{

    private int id;

    private String title;

    private String img;

    private int weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public VideoDO() {
    }

    public VideoDO(int id, String title, String img, int weight) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "VideoDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", weight=" + weight +
                '}';
    }
}
