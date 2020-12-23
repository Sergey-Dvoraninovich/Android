package com.example.game1app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FieldAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int layout;
    private Context _context;
    private ArrayList<String> images;

    FieldAdapter(Context context, int resource, ArrayList<String> fields) {
        super(context, resource, fields);
        this._context = context;
        this.images = fields;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String val = images.get(pos);
        if (val.equals("blank"))
            viewHolder.image.setImageResource(R.mipmap.blank_field_foreground);
        if (val.equals("ship"))
            viewHolder.image.setImageResource(R.mipmap.ship_field_foreground);
        if (val.equals("used"))
            viewHolder.image.setImageResource(R.mipmap.used_field_foreground);
        if (val.equals("shooted"))
            viewHolder.image.setImageResource(R.mipmap.fire_field_foreground);


        return (convertView);
    }

    private static class ViewHolder {
        final ImageView image;
        ViewHolder(View view){
            image = (ImageView) view.findViewById(R.id.field_item_image);
        }
    }


}
