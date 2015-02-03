package ru.perm.trubnikov.chayka;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] titles;
    private final Drawable[] icons;

    public AddListAdapter(Context context, String[] product_ids, String[] titles,  Drawable[] icons) {
        super(context, R.layout.choose_fav_list_item, product_ids); // !!!
        this.context = context;
        this.titles = titles;
        this.icons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.add_dialog_item, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstLine.setText(titles[position]);
        firstLine.setTextColor(Color.parseColor("#9E9E9E"));
        imageView.setImageDrawable(icons[position]);


        return rowView;
    }

}