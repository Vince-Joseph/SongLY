package com.example.songly;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    // contains all the passed list of songs, this list is used to restore the original passed songs
    private final List<ModalFullSearch> OriginalSongList;
    private final List<ModalFullSearch> exampleList; // all experiments are done on this list

    final SongClickInterface songClickInterface;

    public Adapter(List<ModalFullSearch>userList, SongClickInterface songClickInterface) {

        this.OriginalSongList = new ArrayList<>(userList); // copy the passed data
        this.exampleList = userList; // copy the passed data
        this.songClickInterface = songClickInterface;

    }

    // upon creating viewholder, inflate individual song list item's layout
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item,parent,false);
        return new ViewHolder(view, songClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String title = exampleList.get(position).getMalayalamTitle();
        // set the typeface(font of textview) to malayalam
        holder.textView.setTypeface(Typeface.createFromAsset(
                holder.textView.getContext().getAssets(),"font/MLKR0NTT.TTF"));
        holder.textView.setText(title); // set the malayalam title
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    //view holder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final SongClickInterface songClickInterface;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView, SongClickInterface songClickInterface) {
            super(itemView);

            //here use xml ids
            textView=itemView.findViewById(R.id.songTitle);


            this.songClickInterface = songClickInterface;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            songClickInterface.songClicked(getAbsoluteAdapterPosition());
        }
    }

    interface SongClickInterface
    {
        void songClicked(int position);
    }

    // filter the list of songs based on query (handle searching)
    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModalFullSearch> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(OriginalSongList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModalFullSearch item : OriginalSongList) {
                    if (item.getEnglishTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            // we are creating new songs list with the help of filtered results
            exampleList.addAll((List) results.values);
            notifyDataSetChanged(); // notify the changes
        }
    };
}