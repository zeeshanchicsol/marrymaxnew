package com.chicsol.marrymax.adapters;

/**
 * Created by macintoshhd on 12/26/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mChild;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.modal.mParentChild;
import com.chicsol.marrymax.utils.NestedRecyclerLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chicsol.marrymax.utils.Constants.selectedQuestions;

/*import com.example.nestedrecyclerview.NestedRecyclerLinearLayoutManager;

import com.example.nestedrecyclerview.model.Child;
import com.example.nestedrecyclerview.model.ParentChild;

import java.util.ArrayList;

import butterknife.Bind;*/


public class ParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<mParentChild> parentChildData = new ArrayList<>();
    Context ctx;

    public ParentAdapter(Context ctx, ArrayList<mParentChild> parentChildData) {
        this.ctx = ctx;
        this.parentChildData = parentChildData;
        selectedQuestions.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_child)
        RecyclerView rv_child;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_recycler_item_parent, parent, false);
        ParentAdapter.ViewHolder pavh = new ParentAdapter.ViewHolder(itemLayoutView);
        return pavh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        mParentChild p = parentChildData.get(position);
        initChildLayoutManager(vh.rv_child, p.getChild(), p.getTitle(), Integer.parseInt(p.getId()));
    }

    private void initChildLayoutManager(RecyclerView rv_child, ArrayList<mChild> childData, String title, int pos) {
        rv_child.setLayoutManager(new NestedRecyclerLinearLayoutManager(ctx));
        ChildAdapter childAdapter = new ChildAdapter(childData, title, pos);
        rv_child.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return parentChildData.size();
    }

    public void clear() {
        parentChildData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<mParentChild> lst) {
        parentChildData.clear();
        parentChildData.addAll(lst);
        notifyDataSetChanged();

        // Log.e("item size in adapter", parentChildData.size() + "");
    }
}