package com.example.songly;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class AdapterExtraPrayers extends RecyclerView.Adapter<AdapterExtraPrayers.ViewHolder> {

    List<ModalFullSearch> prayerNames; // stores karososa titles

    public AdapterExtraPrayers(List<ModalFullSearch> prayers)
    {
        this.prayerNames = prayers;
    }
    @NonNull
    @NotNull
    @Override
    public AdapterExtraPrayers.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item,parent,false);
        return new AdapterExtraPrayers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.malayalamTitle.setTypeface(holder.typeface);
        holder.malayalamTitle.setText(prayerNames.get(position).getMalayalamTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.malayalamTitle.getContext(), KarososaViewActivity.class);
                intent.putExtra("pageStart", prayerNames.get(position).getPageStart());
                intent.putExtra("pageEnd", prayerNames.get(position).getPageEnd());
                holder.malayalamTitle.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return prayerNames.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView malayalamTitle;
        Typeface typeface;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            malayalamTitle = itemView.findViewById(R.id.songTitle);
            typeface = Typeface.createFromAsset(itemView.getContext().getAssets(),"font/MLKR0NTT.TTF");
        }
    }
}
