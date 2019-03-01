package com.example.whatsupcolombo;

public class ImageUploadInfo {

    String title, description, image, search, location;

    public ImageUploadInfo() {
    }


    public ImageUploadInfo(String title, String description, String image, String search, String location) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.search = search;
        this.location = location;
    }


    public String getTitle() {
        return title;
    }
    public String getLocation() {
        return location;
    }


    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getSearch() {
        return search;
    }
}
