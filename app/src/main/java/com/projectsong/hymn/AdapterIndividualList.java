package com.example.songly;

import android.content.Context;
import android.content.Intent;
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

public class AdapterIndividualList extends RecyclerView.Adapter<AdapterIndividualList.ViewHolder> {

    final List<ModalFullSearch> songNames;
    final List<ModalFullSearch> selected;

    final List<AdapterIndividualList.ViewHolder> viewHoldersList;
    final List<AdapterIndividualList.ViewHolder> fullViewHolder;
    AdapterIndividualList.OnClickAction receiver;

    final Context context;
    final LayoutInflater inflater;
    public  AdapterIndividualList(Context context, List<ModalFullSearch> listNames)
    {
        this.context = context;
        this.songNames = listNames;
        this.inflater = LayoutInflater.from(context);
        this.selected = new ArrayList<>();
        this.viewHoldersList = new ArrayList<>();
        this.fullViewHolder = new ArrayList<>();
    }

    public interface OnClickAction {
        boolean onClickAction();
        boolean onLongClickAction();
    }


    @NonNull
    @NotNull
    @Override
    public AdapterIndividualList.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

//        View view = inflater.inflate(R.layout.individual_list_item, parent, false);
        View view = inflater.inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterIndividualList.ViewHolder holder, int position) {

        final ModalFullSearch item = songNames.get(position);
        holder.listText.setText(item.getMalayalamTitle()); // set the title of textview
        holder.selectionIndicator.setImageResource(R.drawable.check_icon);
        holder.selectionIndicator.setVisibility(View.GONE);
        holder.theIcon.setImageResource(R.drawable.library_music);
        fullViewHolder.add(holder);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int countOfSelected = selected.size();
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
                int countOfSelected = selected.size();
                if(countOfSelected > 0)
                {
                    if (selected.contains(item)) {
                        selected.remove(item);
                        viewHoldersList.remove(holder);
                        unhighlightView(holder);
                    } else {
                        selected.add(item);
                        viewHoldersList.add(holder);
                        highlightView(holder);
                    }
                    receiver.onClickAction();
                }
                else
                {
                    Intent intent = new Intent(holder.itemView.getContext(), TabbedLyricsView.class);
                    Bundle bundle = new Bundle();
                    // we are passing an array of strings to TabbedLyricsView
                    bundle.putStringArray("contents", new String[]{
                            item.getPageStart(),
                            item.getPageEnd(),
                            item.getChord(),
                            item.getSong(),
                            item.getKaraoke()
                    });
                    intent.putExtras(bundle);

                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }
    private void highlightView(AdapterIndividualList.ViewHolder holder) {
        holder.selectionIndicator.setVisibility(View.VISIBLE);
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selected));
    }

    private void unhighlightView(AdapterIndividualList.ViewHolder holder) {
        holder.selectionIndicator.setVisibility(View.GONE);
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }


    @Override
    public int getItemCount() {
        return songNames.size();
    }

    public void clearAll(boolean isNotify) {
        viewHoldersList.clear();
//        viewHoldersList.addAll(fullViewHolder);
        for (AdapterIndividualList.ViewHolder rw: fullViewHolder ) {
            unhighlightView(rw);
        }

        selected.clear();
        if (isNotify) notifyDataSetChanged();
    }

    public void deleteFileNames() {
//        int size = viewHoldersList.size();

        for (AdapterIndividualList.ViewHolder rw: viewHoldersList ) {
            fullViewHolder.remove(rw);
        }
        for (ModalFullSearch item: selected ) {
            songNames.remove(item);
        }
        notifyDataSetChanged();
    }

    public void selectAll() {

        selected.clear();
        selected.addAll(songNames);
        viewHoldersList.clear();
        viewHoldersList.addAll(fullViewHolder);

        for (AdapterIndividualList.ViewHolder rw: viewHoldersList ) {
            highlightView(rw);
//            Log.d("engName", Integer.toString(viewHoldersList.size()));
        }

//        notifyDataSetChanged();
    }

    public List<ModalFullSearch> getSelected() {
        return selected;
    }

    public void removeAllSelections()
    {
        for (AdapterIndividualList.ViewHolder rw: viewHoldersList ) {
            unhighlightView(rw);
        }

        selected.clear();
        viewHoldersList.clear();
    }
    public void setActionModeReceiver(AdapterIndividualList.OnClickAction receiver) {
        this.receiver = receiver;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView listText;
        // --Commented out by Inspection (18-09-2021 05:47 PM):TextView malayalamTextView;
        final ImageView selectionIndicator;
        final ImageView theIcon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/MLKR0NTT.TTF");
//            listText =  itemView.findViewById(R.id.malayalamTitle);
            listText =  itemView.findViewById(R.id.songTitle);
            this.theIcon = itemView.findViewById(R.id.theIcon);
            listText.setTypeface(typeface);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
//            removeView = itemView.findViewById(R.id.removeIcon);

        }
    }
}
