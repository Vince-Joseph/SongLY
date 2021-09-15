package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterHomePage extends RecyclerView.Adapter<AdapterHomePage.ViewHolder> {

    final List<String> folderNames; // folder names list
    final Context context;
    final LayoutInflater layoutInflater;

    public AdapterHomePage(Context context,  List<String> folderNames)
    {
        this.folderNames = folderNames;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @NotNull
    @Override
    public AdapterHomePage.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterHomePage.ViewHolder holder, int position) {
        holder.folderName.setText(folderNames.get(position)); // on binding, set text for each folder
        holder.theIcon.setImageResource(R.drawable.ic_baseline_folder_24);
    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    /**
     * View holder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView folderName;
        final ImageView theIcon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.folderName = itemView.findViewById(R.id.songTitle);
            this.theIcon = itemView.findViewById(R.id.theIcon);

            // on clicking a view(folder icon + its text), go to next activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ListOfSongs.class);

                    // notify the next activity which folder has been clicked
                    intent.putExtra("folder", folderName.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
