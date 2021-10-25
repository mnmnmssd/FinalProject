package com.example.blog_system.entity;

import java.io.Serializable;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
public class Tag implements Serializable {
    private String text;
    private int weight;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
