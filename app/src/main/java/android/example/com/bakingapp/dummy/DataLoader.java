package android.example.com.bakingapp.dummy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DataLoader {

    /**
     * An array of sample (dummy) items.
     */
    public static boolean DataIsLoaded=false;
    public static final List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RecipeItem> ITEM_MAP = new HashMap<String, RecipeItem>();

    private static final int COUNT = 25;

    public static void load(final Context context) {
        if (!DataIsLoaded) {
            new Thread(new Runnable() {
                public boolean verifyInternetConnection() {

                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    return isConnected;
                }

                public void proccessJSON(String json) {

                }

                @Override
                public void run() {
                    if(!verifyInternetConnection())
                    {
                        Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();
                        return;
                    }
                    String source = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
                    URL url = null;
                    try {
                        url = new URL(source);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    String json = null;
                    HttpURLConnection urlConnection = null;
                    try {

                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = urlConnection.getInputStream();
                        Scanner scanner = new Scanner(in);
                        scanner.useDelimiter("\\A");
                        boolean hasInput = scanner.hasNext();

                        if (hasInput) {
                            json = scanner.next();
                        } else {
                            json = null;
                        }

                    } catch (IOException e) {
                        urlConnection.disconnect();
                    }
                    DataIsLoaded = (json != null);
                    proccessJSON(json);
                }


            }).start();

        }
    }


    private static void addRecipe(RecipeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static RecipeItem createRecipeItem(int position) {
        return new RecipeItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class RecipeItem {
        public final String id;
        public final String content;
        public final String details;

        public RecipeItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
