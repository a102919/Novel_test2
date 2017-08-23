package com.tsai.alan.novel_test2.message;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tsai.alan.novel_test2.novelData.homeData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Alan on 2017/7/15.
 */

public class readNovelMesage {
    private homeData homeData;
    private Activity activity;
    private ProgressBar progressBar;
    private TextView textView;
    private ScrollView scrollView;
    /**
     * 请求数据
     */

    public readNovelMesage(homeData homeData, TextView textView, ProgressBar progressBar, ScrollView scrollView, Activity activity){
        this.homeData = homeData;
        this.textView = textView;
        this.activity = activity;
        this.progressBar = progressBar;
        this.scrollView = scrollView;

    }

    public String getUrl(int page) {
        String url = homeData.getUrl();
        String uu = url.split("thread-")[1];
        uu = uu.split("-")[0];
        String urll = "https://ck101.com/thread-"+uu+"-"+page+"-1.html";
        return urll;
    }

    public void fetchData(final int page) {
        if(homeData.getNovel(page-1)=="null"){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document doc = null;
                    try {
                        String URL = getUrl(page);
                        doc = Jsoup.connect(URL).get();
                        final homeData data = parseHtml(doc,page);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                //更新列表
                                parseDataFinish(data,page);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            parseDataFinish(homeData,page);
        }

    }
    private void parseDataFinish(final homeData data, int page) {
        page--;
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,data.getBookmarks());
            }
        });
        textView.setText(page+" "+data.getNovel(page));
        progressBar.setVisibility(View.GONE);

    }

    /**
     * 解析HTML
     *
     * @param document
     * @return
     */
    private homeData parseHtml(Document document,int page) {
        Elements elements = document.select(".t_f");
        String novel="";
        for (Element element : elements) {
            String[] novel_text = element.text().split("。");
            for (String n:novel_text){
                if(novel==""){
                    novel = n;
                }else {
                    novel = novel+"\n\n"+n+"。";
                }
            }
            novel+="\n\n";
            }
        homeData.setNovel(page,novel);
        return homeData;
    }
}
