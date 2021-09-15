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

    private final List<ModelClassSongList> OriginalSongList;
    private final List<ModelClassSongList> exampleList;

    final SongClickInterface songClickInterface;
    final Typeface typeface; // to set malayalam font for the recycler view

    public Adapter(List<ModelClassSongList>userList, SongClickInterface songClickInterface,
    Typeface typeface) {

        this.OriginalSongList = new ArrayList<>(userList);
        this.exampleList = userList;

        this.songClickInterface = songClickInterface;

        this.typeface = typeface;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item,parent,false);
        return new ViewHolder(view, songClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String title=exampleList.get(position).getTextViewSongTitle();
        holder.setData(title);
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

        public void setData(String name) {


            textView.setTypeface(typeface);
            textView.setText(name);

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

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelClassSongList> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(OriginalSongList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelClassSongList item : OriginalSongList) {
                    if (item.getEngName().toLowerCase().contains(filterPattern)) {
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
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}