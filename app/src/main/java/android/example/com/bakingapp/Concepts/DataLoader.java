package android.example.com.bakingapp.Concepts;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static android.example.com.bakingapp.RecipeListActivity.setupRecyclerView;

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
    public static final List<Recipe> ITEMS = new ArrayList<Recipe>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<String, Recipe>();

    private static  int COUNT;

    public static void load(final Context context) throws InterruptedException {
        if (!DataIsLoaded) {
            try {
                new AsyncTask<URL, Integer, String>() {
                    boolean isConnected;

                    private ArrayList<Ingredient> makeIngredientsList(JSONArray ingredientsArray) throws JSONException {
                        int i;
                        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                        for (i = 0; i < ingredientsArray.length(); i++) {

                            ingredients.add(new Ingredient(ingredientsArray.getJSONObject(i).getInt("quantity"),
                                    ingredientsArray.getJSONObject(i).getString("ingredient"),
                                    ingredientsArray.getJSONObject(i).getString("measure")
                            ));

                        }
                        return ingredients;
                    }

                    private ArrayList<Step> makeStepList(JSONArray stepsArray) throws JSONException {
                        ArrayList<Step> steps = new ArrayList<Step>();
                        int i;
                        for (i = 0; i < stepsArray.length(); i++) {

                            steps.add(new Step(stepsArray.getJSONObject(i).getInt("id"),
                                    stepsArray.getJSONObject(i).getString("shortDescription"),
                                    stepsArray.getJSONObject(i).getString("description"),
                                    stepsArray.getJSONObject(i).getString("videoURL"),
                                    stepsArray.getJSONObject(i).getString("thumbnailURL")));

                        }
                        return steps;
                    }

                    @Override
                    protected void onPostExecute(String json) {
                        if (json == null)
                            return;
                        JSONArray recipesArray = null;
                        try {
                            recipesArray = new JSONArray(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject recipeObject;
                        DataLoader.COUNT = recipesArray.length();
                        int i;
                        int id;
                        String name;
                        ArrayList<Ingredient> ingredients;
                        ArrayList<Step> steps;
                        int servings;
                        String image;
                        for (i = 0; i < DataLoader.COUNT; i++) {
                            try {
                                recipeObject = recipesArray.getJSONObject(i);

                                id = recipeObject.getInt("id");

                                name = recipeObject.getString("name");
                                ingredients = makeIngredientsList(recipeObject.getJSONArray("ingredients"));
                                steps = makeStepList(recipeObject.getJSONArray("steps"));
                                servings = recipeObject.getInt("servings");
                                image = recipeObject.getString("image");
                                DataLoader.addRecipe(new Recipe(id, name, ingredients, steps, servings, image));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        setupRecyclerView();
                    }


                    @Override
                    protected void onPreExecute() {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                        isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();

                    }

                    @Override
                    protected String doInBackground(URL... urls) {
                        if (!isConnected) {
                            return null;
                        }
                        URL url = urls[0];

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
                        return json;
                    }
                }.execute(new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json"));
            }
            catch (MalformedURLException mue)
            {

            }
        }
        else
        {
            setupRecyclerView();
        }

    }


    private static void addRecipe(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    private static Recipe createRecipe(int position) {
       // return new Recipe(String.valueOf(position), "Item " + position, makeDetails(position));
        return null;
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
    public static class Recipe {
       public final int id;
       public final String name;
       public final List<Ingredient> ingredients;
       public final List<Step> steps;
       public final int servings;
       public final String image;

        public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
            this.id = id;
            this.name = name;
            this.ingredients = ingredients;
            this.steps = steps;
            this.servings = servings;
            this.image = image;
        }
    }
}
