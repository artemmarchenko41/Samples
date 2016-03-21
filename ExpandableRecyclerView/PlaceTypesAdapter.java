package com.artemmarchenko.ua.placesnearby.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.artemmarchenko.ua.placesnearby.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Artem on 20.05.15.
 */
public class PlaceTypesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEADER = 1, ITEM = 2;
    private List<PlaceType> mAllData, mData;

    public PlaceTypesAdapter(List<PlaceType> data) {
        this.mAllData = data;
        mData = new ArrayList<>();
        mData.addAll(mAllData);
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).isHeader()) return HEADER;
        else return ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == HEADER)
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.type_header, viewGroup, false));
        else
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.type_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((BaseViewHolder) viewHolder).setData(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
    }

    private class ScrollHandler implements Runnable {
        int pos;

        public ScrollHandler(int pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            if (mRecyclerView != null)
                mRecyclerView.smoothScrollToPosition(pos);
        }
    }

    private class HeaderViewHolder extends BaseViewHolder {

        View ic_more;
        ImageView ic_type;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ic_type = (ImageView) itemView.findViewById(R.id.icon);
            ic_more = itemView.findViewById(R.id.icon_more);
        }

        @Override
        public void setData(PlaceType type) {
            super.setData(type);
            ic_type.setImageDrawable(type.icon);
            if (type.hasSubTypes()) {
                ic_more.setVisibility(View.VISIBLE);
                ic_more.setRotation(type.isExpanded ? 180 : 0);
            } else {
                ic_more.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(final View view) {
            PlaceType type = mData.get(getAdapterPosition());
            if (type.hasSubTypes()) {
                int nextPos = getAdapterPosition() + 1;

                if (!type.isExpanded) {
                    mData.addAll(nextPos, type.subTypes);
                    type.isExpanded = true;

                    notifyItemRangeInserted(nextPos, type.subTypes.size());

                    ic_more.animate().rotation(180).setInterpolator(new OvershootInterpolator());

                    if (mRecyclerView != null)
                        mRecyclerView.postDelayed(new ScrollHandler(nextPos + type.subTypes.size() - 1), 500);

                } else {
                    mData.removeAll(type.subTypes);
                    type.isExpanded = false;

                    notifyItemRangeRemoved(nextPos, type.subTypes.size());

                    ic_more.animate().rotation(0).setInterpolator(new OvershootInterpolator());
                }
            } else {
                boolean act = !mParentView.isSelected();

                type.isSelected = act;
                mParentView.setSelected(act);
            }
        }
    }

    private class ItemViewHolder extends BaseViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            PlaceType type = mData.get(getAdapterPosition());
            boolean act = !mParentView.isSelected();

            type.isSelected = act;
            mParentView.setSelected(act);

            if (act && !type.parentType.isSelected) {
                type.parentType.isSelected = true;
                notifyItemChanged(mData.indexOf(type.parentType));
            } else if (type.parentType.isSelected) {
                boolean f = false;
                for (PlaceType p : type.parentType.subTypes) {
                    if (p.isSelected) f = true;
                }
                if (!f) {
                    type.parentType.isSelected = false;
                    notifyItemChanged(mData.indexOf(type.parentType));
                }
            }

        }
    }

    private abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleText;
        View mParentView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            itemView.setOnClickListener(this);

            mTitleText = (TextView) itemView.findViewById(R.id.name);
        }

        public void setData(PlaceType type) {
            mParentView.setSelected(type.isSelected);
            mTitleText.setTextColor(type.color);
            mTitleText.setText(type.toString());
        }
    }

}