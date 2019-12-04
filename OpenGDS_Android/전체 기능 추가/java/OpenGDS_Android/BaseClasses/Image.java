package com.lmn.OpenGDS_Android.BaseClasses;

import java.util.ArrayList;

/**
 * Image 객체에 이미지의 이름 및 경로, 경계값 저장
 *
 * @author JiJungKeun
 * @version 1.1 2016/09/12
 */
public class Image {

    private String path;  //image url
    private String name; //image name
    private float left; //boundary - left
    private float right; //boundary - right
    private float top; //boundary - top
    private float bottom; //boundary - bottom
    private ArrayList<String> imageBuildOptions = new ArrayList<String>();
    private boolean imageOpacity = false; //image opacity

    /**
     * @author JiJungKeun
     */
    public Image() {
    }

    /**
     * Set image url and image name
     *
     * @param item Image
     * @author JiJungKeun
     */
    public Image(Image item) {
        this.path = item.getPath();
        this.name = item.getName();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void setDrawBuildOption() {
        imageBuildOptions.add("DRAW");
    }

    public void setBoundaryBuildOption() {
        imageBuildOptions.add("BOUNDARY");
    }

    public void setAOIBuildOption() {
        imageBuildOptions.add("AOI");
    }

    public void setImageOpacity() {
        imageOpacity = true;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public ArrayList<String> getImageBuildOptions() {
        return imageBuildOptions;
    }

    public boolean getImageOpacity() {
        return imageOpacity;
    }

}
