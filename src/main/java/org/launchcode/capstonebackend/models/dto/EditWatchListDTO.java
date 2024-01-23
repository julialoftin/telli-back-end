package org.launchcode.capstonebackend.models.dto;

public class EditWatchListDTO {

    private String newName;
    private String newDescription;

    EditWatchListDTO() {}

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
}
