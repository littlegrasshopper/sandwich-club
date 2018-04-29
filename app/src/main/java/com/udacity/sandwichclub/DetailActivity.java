package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String NEWLINE = "\n";

    /* ButterKnife binding */
    @BindView(R.id.name_tv) TextView tvName;
    @BindView(R.id.description_tv) TextView tvDescription;
    @BindView(R.id.origin_tv) TextView tvPlaceOfOrigin;
    @BindView(R.id.also_known_tv) TextView tvAlsoKnownAs;
    @BindView(R.id.ingredients_tv) TextView tvIngredients;
    @BindView(R.id.also_known_label_tv) TextView tvAlsoKnownAsLabel;
    @BindView(R.id.origin_label_tv) TextView tvPlaceOfOriginLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates the data into the views
     * @param sandwich Sandwich data
     */
    private void populateUI(Sandwich sandwich) {
        /* name */
        tvName.setText(sandwich.getMainName());

        /* description */
        tvDescription.setText(sandwich.getDescription());

        /* ingredients */
        List<String> ingredientsArray = sandwich.getIngredients();
        if (ingredientsArray != null && !ingredientsArray.isEmpty()) {
            /* SpannableStringBuilder allows for adding newline and styling for the bullet item */
            SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
            for (int i = 0; i < ingredientsArray.size(); i++) {
                String singleIngredient = ingredientsArray.get(i);
                SpannableString spannableString = new SpannableString (
                        singleIngredient + (i < ingredientsArray.size() - 1 ? NEWLINE : "")
                );
                spannableString.setSpan(
                        new BulletSpan(BulletSpan.STANDARD_GAP_WIDTH),
                        0,
                        spannableString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                spannableBuilder.append(spannableString);
            }
            tvIngredients.setText(spannableBuilder);
        }

        /* place of origin */
        String origin = sandwich.getPlaceOfOrigin();
        if (origin != null && !origin.isEmpty()) {
            tvPlaceOfOrigin.setText(origin);
            tvPlaceOfOrigin.setVisibility(View.VISIBLE);
            tvPlaceOfOriginLabel.setVisibility(View.VISIBLE);
        }

        /* also known as */
        List<String> aliases = sandwich.getAlsoKnownAs();
        if (aliases != null && !aliases.isEmpty()) {
            tvAlsoKnownAs.setText(TextUtils.join(", ", aliases));
            tvAlsoKnownAs.setVisibility(View.VISIBLE);
            tvAlsoKnownAsLabel.setVisibility(View.VISIBLE);
        }
    }
}
