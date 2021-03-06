package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to process sandwich JSON data.
 */
public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    /* Sandwich JSON data keys */
    private static final String NAME = "name";
    private static final String MAINNAME = "mainName";
    private static final String ALIAS = "alsoKnownAs";
    private static final String DESC = "description";
    private static final String ORIGIN = "placeOfOrigin";
    private static final String PIC = "image";
    private static final String INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        Log.d(TAG, "Received JSON : " + json);
        Sandwich sandwich = new Sandwich();
        JSONObject sandwichJson;
        try {

            sandwichJson = new JSONObject(json);

            /* image */
            sandwich.setImage(sandwichJson.optString(PIC));

            /* name */
            JSONObject namesJson = sandwichJson.optJSONObject(NAME);
            sandwich.setMainName(namesJson.optString(MAINNAME));

            /* origin */
            sandwich.setPlaceOfOrigin(sandwichJson.optString(ORIGIN));

            /* description */
            sandwich.setDescription(sandwichJson.optString(DESC));

            /* list of ingredients */
            JSONArray ingredientsArray = sandwichJson.optJSONArray(INGREDIENTS);
            sandwich.setIngredients(convertArrayToList(ingredientsArray));

            /* aliases */
            JSONArray aliasesArray = namesJson.optJSONArray(ALIAS);
            sandwich.setAlsoKnownAs(convertArrayToList(aliasesArray));

        } catch (JSONException e) {
            Log.d(TAG, "Caught exception in creating JSON Object");
        }

        return sandwich;
    }

    private static List<String> convertArrayToList (JSONArray array) throws JSONException {
        List<String> list = new ArrayList<>();
        try {
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getString(i));
                }
            }
        } catch (JSONException e) {
            throw new JSONException("Caught exception in converting array to list");
        }
        return list;
    }
}
