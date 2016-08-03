package com.tiagoderlan.showcase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.activities.ProductDetailActivity;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.tasks.ImageLoadTask;
import com.tiagoderlan.showcase.utils.FormatUtils;

/**
 * Created by Tiago on 02/08/2016.
 */
public class SimilarProductFragment extends Fragment implements View.OnClickListener
{
    private ImageView product_image;

    private TextView product_name;

    private Product model;

    public void setProduct(Product model)
    {
        this.model = model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.similar_card_view, null);

        product_image = (ImageView)view.findViewById(R.id.similar_card_view_image);

        product_name = (TextView)view.findViewById(R.id.similar_card_view_name);

        view.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Content content = DatabaseHelper.getInstance(getContext()).getContent(model.photo);

        if(content == null)
        {
            ImageLoadTask task = new ImageLoadTask(getContext(), product_image);

            task.execute(model.photo);
        }
        else
            product_image.setImageBitmap(content.getImageBitmap());

        product_name.setText(model.name);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);

        intent.putExtra("idproduct", model.id);

        getActivity().startActivityForResult(intent, 1);
    }
}
