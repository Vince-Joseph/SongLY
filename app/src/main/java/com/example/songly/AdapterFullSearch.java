package com.example.songly;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class AdapterFullSearch extends RecyclerView.Adapter<AdapterFullSearch.ViewHolder> implements Filterable {
    private List<ModalFullSearch> modifiedList;
    private List<ModalFullSearch> fullListOfSongs;

//    List<ModalFullSearch> checkedList;
    List<ModalFullSearch> existingList;

    SongTitleClicked songTitleClickedInterface;
    public String mode = "";
    Context context;

    // constructor - initialises the full song's arrayList with passed data
    AdapterFullSearch(List<ModalFullSearch> modifiedList, SongTitleClicked songTitleClickedInterface,
                      String mode,
                      ArrayList<ModalFullSearch> existingList, Context context){

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
//        Toast.makeText(v.getContext(), mode, Toast.LENGTH_SHORT).show();
        return new ViewHolder(v, songTitleClickedInterface, mode);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModalFullSearch currentItem = modifiedList.get(position);

        // change the font of textView to malayalam using previously defined Typeface obj

        holder.malayalamTextView.setTypeface(currentItem.getTypeface());
        // set the passed text from ActiitySearchSongList
        holder.malayalamTextView.setText(currentItem.getMalayalamTitle());
        holder.englishTitleTextView.setText(currentItem.getEnglishTitle());
        holder.folderNameTextView.setText(currentItem.getFolderName());
        holder.fileNameTextView.setText(currentItem.getFileName());


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                selectSong( currentItem,  holder,  isChecked);
            }
        });

        holder.linearLayoutHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mode.equals("on"))
                {
                    selectSong( currentItem,  holder,  holder.checkBox.isChecked());
                    if(holder.checkBox.isChecked())
                        holder.checkBox.setChecked(false);
                    else
                        holder.checkBox.setChecked(true);
                }
                else
                    songTitleClickedInterface.songTitleClicked(position);
            }
        });



        // set the already selected song's checkboxes to checked
        boolean flag = false;
        for (int i = 0; i< FullSearch.checkedList.size(); i++)
        {
            if(holder.englishTitleTextView.getText().toString().equals(
                    FullSearch.checkedList.get(i).getEnglishTitle()))
            {
                flag = true;
                break;
            }
        }

        if(flag)
            holder.checkBox.setChecked(true);
        else
             holder.checkBox.setChecked(false);

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
    class ViewHolder extends RecyclerView.ViewHolder   {
        TextView malayalamTextView;
        TextView englishTitleTextView;
        TextView folderNameTextView;
        TextView fileNameTextView;
        LinearLayout linearLayoutHolder;
        CheckBox checkBox;
        SongTitleClicked songTitleClicked;


        ViewHolder(View itemView,  AdapterFullSearch.SongTitleClicked songTitleClicked, String mode) {
            super(itemView);

            malayalamTextView = itemView.findViewById(R.id.malayalamTitle);
            englishTitleTextView = itemView.findViewById(R.id.englishTitle);
            folderNameTextView = itemView.findViewById(R.id.foldernameofsong);
            fileNameTextView = itemView.findViewById(R.id.fileNameOfSong);
            checkBox = itemView.findViewById(R.id.selectSong);
            linearLayoutHolder = itemView.findViewById(R.id.searchHolder);

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
            if(isChecked == true)
            {
                boolean flag = false;
                // search whether current song has been selected already or not, using checkedList
                for (int i = 0; i< FullSearch.checkedList.size(); i++)
                {
                    flag = false;

                    if(holder.englishTitleTextView.getText().toString().equals(
                            FullSearch.checkedList.get(i).getEnglishTitle()))
                    {
                        flag = true;
                        break;
                    }
                }
                // if not, then add current song to the checkedlist
                if(flag == false)
                {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/MLKR0NTT.TTF");

                    FullSearch.checkedList.add(new ModalFullSearch(
                            currentItem.getFileName(),
                            currentItem.getEnglishTitle(),
                            currentItem.getMalayalamTitle(),
                            currentItem.getFolderName(),
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
                for (int i = 0; i< FullSearch.checkedList.size(); i++)
                {
                    if(holder.englishTitleTextView.getText().toString().equals(
                            FullSearch.checkedList.get(i).getEnglishTitle()))
                    {
                        FullSearch.checkedList.remove(i);
                        break;
                    }
                }
            }
        }
    }
}