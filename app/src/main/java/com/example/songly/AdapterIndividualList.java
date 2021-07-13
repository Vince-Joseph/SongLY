package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterIndividualList extends RecyclerView.Adapter<AdapterIndividualList.ViewHolder> {

    List<ModalFullSearch> songNames;
    List<ModalFullSearch> selected;

    List<AdapterIndividualList.ViewHolder> viewHoldersList;
    List<AdapterIndividualList.ViewHolder> fullViewHolder;
    AdapterIndividualList.OnClickAction receiver;

    Context context;
    LayoutInflater inflater;
    ImageView  deleteIcon;
    public  AdapterIndividualList(Context context, List<ModalFullSearch> listNames)
    {
        this.context = context;
        this.songNames = listNames;
        this.inflater = LayoutInflater.from(context);
        this.selected = new ArrayList<>();
        this.viewHoldersList = new ArrayList<>();
        this.fullViewHolder = new ArrayList<>();
    }


//    public void addToSelected(String songName)
//    {
//        this.selected.add(songName);
//    }


    public interface OnClickAction {
        public boolean onClickAction();
        public boolean onLongClickAction();
    }


    @NonNull
    @NotNull
    @Override
    public AdapterIndividualList.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.individual_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterIndividualList.ViewHolder holder, int position) {

        final ModalFullSearch item = songNames.get(position);
        holder.listText.setText(item.getMalayalamTitle()); // set the title of textview
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
                    intent.putExtra("fileName", item.getFileName());
                    intent.putExtra("folderName",item.getFolderName());
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

        viewHoldersList.addAll(fullViewHolder);

        for (AdapterIndividualList.ViewHolder rw: viewHoldersList ) {
            highlightView(rw);
        }

        notifyDataSetChanged();
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

        TextView listText;
        TextView malayalamTextView;
        ImageView selectionIndicator;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/MLKR0NTT.TTF");
            listText =  itemView.findViewById(R.id.malayalamTitle);
            listText.setTypeface(typeface);
            selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
//            removeView = itemView.findViewById(R.id.removeIcon);

        }
    }


}
