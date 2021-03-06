package com.example.songly;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class AdapterFullSearch extends RecyclerView.Adapter<AdapterFullSearch.ViewHolder> implements Filterable {
    private final List<ModalFullSearch> modifiedList;
    private final List<ModalFullSearch> fullListOfSongs;

//    List<ModalFullSearch> checkedList;

    final SongTitleClicked songTitleClickedInterface;
    public String mode; // indicates selection mode or view mode
    final Context context;

    // constructor - initialises the full song's arrayList with passed data
    AdapterFullSearch(List<ModalFullSearch> modifiedList, SongTitleClicked songTitleClickedInterface,
                      String mode,
                      Context context){

        fullListOfSongs = new ArrayList<>(modifiedList);
        FullSearch.checkedList = new ArrayList<>();

        this.modifiedList = modifiedList;

        this.songTitleClickedInterface = songTitleClickedInterface;
        this.mode = mode;
        this.context = context;
//        this.adapterIndividualList = new AdapterIndividualList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflating representation of individual song
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_search_item,
                parent, false);
        return new ViewHolder(v, songTitleClickedInterface, mode);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModalFullSearch currentItem = modifiedList.get(position);

        // change the font of textView to malayalam using previously defined Typeface obj
        holder.malayalamTextView.setTypeface(currentItem.getTypeface());
        // set the passed text from ActivitySearchSongList
        holder.malayalamTextView.setText(currentItem.getMalayalamTitle());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                selectSong( currentItem,  holder,  isChecked);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mode.equals("on"))
                {
                    selectSong( currentItem,  holder,  holder.checkBox.isChecked());
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                }
                else
                    songTitleClickedInterface.songTitleClicked(position);
            }
        });

        // set the already selected song's checkboxes to checked
        boolean flag = false;
        String engTitle = modifiedList.get(holder.getAbsoluteAdapterPosition()).getEnglishTitle();
        for (int i = 0; i< FullSearch.checkedList.size(); i++)
        {
            if(engTitle.equals(FullSearch.checkedList.get(i).getEnglishTitle()))
            {
                flag = true;
                break;
            }
        }
            holder.checkBox.setChecked(flag);
    }

    @Override
    public int getItemCount() {
        return modifiedList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {

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
    static class ViewHolder extends RecyclerView.ViewHolder   {
        final TextView malayalamTextView;
        final CheckBox checkBox;
       final SongTitleClicked songTitleClicked;


        ViewHolder(View itemView,  AdapterFullSearch.SongTitleClicked songTitleClicked, String mode) {
            super(itemView);

            malayalamTextView = itemView.findViewById(R.id.malayalamTitle);
            checkBox = itemView.findViewById(R.id.selectSong);

            if(mode.equals("off"))
                checkBox.setVisibility(View.GONE);
            else
                checkBox.setVisibility(View.VISIBLE);

            this.songTitleClicked = songTitleClicked;
//            itemView.setOnClickListener(this);

        }
    }

    void selectSong(ModalFullSearch currentItem, ViewHolder holder, boolean isChecked)
    {

        if(mode.equals("on"))
        {
            if(isChecked)
            {
                boolean flag = false;
                // search whether current song has been selected already present or not, using checkedList
                String engTitle = modifiedList.get(holder.getAbsoluteAdapterPosition()).getEnglishTitle();
                for (int i = 0; i< FullSearch.checkedList.size(); i++)
                {
                    flag = false;
                    if(engTitle.equals(FullSearch.checkedList.get(i).getEnglishTitle()))
                    {
                        flag = true;
                        break;
                    }
                }
                // if not, then add current song to the checkedlist
                if(!flag)
                {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/MLKR0NTT.TTF");

                    FullSearch.checkedList.add(new ModalFullSearch(
                            currentItem.getPageStart(),
                            currentItem.getPageEnd(),
                            currentItem.getEnglishTitle(),
                            currentItem.getMalayalamTitle(),
                            currentItem.getChord(),
                            currentItem.getSong(),
                            currentItem.getKaraoke(),
                            typeface
                    ));
                }
            }
            else
            {
                // search and remove the unchecked element from the checkedList
                String engTitle = modifiedList.get(holder.getAbsoluteAdapterPosition()).getEnglishTitle();
                for (int i = 0; i< FullSearch.checkedList.size(); i++)
                {
                    if(engTitle.equals(FullSearch.checkedList.get(i).getEnglishTitle()))
                    {
                        FullSearch.checkedList.remove(i);
                        break;
                    }
                }
            }
        }
    }
}