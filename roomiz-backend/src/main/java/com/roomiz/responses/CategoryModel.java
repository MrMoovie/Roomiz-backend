package com.roomiz.responses;
import com.roomiz.entities.CategoryEntity;

public class CategoryModel {
    private String name;
    private int id;
    public CategoryModel(CategoryEntity categoryEntity) {
        this.name = categoryEntity.getName();
        this.id = categoryEntity.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
