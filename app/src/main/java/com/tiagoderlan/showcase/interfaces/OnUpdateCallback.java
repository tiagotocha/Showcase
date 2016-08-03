package com.tiagoderlan.showcase.interfaces;

import com.tiagoderlan.showcase.tasks.enums.GetDataTaskResult;

/**
 * Created by Tiago on 01/08/2016.
 */
public interface OnUpdateCallback {

    void updated(GetDataTaskResult result);
}
