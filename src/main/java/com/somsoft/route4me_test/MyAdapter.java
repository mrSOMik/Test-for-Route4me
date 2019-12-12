package com.somsoft.route4me_test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Pair> list;

    public MyAdapter(ArrayList<Pair> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.tvCurrency.setText(list.get(i).getCurrency());
        viewHolder.tvRate.setText(String.format("%.2f", list.get(i).getRate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        View mView;
        TextView tvCurrency;
        TextView tvRate;

        public MyViewHolder(View view) {
            super(view);
            tvCurrency = view.findViewById(R.id.currency);
            tvRate = view.findViewById(R.id.rate);
//            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ChartActivity.class);
                    intent.putExtra("currency", list.get(getAdapterPosition()).getCurrency());

                    context.startActivity(intent);
                }
            });
        }
    }

}
