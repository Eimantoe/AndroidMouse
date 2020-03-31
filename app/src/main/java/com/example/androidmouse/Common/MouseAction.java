package com.example.androidmouse.Common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MouseAction {

    @Expose(serialize = false, deserialize = false)
    public static final int ACTION_UP = 1;
    @Expose(serialize = false, deserialize = false)
    public static final int ACTION_DOWN = 2;
    @Expose(serialize = false, deserialize = false)
    public static final int ACTION_L_CLICK = 3;
    @Expose(serialize = false, deserialize = false)
    public static final int ACTION_R_CLICK = 4;
    @Expose(serialize = false, deserialize =  false)
    public static final int ACTION_L_RELEASE = 5;


    @SerializedName("motion")
    public boolean isMouseMotion;
    @SerializedName("x")
    public float x;
    @SerializedName("y")
    public float y;
    @SerializedName("z")
    public float z;
    @SerializedName("action")
    public int action;

    public MouseAction(final float x, final float y, final float z) {
        this.isMouseMotion = true;
        this.x = x * -1;
        this.y = y;
        this.z = z;
    }

    public MouseAction(final int action) {
        this.action = action;
    }

    public String getJSON()
    {
        return new Gson().toJson(this) + "\n";
    }
}
