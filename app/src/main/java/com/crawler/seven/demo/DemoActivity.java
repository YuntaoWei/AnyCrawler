package com.crawler.seven.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crawler.seven.R;
import com.crawler.seven.demo.GlideApp;
import com.seven.crawler.spider.CrawlTask;
import com.seven.crawler.spider.Crawler;
import com.seven.crawler.spider.downloader.PicDownLoader;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    Crawler crawler;
    CrawlTask task;
    RecyclerView listView;
    List<String> imgUrls = new ArrayList<>();
    private final int LOAD_LIMITE = 50;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);

        listView = findViewById(R.id.imgList);

        crawler = new Crawler.Builder()
                .setCrawlerUrl("http://www.rosi44.com")
                .crawlPic()
                .setCrawlInterval(3000)
                .setCrawlThreadCount(4)
                .build();

        task = CrawlTask.getInstance();
        task.setCrawlerConfiguration(crawler);
        task.setDownLoader(new PicDownLoader() {
            @Override
            public void startDownLoad(List<String> urls, List<String> title, String url) {
                imgUrls.addAll(urls);
                if(imgUrls.size() > LOAD_LIMITE)
                    task.pause();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(DemoActivity.this.getApplicationContext()).inflate(R.layout.item, null);
                return new MyViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                GlideApp.with(DemoActivity.this)
                        .load(imgUrls.get(i))
                        .centerCrop()
                        .placeholder(R.mipmap.place_holder)
                        .into(((MyViewHolder)viewHolder).img);
            }

            @Override
            public int getItemCount() {
                return imgUrls.size();
            }
        };


        listView.setAdapter(mAdapter);
    }

    public void onButtonClick(View view) {
        task.start();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        task.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        task.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.stop();
    }
}
