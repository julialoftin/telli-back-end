package org.launchcode.capstonebackend.models;

import jakarta.persistence.Entity;

@Entity
public class WatchList extends AbstractEntity {

    private String name;
    private String description;

    public WatchList(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public WatchList() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
