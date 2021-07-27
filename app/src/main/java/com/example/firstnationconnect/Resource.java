package com.example.firstnationconnect;

import java.util.ArrayList;

public class Resource {

    private String title, link;

    public Resource(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static ArrayList<Resource> getSuicidePreventionResources() {
        ArrayList<Resource> suicidePreventionResources = new ArrayList<>();
        suicidePreventionResources.add(new Resource("Lifeline Australia","https://www.lifeline.org.au/"));
        return suicidePreventionResources;
    }

    public static ArrayList<Resource> getDepressionResources() {
        ArrayList<Resource> depressionResources = new ArrayList<>();
        depressionResources.add(new Resource("Beyond Blue", "https://www.beyondblue.org.au/"));
        return depressionResources;
    }

    public static ArrayList<Resource> getAnxietyResources() {
        ArrayList<Resource> anxietyResources = new ArrayList<>();
        return anxietyResources;
    }

    public static ArrayList<Resource> getBelongingResources() {
        ArrayList<Resource> belongingResources = new ArrayList<>();
        return belongingResources;
    }

    public static ArrayList<Resource> getPrenatalHealthResources() {
        ArrayList<Resource> prenatalHealthResources = new ArrayList<>();
        return prenatalHealthResources;
    }

    public static ArrayList<Resource> getBullyingResources() {
        ArrayList<Resource> bullyingResources = new ArrayList<>();
        return bullyingResources;
    }
}
