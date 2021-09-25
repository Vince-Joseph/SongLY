package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

/**
 *
 * This activity is used to display the individual karososa prayers, selected from FragmentExtra.
 * Bottom nav is disabled for this activity
 *
 */
public class KarososaViewActivity extends AppCompatActivity {
    PDFView pdfView;
    ImageView karososaClose;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karososa_view);


        karososaClose = findViewById(R.id.karososaClose); // close button

        intent = getIntent();
        intent.getExtras();
        int pageStart = Integer.parseInt(intent.getStringExtra("pageStart"));
        int pageEnd = Integer.parseInt(intent.getStringExtra("pageEnd"));
        pdfView = findViewById(R.id.prayerPdfKarososaView);

        switch (pageEnd - pageStart)
        {
            case 5:
                pdfView.fromAsset("prayer/karosusa.pdf")
                        .pages(pageStart-1, pageStart,pageStart+1,pageStart+2, pageStart+3, pageStart+4)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .nightMode(false)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
                break;
            case 6:
                pdfView.fromAsset("prayer/karosusa.pdf")
                        .pages(pageStart-1, pageStart,pageStart+1,pageStart+2,pageStart+3,
                                pageStart+4, pageStart+5)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .nightMode(false)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
                break;
            case 7:
                pdfView.fromAsset("prayer/karosusa.pdf")
                        .pages(pageStart-1, pageStart,pageStart+1,pageStart+2,pageStart+3,
                                pageStart+4, pageStart+5, pageStart+6)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .nightMode(false)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
                break;
            case 8:
                pdfView.fromAsset("prayer/karosusa.pdf")
                        .pages(pageStart-1, pageStart,pageStart+1,pageStart+2,pageStart+3,
                                pageStart+4, pageStart+5, pageStart+6, pageStart+7)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .nightMode(false)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
                break;
            case 9:
                pdfView.fromAsset("prayer/karosusa.pdf")
                        .pages(pageStart-1, pageStart,pageStart+1,pageStart+2,pageStart+3,
                                pageStart+4, pageStart+5, pageStart+6, pageStart+7, pageStart+8)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .nightMode(false)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
                break;
        }


        karososaClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}