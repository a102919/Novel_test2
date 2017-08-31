package com.tsai.alan.novel_test2.message;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.tsai.alan.novel_test2.novelData.homeData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Alan on 2017/8/23.
 */

public class downloadMessage {
    private Activity activity;
    private homeData homeData;
    public downloadMessage(Activity activity,homeData homeData){
        this.activity = activity;
        this.homeData = homeData;
    }
    public String getUrl(int page) {
        String url = homeData.getUrl();
        String uu = url.split("thread-")[1];
        uu = uu.split("-")[0];
        String urll = "https://ck101.com/thread-"+uu+"-"+page+"-1.html";
        return urll;
    }

    public void fetchData() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document doc = null;
                    try {
                        for(int i=1;i<=Integer.parseInt(homeData.getPage());i++){
                            String URL = getUrl(i);
                            doc = Jsoup.connect(URL).get();
                            final homeData data = parseHtml(doc,i);
                            final int finalI = i;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新列表
                                    Toast.makeText(activity, finalI +"/"+data.getPage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

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
