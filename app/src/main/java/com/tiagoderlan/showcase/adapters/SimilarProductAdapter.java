package com.tiagoderlan.showcase.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tiagoderlan.showcase.fragments.SimilarProductFragment;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.ProductCollection;

import java.util.ArrayList;
import java.util.List;

public class SimilarProductAdapter extends FragmentPagerAdapter {
    private List<Fragment> frags;

    private ProductCollection model;

    public SimilarProductAdapter(FragmentManager fm, ProductCollection model, Product reference) {
        super(fm);

        this.model = model;


        frags = new ArrayList<>();

        for (Product p : model)
        {
            if(p.id != reference.id) {
                SimilarProductFragment frag = new SimilarProductFragment();

                frag.setProduct(p);

                frags.add(frag);
            }
        }
    }


    @Override
    public Fragment getItem(int index) {
        return frags.get(index);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
}