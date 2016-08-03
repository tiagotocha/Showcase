package com.tiagoderlan.showcase.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.adapters.SimilarProductAdapter;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.ProductCollection;
import com.tiagoderlan.showcase.tasks.ImageLoadTask;
import com.tiagoderlan.showcase.utils.FormatUtils;

/**
 * Created by Pichau on 28/07/2016.
 */
public class ProductDetailActivity extends AppCompatActivity {

    private ImageView product_image;

    private TextView product_value;

    private TextView product_name;

    private ImageView product_favorite;

    private TextView product_description;

    private TextView product_category;

    private ViewPager product_similar;

    private View product_similar_panel;

    private SimilarProductAdapter adapter;

    private Product model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_detail_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        product_image = (ImageView)findViewById(R.id.product_detail_image);
        product_value = (TextView)findViewById(R.id.product_detail_value);
        product_name = (TextView)findViewById(R.id.product_detail_name);
        product_favorite = (ImageView)findViewById(R.id.product_detail_favorite);
        product_description = (TextView)findViewById(R.id.product_detail_description);
        product_category = (TextView)findViewById(R.id.product_detail_category);
        product_similar = (ViewPager)findViewById(R.id.product_detail_similar);
        product_similar_panel = findViewById(R.id.product_detail_similar_panel);

        int idproduct = getIntent().getExtras().getInt("idproduct");

        model = DatabaseHelper.getInstance(this).getProduct(idproduct);

        loadModel();
    }


    private void loadModel()
    {
        Content content = DatabaseHelper.getInstance(this).getContent(model.photo);

        if(content == null)
        {
            ImageLoadTask task = new ImageLoadTask(this, product_image);

            task.execute(model.photo);
        }
        else
            product_image.setImageBitmap(content.getImageBitmap());

        product_value.setText(FormatUtils.formatCurrency(model.price));

        product_name.setText(model.name);

        product_favorite.setImageResource(model.favorite ? R.drawable.star : R.drawable.starc);

        product_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                model.favorite = !model.favorite;

                DatabaseHelper.getInstance(ProductDetailActivity.this).updateProduct(model);

                product_favorite.setImageResource(model.favorite ? R.drawable.star : R.drawable.starc);
            }
        });


        product_description.setText(model.description);

        Category category = DatabaseHelper.getInstance(this).getCategory(model.category_id);

        if(category != null) {
            product_category.setText(category.name);
            loadSimilar(category);
        }
        else {
            product_similar_panel.setVisibility(View.GONE);
            product_category.setText("Produto sem categoria");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void loadSimilar(Category category)
    {
        ProductCollection similars = DatabaseHelper.getInstance(this).getProductsByCategory(category);

        adapter = new SimilarProductAdapter(getSupportFragmentManager(), similars, model);

        product_similar.setAdapter(adapter);


    }

    public void setProduct(Product product)
    {
        this.model = product;
    }
}

