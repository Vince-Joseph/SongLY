package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    final List<String> listNames;
    final List<String> selected;

    final List<AdapterList.ViewHolder> viewHoldersList;
    final List<AdapterList.ViewHolder> fullViewHolder; // list to hold all list views
    AdapterList.OnClickAction receiver;

    final SharedPreferences sharedPreferences;

    final LayoutInflater inflater;
    Intent intent;
    public  AdapterList(Context context, List<String> listNames)
    {
        this.listNames = listNames;
        this.inflater = LayoutInflater.from(context);
        this.selected = new ArrayList<>();
        this.viewHoldersList = new ArrayList<>();
        this.fullViewHolder = new ArrayList<>();
        this.sharedPreferences = context.getSharedPreferences("SongLY", Context.MODE_PRIVATE);
    }

    // method to update a list's name after editing its name
    // used in ListActivity.java
    public void updateNames(String oldName, String newFileName) {
        listNames.set(listNames.indexOf(oldName), newFileName);
        notifyDataSetChanged();
    }


    public interface OnClickAction {
         boolean onClickAction();
         boolean onLongClickAction();
    }

    @NonNull
    @NotNull
    @Override
    public AdapterList.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    // bind the actions to be performed on each list view
    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterList.ViewHolder holder, int position) {

        final String item = listNames.get(position);
        holder.listText.setText(item); // set the list name
        holder.theIcon.setImageResource(R.drawable.list_icon_100);
        fullViewHolder.add(holder); // add current list view to full list of view holders

        // upon long pressing a list view
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int countOfSelected = selected.size(); // take the size of selected lists

                // if the size = 0, then start action mode
                // add current item into selected arraylist
                // highlight it
                if(countOfSelected == 0)
                {
                    receiver.onLongClickAction();
                    selected.add(item);
                    viewHoldersList.add(holder);
                    highlightView(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countOfSelected = selected.size(); // take the size of selected lists

                // if some lists are already present then
                if(countOfSelected > 0)
                {
                    // check whether currently selected list is present in the list
                    // if so remove it from the list, else add it as new element
                    if (selected.contains(item))
                    {
                        selected.remove(item);
                        viewHoldersList.remove(holder);
                        unhighlightView(holder);
                    }
                    else
                    {
                        selected.add(item);
                        viewHoldersList.add(holder);
                        highlightView(holder);
                    }
                    receiver.onClickAction();
                }
                else // else goto the activity to display the contents of the clicked list
                {
                    intent = new Intent(holder.itemView.getContext(), ViewIndividualList.class);
                    sharedPreferences.edit().putString("file_name", holder.listText.getText().toString()+".txt").apply();
                    sharedPreferences.edit().putString("selected", "off").apply();
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }

    // method to highlight the given view
    private void highlightView(AdapterList.ViewHolder holder) {
        holder.checkIcon.setImageResource(R.drawable.check_icon);
//        holder.checkIcon.setVisibility(View.VISIBLE);
    }

    // method to UN highlight the given view
    private void unhighlightView(AdapterList.ViewHolder holder) {
        holder.checkIcon.setImageResource(R.drawable.right_arrow_go);
    }


    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public void clearAll(boolean isNotify) {
        viewHoldersList.clear();
        for (AdapterList.ViewHolder rw: fullViewHolder ) {
            unhighlightView(rw);
        }

        selected.clear();
        if (isNotify) notifyDataSetChanged();
    }

    public void deleteFileNames() {

        for (AdapterList.ViewHolder rw: viewHoldersList ) {
            fullViewHolder.remove(rw);
        }
        for (String item: selected ) {
            listNames.remove(item);
        }
        notifyDataSetChanged();
    }

    public void selectAll() {

        selected.clear();
        selected.addAll(listNames);

        viewHoldersList.addAll(fullViewHolder);

        for (AdapterList.ViewHolder rw: fullViewHolder ) {
            highlightView(rw);
        }

//        notifyDataSetChanged();
    }

    public List<String> getSelected() {
        return selected;
    }

    public void removeAllSelections()
    {
        for (AdapterList.ViewHolder rw: viewHoldersList ) {
            unhighlightView(rw);
        }

        selected.clear();
        viewHoldersList.clear();
    }
    public void setActionModeReceiver(AdapterList.OnClickAction receiver) {
        this.receiver = receiver;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView listText;
        final ImageView checkIcon;
        final ImageView theIcon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            listText =  itemView.findViewById(R.id.songTitle);
            theIcon =  itemView.findViewById(R.id.theIcon);
            checkIcon = itemView.findViewById(R.id.selectionIndicator);
        }
    }
}
