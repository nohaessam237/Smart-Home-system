package com.example.smarthomesystem;

public class recycler_list {
    private Integer img;
    private String txt;


    public recycler_list(Integer img, String txt) {
        this.img = img;
        this.txt = txt;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
