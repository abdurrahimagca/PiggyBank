package com.example.piggybank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {
    ArrayList<CardInfoModel> card = new ArrayList<>();
    Context context;
    RecyclerViewInterface recyclerViewInterface;

    public MainRecyclerViewAdapter(ArrayList<CardInfoModel> card, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.card = card;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MainRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.MyViewHolder holder, int position) {
        CardInfoModel cardInfoModel = card.get(position);
        holder.cardNumber.setText(cardInfoModel.getCardNum());
        if("BankCard".equals(cardInfoModel.getCardType())){
            holder.cardType.setText("Banka Kartı");
        }
        else{
            holder.cardType.setText("Kredi Kartı");
        }


    }

    @Override
    public int getItemCount() {
        return card.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cardNumber;
        TextView cardType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_set_num_tv);
            cardType = itemView.findViewById(R.id.card_type_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface !=  null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION);
                        {
                            recyclerViewInterface.itemClickListener(pos);
                        }
                    }
                }
            });
        }

    }
}
