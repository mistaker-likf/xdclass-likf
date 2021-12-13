package net.xdclass.xdclassredis.model;

public class VideoDO {

    private int id;

    private String title;

    private String img;

    private int weight;

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
