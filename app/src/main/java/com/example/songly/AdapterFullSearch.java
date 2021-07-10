package com.example.songly;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class AdapterFullSearch extends RecyclerView.Adapter<AdapterFullSearch.ViewHolder> implements Filterable {
    private List<ModalFullSearch> modifiedList;
    private List<ModalFullSearch> fullListOfSongs;
    SongTitleClicked songTitleClickedInterface;

    // constructor - initialises the full song's arrayList with passed data
    AdapterFullSearch(List<ModalFullSearch> modifiedList, SongTitleClicked songTitleClickedInterface)
    {
        fullListOfSongs = new ArrayList<>(modifiedList);
        this.modifiedList = modifiedList;
        this.songTitleClickedInterface = songTitleClickedInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflating representation of individual song
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_search_item,
                parent, false);

        return new ViewHolder(v, songTitleClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModalFullSearch currentItem = modifiedList.get(position);

        // change the font of textView to malayalam using previously defined Typeface obj
        holder.malayalamTextView.setTypeface(currentItem.getTypeface());
        // set the passed text from ActiitySearchSongList
        holder.malayalamTextView.setText(currentItem.getMalayalamTitle());

    }

    @Override
    public int getItemCount() {
        return modifiedList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        @Override // filter the list using english title
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModalFullSearch> filteredList = new ArrayList<>(); // temp list

            // if search keyword's len is zero/ null then
            if (constraint == null || constraint.length() == 0)
            {
                // put entire songs as modified list - means make no filtering to existing list
                filteredList.addAll(fullListOfSongs);
            }
            else
            {
                // convert the search keyword to all lowercase
                String filterPattern = constraint.toString().toLowerCase().trim();

                // now search for given keyword in the entire song's list
                for (ModalFullSearch item : fullListOfSongs) {
                    // compare the english titles
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
            modifiedList.clear();
            modifiedList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    // interface
    public interface SongTitleClicked
    {
        void songTitleClicked(int position);
    }

    /**
     * View holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView malayalamTextView;
        SongTitleClicked songTitleClicked;


        ViewHolder(View itemView,  AdapterFullSearch.SongTitleClicked songTitleClicked) {
            super(itemView);

            malayalamTextView = itemView.findViewById(R.id.malayalamTitle);

            this.songTitleClicked = songTitleClicked;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            songTitleClickedInterface.songTitleClicked(getAbsoluteAdapterPosition());
        }
    }
}