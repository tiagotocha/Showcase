package com.tiagoderlan.showcase.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.tiagoderlan.showcase.api.StaticApi;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.interfaces.OnUpdateCallback;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;
import com.tiagoderlan.showcase.models.collections.ProductCollection;
import com.tiagoderlan.showcase.tasks.enums.GetDataTaskProgress;
import com.tiagoderlan.showcase.tasks.enums.GetDataTaskResult;
import com.tiagoderlan.showcase.utils.Connection;

/**
 * Created by Tiago on 28/07/2016.
 */
public class GetDataAsyncTask extends AsyncTask<Void, GetDataTaskProgress, GetDataTaskResult>
{
    private ProductCollection products;

    private CategoryCollection categories;

    private Context context;

    private OnUpdateCallback callback;

    public GetDataAsyncTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected GetDataTaskResult doInBackground(Void... params)
    {
        try
        {

            if(!Connection.isOnline(context))
                return GetDataTaskResult.Connection;

            DatabaseHelper.getInstance(this.context).clearTables();

            products = StaticApi.getProductCollection();

            categories = StaticApi.getCategoryCollection();

            for(Category c : categories)
                DatabaseHelper.getInstance(this.context).addCategory(c);

            for(Product p : products)
                DatabaseHelper.getInstance(this.context).addProduct(p);


        }
        catch (Exception e)
        {
            Log.e("GetDataTaskResult", e.getLocalizedMessage());

            return GetDataTaskResult.Failed;
        }

        return GetDataTaskResult.Ok;
    }

    @Override
    protected void onPostExecute(GetDataTaskResult result) {
        super.onPostExecute(result);


        if(callback != null)
            callback.updated(result);
    }

    public void setUpdateCallback(OnUpdateCallback callback)
    {
        this.callback = callback;
    }


}
