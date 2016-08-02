package com.lmn.Arbiter_Android.BaseClasses;


/* Class for creating image data */
public class Image{

    private String path;  //image url
    private String name; //image name
    private float left; //boundary - left
    private float right; //boundary - right
    private float top; //boundary - top
    private float bottom; //boundary - bottom

    public Image()
    {

    }

    public Image(Image item){
        this.path = item.getPath();
        this.name = item.getName();
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLeft(float left)
    {
        this.left = left;
    }

    public void setRight(float right)
    {
        this.right = right;
    }

    public void setTop(float top) { this.top = top;}

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
    }

    public String getPath()
    {
        return path;
    }

    public String getName()
    {
        return name;
    }

    public float getLeft()
    {
        return left;
    }

    public float getRight()
    {
        return right;
    }

    public float getTop()
    {
        return top;
    }

    public float getBottom()
    {
        return bottom;
    }

}
