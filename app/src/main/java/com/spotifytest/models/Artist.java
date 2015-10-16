package com.spotifytest.models;

import java.io.Serializable;

/**
 * Created by Camilo on 15/10/2015.
 */
public class Artist implements Serializable {

    private static final long serialVersionUID = -7440869126539136416L;

    private String id;
    private String image;
    private String name;
    private int followers;
    private int popularity;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
