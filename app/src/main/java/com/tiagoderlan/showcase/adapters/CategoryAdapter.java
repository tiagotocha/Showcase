package com.tiagoderlan.showcase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 01/08/2016.
 */
public class CategoryAdapter extends ArrayAdapter<Category> implements Filterable
{
    private LayoutInflater inflater;

    private CategoryCollection source;

    private CategoryCollection filtered;

    private CategoryFilter filter = new CategoryFilter();

    public CategoryAdapter(Context context, CategoryCollection categories)
    {
        super(context, R.layout.category_list_item);

        this.inflater = LayoutInflater.from(context);

        this.source = categories;

        this.filtered = new CategoryCollection();
        this.addAll(categories);

        addAll(categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Category category = getItem(position);

        if(category.selected)
            convertView = inflater.inflate(R.layout.category_list_item_selected, null);
        else
            convertView = inflater.inflate(R.layout.category_list_item, null);

        TextView view  = (TextView)convertView.findViewById(R.id.category_list_item_name);

        view.setText(category.name);

        return convertView;
    }

    public void updateSelection(Category newselection)
    {
        for(Category c : source)
            c.selected = false;

        newselection.selected = true;
    }

    public int getCount() {
        return filtered.size();
    }

    public Category getItem(int position) {
        return filtered.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CategoryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if(constraint.length() > 0) {

                String filterString = constraint.toString().toLowerCase();

                final List<Category> list = source;

                int count = list.size();

                final ArrayList<Category> nlist = new ArrayList<Category>(count);

                Category filterItem;

                for (int i = 0; i < count; i++) {
                    filterItem = list.get(i);
                    if (filterItem.name.toLowerCase().contains(filterString)) {
                        nlist.add(filterItem);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();
            }
            else
            {
                results.values = source;
                results.count = source.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = new CategoryCollection();

            filtered.addAll((ArrayList<Category>) results.values);

            notifyDataSetChanged();
        }

    }



}
