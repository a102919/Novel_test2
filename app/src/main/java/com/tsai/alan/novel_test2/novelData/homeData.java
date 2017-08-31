package com.tsai.alan.novel_test2.novelData;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.Map;


/**
 * Created by Alan on 2017/7/9.
 */

public class homeData implements Serializable {
        private String id=null;
        private String title;
        private String author;
        private String classification;
        private String page = "1";
        private String url;
        private Map<Integer,String> novel = new ArrayMap<>();
        private Context context;
        private int Bookmarks;
        private int BookmarksPage;
        private boolean mark = false;



    public homeData(Context context) {
        this.context = context;
    }
    public void init(){
        novel = new ArrayMap<>();
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public int getBookmarks() {
//        int getBookmarks = Bookmarks;
//        this.Bookmarks = 0;
        return Bookmarks;
    }

    public void setBookmarks(int bookmarks) {
        Bookmarks = bookmarks;
    }

    public int getBookmarksPage() {
        return BookmarksPage;
    }

    public void setBookmarksPage(int bookmarksPage) {
        BookmarksPage = bookmarksPage;
    }

    public Context getContext() {
        return context;
    }

    public String getNovel(int page) {
        //novel = new ArrayMap<>();
        //Log.i("title","title= "+novel.isEmpty()+" "+novel.size());
        if(!novel.isEmpty()){
            if((novel.size())>page){
                page++;
                return novel.get(page);
            }
        }

        return "null";
    }
    public Map<Integer,String> getNovel(){
        return this.novel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNovel(int pager ,String novelText) {
        this.novel.put(pager,novelText);
    }
    public void setNovel(Map<Integer,String> novel) {
        this.novel = novel;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String uu = url.split("thread-")[1];
        setId(uu.split("-")[0]);
        this.url = url;
    }

    @Override
    public String toString() {
        return "homeData{" +
                "novel=" + novel +
                '}';
    }

    public void saveNovel(){
        SharedPreferences set = getContext().getSharedPreferences(getId(), 0);
        SharedPreferences.Editor editor = set.edit();
        editor.putString("id", getId());
        editor.putString("title", getTitle());
        editor.putString("url", getUrl());
        editor.putString("author", getAuthor());
        editor.putString("classification", getClassification());
        editor.putString("page", getPage());
        editor.putString("novel", javaListAndJsonInterChange());
        editor.putInt("Bookmarks", getBookmarks());
        editor.putInt("BookmarksPage", getBookmarksPage());
        editor.commit();
        //Log.i("pager","page= "+getBookmarksPage()+" "+getBookmarks());
    }
    public String javaListAndJsonInterChange() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this.novel);
        //Log.i("json","json= "+jsonString);
        return jsonString;
    }
    public void takeNovel(){
        Gson gson = new Gson();
        SharedPreferences settingsget =  getContext().getSharedPreferences(getId(), 0);
        String novelJson = settingsget.getString("novel", "null");
        if(!novelJson.equals("null")){
            this.novel = gson.fromJson(novelJson,new TypeToken<Map<Integer,String>>(){}.getType());
            //Log.i("novell","novel= "+novel.size());
        }
        int Bookmarks = settingsget.getInt("Bookmarks",0);
        int BookmarksPage = settingsget.getInt("BookmarksPage",0);
        this.Bookmarks = Bookmarks;
        this.BookmarksPage = BookmarksPage;
    }
    public void takeNovel(String id){
        Gson gson = new Gson();
        SharedPreferences settingsget =  context.getSharedPreferences(id , 0);
        this.id = settingsget.getString("id","null");
        this.title = settingsget.getString("title", "null");
        this.author = settingsget.getString("author", "null");
        this.classification = settingsget.getString("classification", "null");
        this.page = settingsget.getString("page", "null");
        this.url = settingsget.getString("url", "null");
        this.Bookmarks = settingsget.getInt("Bookmarks",0);
        this.BookmarksPage = settingsget.getInt("BookmarksPage",0);

        String novelJson = settingsget.getString("novel", "null");
        if(!novelJson.equals("null")){
            this.novel = gson.fromJson(novelJson,new TypeToken<Map<Integer,String>>(){}.getType());
            //Log.i("novell","novel= "+novel.size());
        }
    }
}
