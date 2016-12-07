package com.sidm.mgp_2016;

import android.content.Context;

/**
 * Created by guanhui1998 on 5/12/2016.
 */

public class ParticleSystem extends Randomiser {
    public ParticleSystem(Context context) {
        super(context);
    }

    public class Particle
    {
        int Position_X, Position_Y;
        int Dir_x,Dir_y;
        int rotate;
        float speed;
        Particle(int X, int Y,float spd)
        {
            Position_X = X;
            Position_Y = Y;

            speed = spd;
        }

        void Update(double dt)
        {

        }
    }

}
