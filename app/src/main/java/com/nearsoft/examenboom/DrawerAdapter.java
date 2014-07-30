package com.nearsoft.examenboom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by jsalcido on 7/27/14.
 */
public class DrawerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final String[] items;
    private int size;

    public DrawerAdapter(Context context, String[] itemsArray) {
        items = itemsArray;
        size = itemsArray.length;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_drawer, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.drawer_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setText(holder, position);
        return convertView;
    }

    private void setText(ViewHolder holder, int position) {
        holder.text.setText((String) getItem(position));
    }

    private class ViewHolder {
        TextView text;
    }
}
