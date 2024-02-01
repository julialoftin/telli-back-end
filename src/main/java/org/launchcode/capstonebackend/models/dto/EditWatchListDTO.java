package org.launchcode.capstonebackend.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditWatchListDTO {

    @NotNull
    @Size(min = 1, max = 50, message = "Watch List name must be between 1 and 50 characters.")
    private String newName;
    @NotNull
    @Size(min = 3, max = 200, message = "Watch List description must be between 3 and 200 characters.")
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
