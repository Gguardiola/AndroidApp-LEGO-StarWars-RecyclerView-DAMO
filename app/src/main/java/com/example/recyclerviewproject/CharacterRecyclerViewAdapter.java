package com.example.recyclerviewproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CharacterRecyclerViewAdapter extends RecyclerView.Adapter<CharacterRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<characterDAMO> lchars;
    private Context context;
    private OnClickListener onClickListener;

    private int selectedItem;

    private MyViewHolder lastItem = null;



    public CharacterRecyclerViewAdapter(ArrayList<characterDAMO> lchars, Context context) {
        this.lchars = lchars;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imatgeImg;
        private TextView nameTxt;
        private TextView descTxt;

        private CardView charCard;

        private CardView charItem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imatgeImg = itemView.findViewById(R.id.charImg);
            nameTxt = itemView.findViewById(R.id.charName);
            descTxt = itemView.findViewById(R.id.charDesc);
            charCard = itemView.findViewById(R.id.char_card);

        }
    }
    @NonNull
    @Override
    public CharacterRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(context).inflate(R.layout.activity_details, parent, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterRecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nameTxt.setText(lchars.get(position).getName());
        holder.descTxt.setText(lchars.get(position).getDescription());
        holder.imatgeImg.setImageResource(lchars.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
                else{
                    if(lastItem != null) {
                        lastItem.charCard.setBackgroundColor(Color.WHITE);
                    }
                    lastItem = holder;
                    holder.charCard.setBackgroundColor(Color.parseColor("#D4EFDF"));
                    setSelectedItem(position);
                }
            }
        });

    }

    private void setSelectedItem(int position){
        this.selectedItem = position;
        Log.d("ITEM SELECTED: ", String.valueOf(lchars.get(selectedItem).getName()));
    }

    public int getSelectedItem(){
        return this.selectedItem;
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return lchars.size();
    }
}
