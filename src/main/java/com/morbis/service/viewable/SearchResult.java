package com.morbis.service.viewable;

import lombok.Data;

@Data
public class SearchResult {

    private ViewableEntityType type;
    private int id;

    private String description;

    public SearchResult(int id, String description, ViewableEntityType type) {
        this.description = description;
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
