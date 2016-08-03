package com.tiagoderlan.showcase.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.adapters.CategoryAdapter;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.interfaces.OnFilteredCallback;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;

/**
 * Created by Tiago on 01/08/2016.
 */
public class MenuFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener
{
    private ListView categorylist;

    private EditText filtertext;

    private CategoryAdapter adapter;

    private OnFilteredCallback callback;

    private Category category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_fragment, null);

        categorylist = (ListView)view.findViewById(R.id.menu_fragment_list);

        filtertext = (EditText)view.findViewById(R.id.menu_fragment_filter);

        filtertext.addTextChangedListener(this);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        update();
    }

    public void update()
    {
        CategoryCollection categories = DatabaseHelper.getInstance(getContext()).getCategories();

        Category allcategory = new Category();

        allcategory.name = "Todas as categorias";
        allcategory.id = -1;
        allcategory.selected = true;

        category = allcategory;

        categories.add(0, allcategory);

        adapter = new CategoryAdapter(getContext(), categories);

        categorylist.setAdapter(adapter);
        categorylist.setOnItemClickListener(this);

        filtertext.setText("");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void setOnFilteredCallback(OnFilteredCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(callback != null)
        {
            category = adapter.getItem(position);

            adapter.updateSelection(adapter.getItem(position));

            adapter.notifyDataSetChanged();

            callback.onFilterSelect(adapter.getItem(position));
        }
    }

    public Category getCategory()
    {
        return category;
    }
}
