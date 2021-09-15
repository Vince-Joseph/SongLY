package com.example.songly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterPrayer extends RecyclerView.Adapter {

    private List<ModelClassPrayerItems> itemClass;

    public AdapterPrayer(List<ModelClassPrayerItems> itemClass)
    {
        this.itemClass = itemClass;
    }


    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View individualPrayerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.prayer_pdf_item, parent, false);
                LayoutPdfViewHolder layoutPdfViewHolder = new LayoutPdfViewHolder(individualPrayerView);
                layoutPdfViewHolder.pdfView.fromAsset("prayer/kurbaana.pdf")
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .load();
                return  layoutPdfViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemClass.size();
    }

    // layout viewholder class for prayer pdf
    class LayoutPdfViewHolder extends RecyclerView.ViewHolder
    {
        PDFView pdfView;
        public LayoutPdfViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.individualPrayer);
        }
    }
}
