package com.example.firstnationconnect;

import java.util.ArrayList;

public class Resource {

    private String title, link;
    private int resourcePic;

    public Resource(String title, String link, int resourcePic) {
        this.title = title;
        this.link = link;
        this.resourcePic = resourcePic;
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

    public int getResourcePic() {
        return resourcePic;
    }

    public void setResourcePic(int resourcePic) {
        this.resourcePic = resourcePic;
    }

    public static ArrayList<Resource> getSuicidePreventionResources() {
        ArrayList<Resource> suicidePreventionResources = new ArrayList<>();
        suicidePreventionResources.add(new Resource("Lifeline Australia","https://www.lifeline.org.au/", R.drawable.lifeline));
        suicidePreventionResources.add(new Resource("Beyond Blue","https://www.beyondblue.org.au/the-facts/suicide-prevention", R.drawable.beyondblue));
        suicidePreventionResources.add(new Resource("Suicide Callback Service","https://www.suicidecallbackservice.org.au/", R.drawable.suicidecallbackservice));
        suicidePreventionResources.add(new Resource("Suicide Prevention Australia","https://www.suicidepreventionaust.org/", R.drawable.suicidepreventionaustralia));
        return suicidePreventionResources;
    }

    public static ArrayList<Resource> getDepressionResources() {
        ArrayList<Resource> depressionResources = new ArrayList<>();
        depressionResources.add(new Resource("Healthline", "https://www.healthline.com/health/depression", R.drawable.healthline));
        depressionResources.add(new Resource("Beyond Blue", "https://www.beyondblue.org.au/the-facts/depression/treatments-for-depression", R.drawable.beyondblue));
        return depressionResources;
    }

    public static ArrayList<Resource> getAnxietyResources() {
        ArrayList<Resource> anxietyResources = new ArrayList<>();
        anxietyResources.add(new Resource("Better Health", "https://www.betterhealth.vic.gov.au/health/conditionsandtreatments/anxiety-treatment-options", R.drawable.betterhealth));
        anxietyResources.add(new Resource("Beyond Blue", "https://www.beyondblue.org.au/the-facts/anxiety/treatments-for-anxiety", R.drawable.beyondblue));
        return anxietyResources;
    }

    public static ArrayList<Resource> getBelongingResources() {
        ArrayList<Resource> belongingResources = new ArrayList<>();
        belongingResources.add(new Resource("Beyond Blue", "https://www.beyondblue.org.au/who-does-it-affect/aboriginal-and-torres-strait-islander-people/risk-factors", R.drawable.beyondblue));
        belongingResources.add(new Resource("Australians Together", "https://australianstogether.org.au/discover/indigenous-culture/the-importance-of-land/", R.drawable.australianstogether));
        return belongingResources;
    }

    public static ArrayList<Resource> getPerinatalHealthResources() {
        ArrayList<Resource> perinatalHealthResources = new ArrayList<>();
        perinatalHealthResources.add(new Resource("Beyond Blue", "https://healthyfamilies.beyondblue.org.au/pregnancy-and-new-parents", R.drawable.beyondblue));
        perinatalHealthResources.add(new Resource("Women's & Newborn Health", "https://www.wslhd.health.nsw.gov.au/WNH/Support-services/Perinatal-Mental-Health", R.drawable.womensnewborn));
        perinatalHealthResources.add(new Resource("Panda", "https://www.panda.org.au/info-support/whos-who-in-perinatal-mental-health", R.drawable.panda));
        return perinatalHealthResources;
    }

    public static ArrayList<Resource> getBullyingResources() {
        ArrayList<Resource> bullyingResources = new ArrayList<>();
        bullyingResources.add(new Resource("Beyond Blue", "https://healthyfamilies.beyondblue.org.au/age-13/raising-resilient-young-people/bullying-and-cyberbullying", R.drawable.beyondblue));
        bullyingResources.add(new Resource("Australian Human Rights Commission", "https://humanrights.gov.au/our-work/commission-general/what-bullying-violence-harassment-and-bullying-fact-sheet", R.drawable.australianhumanrightscommission));
        return bullyingResources;
    }
}
