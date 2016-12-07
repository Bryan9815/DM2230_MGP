package com.sidm.mgp_2016;

import android.content.Context;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by guanhui1998 on 6/12/2016.
 */

public class Randomiser extends SurfaceView {
    Random RANDOM = new Random();

    public Randomiser(Context context) {
        super(context);
    }

    public int getRandomInt(int min, int max)
    {
        return RANDOM.nextInt(max - min + 1) + min;
    }
    public float getRandomFloat(float min, float max)
    {
        return RANDOM.nextFloat() * (max - min) + min;
    }
    public boolean getRandomBool()
    {
        return RANDOM.nextBoolean();
    }
}