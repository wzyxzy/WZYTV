package com.wzy.wzytv.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzy.wzytv.R;
import com.wzy.wzytv.model.TVModel;

import java.util.List;

/**
 * Created by zy on 2016/4/9.
 */
public class TVRAdapter extends BaseQuickAdapter<TVModel.TvListEntity, BaseViewHolder> {
    public TVRAdapter(List<TVModel.TvListEntity> tvListEntity) {
        super(R.layout.item, tvListEntity);
    }

//    @Override
//    public void bindData(ViewHolder holder, TVModel.TvListEntity tvListEntity) {
//        TextView title = (TextView) holder.getView(R.id.tv);
//        title.setText(tvListEntity.getTitle());
//    }

    @Override
    protected void convert(BaseViewHolder helper, TVModel.TvListEntity item) {
        helper.setText(R.id.tv, item.getTitle());
    }
}
