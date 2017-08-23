package com.tsai.alan.novel_test2.message;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tsai.alan.novel_test2.Adapter.HomeAdapter;
import com.tsai.alan.novel_test2.novelData.homeData;
import com.tsai.alan.novel_test2.model.readModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Alan on 2017/7/12.
 */

public class readHomeMesage implements MakeData{
    private static int page=1;
    private HomeAdapter homeAdapter;
    private Activity activity;
    private int listSize;
    private ProgressBar progressBar;

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public readHomeMesage(HomeAdapter homeAdapter, ProgressBar progressBar, Activity activity) {
        this.homeAdapter = homeAdapter;
        this.activity = activity;
        this.progressBar = progressBar;
        fetchData(readModel.initRead);
    }

    /**
 * 请求数据
 */
    public void fetchData(final readModel model ) {
    new Thread(new Runnable() {
        @Override
        public void run() {
            Document doc = null;
            try {
                String URL = getURL(model);
                doc = Jsoup.connect(URL).get();
                final List<homeData> datas = parseHtml(doc);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        //更新列表
                        parseDataFinish(datas,model);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}
    private void parseDataFinish(List<homeData> data, readModel model ) {
        if (readModel.initRead.equals(model)) {
            homeAdapter.refresh(data);
            progressBar.setVisibility(View.GONE);
        } else {
            homeAdapter.loadmore(data);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 解析HTML
     *
     * @param document
     * @return
     */
    private List<homeData> parseHtml(Document document) {
        Elements elements = document.select("#threadlisttableid>tbody");
        List<homeData> list = new ArrayList<>();
        homeData data;
        for (Element element : elements) {
            String title="";
            String author="";
            String classification="";
            String page=" ";

            data = new homeData(activity.getApplicationContext());
            String a= element.select(" tr > th > div.titleBox > div > a > h2").text();
            String pattern = ".*\\[.*";
            boolean isMatch = Pattern.matches(pattern, a);
            if(isMatch){
                a=(a.split("\\["))[1];
                String[] b=a.split("\\]");
                classification = b[0];

                String[] c=b[1].split(" 作者");
                if(c[0]!=null){
                    title = c[0];
                }
                if(c.length>1){
                    char ccc = c[1].charAt(1);
                    String cc[] = c[1].split(Character.toString(ccc));
                    author = (cc[1].split("\\(連"))[0];
                }
                data.setTitle(title);
                data.setAuthor(author);
                data.setClassification(classification);
                String url= element.select(" tr > th > div.titleBox > div > a").attr("href");
                data.setUrl(url);
                Elements pageE= element.select("tr > th > div.postInfo > span.tps >a");

                String[] page2 = pageE.text().split(" ");
                String page3 = page2[page2.length-1];

                //Log.i("bbb","size= "+pageE.size()+" "+pageE.text());
                //page = pageE.last().text();
                if(page3!=null){
                    data.setPage(page3);
                }
                list.add(data);
            }
        }
        setListSize(list.size());
        return list;
    }

    public static String getURL(readModel model) {
        if(readModel.initRead.equals(model)){
            page=1;
        }else {
            page++;
        }
        String URL = "https://ck101.com/forum-237-"+page+".html";
        return URL;
    }


}
