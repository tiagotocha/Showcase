package com.tiagoderlan.showcase.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.ProductCollection;
import com.tiagoderlan.showcase.tasks.ImageLoadTask;
import com.tiagoderlan.showcase.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 28/07/2016.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private LayoutInflater inflater;

    private ProductCollection source;

    private ProductCollection filtered;

    private Category category;

    private ProductFilter filter = new ProductFilter();

    public ProductAdapter(Context context, ProductCollection products) {
        super(context, R.layout.product_list_item);

        this.inflater = LayoutInflater.from(context);

        if(products.size() == 0)
            products.add(null);

        this.source = products;

        this.filtered = new ProductCollection();
        this.filtered.addAll(source);

        addAll(products);
    }

    public int getCount() {
        return filtered.size();
    }

    public Product getItem(int position) {
        return filtered.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final Product item = getItem(position);

        if(item == null)
        {
            convertView = inflater.inflate(R.layout.product_list_item_empty, null);

            return convertView;
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_item, null);

            holder = new ViewHolder();
            holder.product_name = (TextView) convertView.findViewById(R.id.product_list_item_name);
            holder.product_price = (TextView) convertView.findViewById(R.id.product_list_item_price);
            holder.product_image = (ImageView) convertView.findViewById(R.id.product_list_item_image);
            holder.product_favorite = (ImageView) convertView.findViewById(R.id.product_list_item_favorite);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.product_image.setImageBitmap(null);


        holder.product_name.setText(item.name);
        holder.product_price.setText(FormatUtils.formatCurrency(item.price));
        holder.product_favorite.setImageResource(item.favorite ? R.drawable.star : R.drawable.starc);

        final ImageView fav = holder.product_favorite;

        holder.product_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.favorite = !item.favorite;

                DatabaseHelper.getInstance(getContext()).updateProduct(item);

                fav.setImageResource(item.favorite ? R.drawable.star : R.drawable.starc);

            }
        });

        Content content = DatabaseHelper.getInstance(getContext()).getContent(item.photo);

        if (content == null) {
            ImageLoadTask task = new ImageLoadTask(getContext(), holder.product_image);

            task.execute(item.photo);
        } else {
            holder.product_image.setImageBitmap(content.getImageBitmap());
        }

        return convertView;
    }

    public void setCategory(Category category)
    {
        this.category = category;

        filter.filter("");
    }

    private class ProductFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            final List<Product> list = source;

            int count = list.size();

            final ArrayList<Product> nlist = new ArrayList<Product>(count);

            Product filterItem;

            for (int i = 0; i < count; i++) {
                filterItem = list.get(i);

                if(category.id == -1)
                    nlist.add(filterItem);
                else
                if (filterItem.category_id != null)
                    if(filterItem.category_id == category.id)
                        nlist.add(filterItem);

            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = new ProductCollection();

            filtered.addAll((ArrayList<Product>) results.values);

            notifyDataSetChanged();
        }
    }
}

class ViewHolder
{
    public TextView product_name;

    public TextView product_price;

    public ImageView product_image;

    public ImageView product_favorite;
}
