package com.tiagoderlan.showcase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.activities.ProductDetailActivity;
import com.tiagoderlan.showcase.adapters.ProductAdapter;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.interfaces.OnUpdateCallback;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.ProductCollection;
import com.tiagoderlan.showcase.tasks.GetDataAsyncTask;
import com.tiagoderlan.showcase.tasks.enums.GetDataTaskResult;

/**
 * Created by Pichau on 28/07/2016.
 */
public class ProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnUpdateCallback, AdapterView.OnItemClickListener
{
    private ListView listView;

    private ProductAdapter adapter;

    private SwipeRefreshLayout swipe;

    private OnUpdateCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.products_fragment, null);

        this.listView = (ListView)view.findViewById(R.id.products_framgment_list);

        this.listView.setOnItemClickListener(this);

        this.swipe = (SwipeRefreshLayout)view.findViewById(R.id.products_framgment_swipe);

        this.swipe.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        loadList();
    }

    public void loadList()
    {
        ProductCollection items = DatabaseHelper.getInstance(getActivity()).getProducts();

        this.adapter = new ProductAdapter(getActivity(), items);

        this.listView.setAdapter(this.adapter);
    }

    @Override
    public void onRefresh()
    {
        GetDataAsyncTask task = new GetDataAsyncTask(getActivity());

        task.setUpdateCallback(this);

        task.execute();
    }

    @Override
    public void updated(GetDataTaskResult result)
    {
        loadList();

        if(callback != null)
            callback.updated(result);

        swipe.setRefreshing(false);
    }

    public void setCategory(Category category)
    {
        adapter.setCategory(category);
    }

    public void setCallback(OnUpdateCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Product product = adapter.getItem(position);

        Intent intent = new Intent(getContext(), ProductDetailActivity.class);

        intent.putExtra("idproduct", product.id);

        getActivity().startActivityForResult(intent, 1);
    }

    public void notifyDataSetChanged()
    {
        if(this.adapter !=null)
            this.adapter.notifyDataSetChanged();
    }
}
