package com.artemmarchenko.ua.placesnearby.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.artemmarchenko.ua.placesnearby.R;

import java.util.ArrayList;

/**
 * Created by Artem on 22.05.15.
 */
public class PlaceType {
    String name, title;
    ArrayList<PlaceType> subTypes;
    PlaceType parentType;
    Drawable icon;
    ColorStateList color;
    boolean isSelected = false, isExpanded = false;

    public PlaceType(Context context, String name, ArrayList<PlaceType> subTypes) {
        this.name = name;
        Resources res = context.getResources();

        try {
            title = res.getString(res.getIdentifier(name, "string", context.getPackageName()));
        } catch (Resources.NotFoundException e){}

        color = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[] {
                        res.getColor(res.getIdentifier(name, "color", context.getPackageName())),
                        context.getResources().getColor(R.color.grey),
                }
        );

        icon = res.getDrawable(res.getIdentifier("ic_"+name, "drawable", context.getPackageName()));
        icon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(icon, color);

        this.subTypes = new ArrayList<>();
        if (subTypes != null) {
            for(PlaceType t: subTypes)
                t.setParentType(this);
            this.subTypes.addAll(subTypes);
        }
    }

    public PlaceType(Context context, String name) {
        this.name = name;
        Resources res = context.getResources();
        try {
            title = res.getString(res.getIdentifier(name, "string", context.getPackageName()));
        } catch (Resources.NotFoundException e){}
    }

    public boolean hasSubTypes() {
        return subTypes != null && subTypes.size()>0;
    }
    public boolean isHeader() {
        return subTypes != null;
    }

    public void setParentType(PlaceType parentType) {
        this.parentType = parentType;
        color = parentType.color;
    }

    @Override
    public String toString() {
        if (title != null)
            return title;
        else
            return name;
    }

    @Override
    public boolean equals(Object o) {
        return name.equalsIgnoreCase(((PlaceType)o).name);
    }
}