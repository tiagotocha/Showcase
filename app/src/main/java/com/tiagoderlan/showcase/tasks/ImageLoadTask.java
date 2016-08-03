package com.tiagoderlan.showcase.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.tiagoderlan.showcase.api.StaticApi;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.tasks.enums.ImageLoadTaskProgress;
import com.tiagoderlan.showcase.tasks.enums.ImageLoadTaskResult;

/**
 * Created by Tiago on 01/08/2016.
 */
public class ImageLoadTask extends AsyncTask<String, ImageLoadTaskProgress, ImageLoadTaskResult>
{
    private Context context;

    private ImageView target;

    private Content content;


    public ImageLoadTask(Context context, ImageView view)
    {
        this.context = context;
        this.target = view;
    }

    @Override
    protected ImageLoadTaskResult doInBackground(String... params)
    {
        String url = params[0];

        content = DatabaseHelper.getInstance(context).getContent(url);

        if(content == null)
        {
            try
            {
                byte[] data = StaticApi.downloadUrlData(url);

                content = new Content();
                content.url = url;
                content.data = data;

                DatabaseHelper.getInstance(context).addContent(content);

                return ImageLoadTaskResult.Ok;
            }
            catch (Exception e)
            {


            }
        }
        else
        {
            return ImageLoadTaskResult.Ok;
        }

        return ImageLoadTaskResult.Failed;
    }

    @Override
    protected void onPostExecute(ImageLoadTaskResult imageLoadTaskResult) {
        super.onPostExecute(imageLoadTaskResult);

        if(imageLoadTaskResult == ImageLoadTaskResult.Ok)
        {
            target.setImageBitmap(content.getImageBitmap());
        }
    }
}
