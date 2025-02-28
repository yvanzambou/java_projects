package model;

import java.io.Serial;
import java.io.Serializable;

public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int categoryId;
    private String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
