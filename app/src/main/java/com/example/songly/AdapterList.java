package com.example.songly;

import android.content.Context;
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

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    List<String> listNames;
    List<String> selected;

    List<AdapterList.ViewHolder> viewHoldersList;
    List<AdapterList.ViewHolder> fullViewHolder;
    AdapterList.OnClickAction receiver;

    Context context;
    LayoutInflater inflater;
    ImageView  deleteIcon;
    public  AdapterList(Context context, List<String> listNames)
    {
        this.context = context;
        this.listNames = listNames;
        this.inflater = LayoutInflater.from(context);
        this.selected = new ArrayList<>();
        this.viewHoldersList = new ArrayList<>();
        this.fullViewHolder = new ArrayList<>();
    }

    public void updateNames(String oldName, String newFileName) {
//        Toast.makeText(context, selected.get(0), Toast.LENGTH_SHORT).show();
        listNames.set(listNames.indexOf(oldName), newFileName);

        notifyDataSetChanged();
    }


    public interface OnClickAction {
        public boolean onClickAction();
        public boolean onLongClickAction();
    }


    @NonNull
    @NotNull
    @Override
    public AdapterList.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.individual_grid_view_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterList.ViewHolder holder, int position) {

        final String item = listNames.get(position);
        holder.listText.setText(listNames.get(position));
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
                    //selected.clear();
                }
            }
        });

    }
    private void highlightView(AdapterList.ViewHolder holder) {
        holder.checkIcon.setVisibility(View.VISIBLE);
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selected));
    }

    private void unhighlightView(AdapterList.ViewHolder holder) {
        holder.checkIcon.setVisibility(View.GONE);
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }


    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public void clearAll(boolean isNotify) {
        viewHoldersList.clear();
//        viewHoldersList.addAll(fullViewHolder);
        for (AdapterList.ViewHolder rw: fullViewHolder ) {
            unhighlightView(rw);
        }

        selected.clear();
        if (isNotify) notifyDataSetChanged();
    }

    public void deleteFileNames() {
//        int size = viewHoldersList.size();

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

        for (AdapterList.ViewHolder rw: viewHoldersList ) {
            highlightView(rw);
        }

        notifyDataSetChanged();
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView listText;
        ImageView checkIcon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            listText =  itemView.findViewById(R.id.list_name);
            checkIcon = itemView.findViewById(R.id.iconCheck);

//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    if(isMultiSelectOn)
//                    {
//
//                    }
//                    Toast.makeText(v.getContext(), Integer.toString(getAbsoluteAdapterPosition()), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    isMultiSelectOn = true;
//                    if(isMultiSelectOn)
//                    {
////                      Toast.makeText(v.getContext(), listText.getText().toString(), Toast.LENGTH_SHORT).show();
//                        itemView.setBackgroundColor(1);
//                    }
//                    return true;
//                }
//            });



        }
    }


}
