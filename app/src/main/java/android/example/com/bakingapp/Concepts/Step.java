package android.example.com.bakingapp.Concepts;

/**
 * Created by florinel on 07.03.2018.
 */

public class Step {
    public final int id;
    public final String shortDescription;
    public final String description;
    public final String videoUrl;
    public final String thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
