package org.hssus.khel.hsskhel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.activity.BaseActivity;
import org.hssus.khel.hsskhel.activity.KhelDetails;
import org.hssus.khel.hsskhel.util.Constant;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDatasetKhelName,mdatasetKhelDescription,mDatasetKhelIds;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context,String[] mDatasetKhelName,String[] mdatasetKhelDescription,String[] mDatasetKhelIds) {
        this.context=context;
        this.mDatasetKhelName = mDatasetKhelName;
        this.mdatasetKhelDescription = mdatasetKhelDescription;
        this.mDatasetKhelIds = mDatasetKhelIds;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.row_layout, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent intent = new Intent(context, KhelDetails.class);
                intent.putExtra(Constant.GAME_ID, mDatasetKhelIds[holder.getAdapterPosition()]);
                context.startActivity(intent);

            }
        });

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextViewKhelName.setText(mDatasetKhelName[position]);
        holder.mTextViewKhelDescription.setText(mdatasetKhelDescription[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetKhelName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView mTextViewKhelName,mTextViewKhelDescription;

        public MyViewHolder(View item) {
            super(item);

            mTextViewKhelName = (TextView) item.findViewById(R.id.txt_khel_name);
            mTextViewKhelDescription = (TextView) item.findViewById(R.id.txt_khel_description);
        }
    }
}

