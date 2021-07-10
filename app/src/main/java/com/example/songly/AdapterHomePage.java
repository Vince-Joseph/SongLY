package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterHomePage extends RecyclerView.Adapter<AdapterHomePage.ViewHolder> {

    List<String> folderNames; // folder names list
    Context context;
    LayoutInflater layoutInflater;

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

        View view = layoutInflater.inflate(R.layout.individual_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterHomePage.ViewHolder holder, int position) {
        holder.folderName.setText(folderNames.get(position)); // on binding, set text for each folder
    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    /**
     * View holder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.folderName = itemView.findViewById(R.id.folderName);

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
