package com.spotifytest.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Camilo on 15/10/2015.
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 5374567607073678609L;

    private String image;
    private String name;
    private String countries;

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

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }
}
