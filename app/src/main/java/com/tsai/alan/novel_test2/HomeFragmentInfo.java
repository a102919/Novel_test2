package com.tsai.alan.novel_test2;

/**
 * Created by Alan on 2017/7/22.
 */

public class HomeFragmentInfo {
    private  String title;

    private Class fragment;

    public HomeFragmentInfo(String title, Class fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
