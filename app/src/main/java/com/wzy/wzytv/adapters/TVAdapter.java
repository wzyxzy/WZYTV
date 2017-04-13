package com.wzy.wzytv.adapters;

import android.content.Context;
import android.widget.TextView;

import com.wzy.wzytv.R;
import com.wzy.wzytv.model.TVModel;

import java.util.List;

/**
 * Created by zy on 2016/4/9.
 */
public class TVAdapter extends TeachBaseAdapter<TVModel.TvListEntity> {
    public TVAdapter(List<TVModel.TvListEntity> data, Context context, int layoutRes) {
        super(data, context, layoutRes);
    }

    @Override
    public void bindData(ViewHolder holder, TVModel.TvListEntity tvListEntity) {
        TextView title = (TextView)holder.getView(R.id.tv);
        title.setText(tvListEntity.getTitle());
    }
}
