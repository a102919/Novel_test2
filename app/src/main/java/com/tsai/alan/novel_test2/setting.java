package com.tsai.alan.novel_test2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Alan on 2017/8/9.
 */

public class setting {
    public enum settingType{
        TEXTSIZE_ACTION("SIZE"),
        Bg_ACTION("BG");
        private String type;
        settingType(String type) {
            this.type = type;
        }
        @Override
        public String toString() {
            return type;
        }
    }

    public static int textSize = 20;
    public static bgModel bgm = setting.bgModel.white;
    public enum bgModel{
        blackbg(1),
        white(2);
        private int bg;
        bgModel(int bg) {
            this.bg = bg;
        }
        public int getbgModel(){
            return bg;
        }
    }
    public static void saveSetting(Context context){
        SharedPreferences set = context.getSharedPreferences("SETTING", 0);
        SharedPreferences.Editor editor = set.edit();
        editor.putInt("textSize", textSize);
        editor.putInt("bgModel", bgm.getbgModel());
        editor.commit();
    }
    public static void getSetting(Context context){
        SharedPreferences set = context.getSharedPreferences("SETTING", 0);
        textSize = set.getInt("textSize",20);
         switch (set.getInt("bgModel",1)){
             case 1:
                 bgm = bgModel.blackbg;
                 break;
             case 2:
                 bgm = bgModel.white;
                 break;
         }
    }

}
