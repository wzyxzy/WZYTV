package com.wzy.sandezi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2016/3/15.
 */
public abstract class WZYBaseAdapter<T> extends BaseAdapter {
    private List<T> data;
    //布局导入器
    private LayoutInflater inflater;
    private Context context;
    //布局id
    private int layoutRes;
    private int color_type;

    public void setColor_type(int color_type) {
        this.color_type = color_type;
    }


    public WZYBaseAdapter(List<T> data, Context context, int layoutRes) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.layoutRes = layoutRes;
        if (data == null) {
            //如果传递进来的数据源是null,我们就实例化一个size为0的数据源
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
    }


    /**
     * 更新数据源(传递进来的数据源不为null,并且size大于0)
     *
     * @param data
     */
    public void updateRes(List<T> data) {
        if (data != null && data.size() > 0) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        this.data.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据源
     *
     * @param data
     */
    public void addRes(List<T> data) {
        if (data != null && data.size() > 0) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutRes, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //数据加载
        bindData(holder, getItem(position), position);
        switch (color_type) {
            case 0:
//                if (position % 2 == 0) {
//                    convertView.setBackgroundColor(context.getResources().getColor(R.color.pink_boys));
//                } else {
//                    convertView.setBackgroundColor(context.getResources().getColor(R.color.pink_girls));
//                }
                switch (position) {
                    case 0:
                        convertView.setBackgroundColor(Color.parseColor("#4493d0"));
                        break;
                    case 1:
                        convertView.setBackgroundColor(Color.parseColor("#00b3c1"));

                        break;
                    case 2:
                        convertView.setBackgroundColor(Color.parseColor("#bc2e9f"));

                        break;
                    case 3:
                        convertView.setBackgroundColor(Color.parseColor("#f75561"));

                        break;
                    case 4:
                        convertView.setBackgroundColor(Color.parseColor("#4493d0"));

                        break;
                    case 5:
                        convertView.setBackgroundColor(Color.parseColor("#77a2e5"));

                        break;
                    case 6:
                        convertView.setBackgroundColor(Color.parseColor("#5a66d6"));

                        break;
                    case 7:
                        convertView.setBackgroundColor(Color.parseColor("#00b98a"));

                        break;
                    case 8:
                        convertView.setBackgroundColor(Color.parseColor("#f39b3d"));

                        break;

                }
                break;
            case 1:

                break;
            case 2:
                break;

        }


        return convertView;

    }

    public abstract void bindData(ViewHolder holder, T t, int indexPostion);

    protected static class ViewHolder {
        //convertView的引用
        private View layout;
        //定义一个Map用来缓存View
        private Map<Integer, View> cacheView;

        //通过构造将convertView传递过来
        public ViewHolder(View convertView) {
            layout = convertView;
            cacheView = new HashMap<>();
        }

        //对外提供获取convertView中子View的方法
        public View getView(int resId) {
            View view = null;
            //如果Map中包含当前获取的资源ID
            if (cacheView.containsKey(resId)) {
                //直接从Map中根据key,获取我们所需的View
                view = cacheView.get(resId);
            } else {
                //不包含的话,我们实例化id所对应的view,并添加到map缓存中
                view = layout.findViewById(resId);
                cacheView.put(resId, view);
            }

            return view;
        }
    }


}
