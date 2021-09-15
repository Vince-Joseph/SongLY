package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterAppliedList extends RecyclerView.Adapter<AdapterAppliedList.ViewHolder> {

    List<ModalFullSearch> itemLists ;
    LayoutInflater inflater;

    public AdapterAppliedList(List<ModalFullSearch> itemLists, Context context)
    {
         inflater = LayoutInflater.from(context);
        this.itemLists = itemLists;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_list_item, parent, false); // inflate layout
        return new AdapterAppliedList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.malayalamTitle.setTypeface(Typeface.createFromAsset(
                holder.malayalamTitle.getContext().getAssets(),"font/MLKR0NTT.TTF"));
        holder.malayalamTitle.setText(itemLists.get(position).getMalayalamTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), TabbedLyricsView.class);
                Bundle bundle = new Bundle();

                // we are passing an array of strings to TabbedLyricsView
                bundle.putStringArray("contents", new String[]{
                        itemLists.get(position).getPageStart(),
                        itemLists.get(position).getPageEnd(),
                        itemLists.get(position).getChord(),
                        itemLists.get(position).getSong(),
                        itemLists.get(position).getKaraoke()
                });
                intent.putExtras(bundle);

                //         now invoke the intent
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView malayalamTitle;
        ImageView deleteIcon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            malayalamTitle = itemView.findViewById(R.id.songTitle);
//            deleteIcon = itemView.findViewById(R.id.selectionIndicator);
//            deleteIcon.setImageResource(R.drawable.delete_icon);
//            deleteIcon.setColorFilter(Color.RED);
        }
    }
}
