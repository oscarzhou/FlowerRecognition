package com.google.sample.cloudvision;

/**
 * Created by oscar on 6/7/2017.
 */

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


        txtTitle.setText(Html.fromHtml(itemname[position]));
        imageView.setImageResource(imgid[position]);
        txtTitle.setMovementMethod(LinkMovementMethod.getInstance());

        return rowView;

    };
}



