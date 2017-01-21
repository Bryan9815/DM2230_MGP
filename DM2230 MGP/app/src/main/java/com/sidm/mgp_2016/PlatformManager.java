package com.sidm.mgp_2016;

import java.util.Vector;

/**
 * Created by guanhui1998 on 19/1/2017.
 */

public class PlatformManager {

    private class Platform
    {
        private float length;
        private Vector3 Position;
        int ScreenWidth;
        boolean Destroy = false;
        public void Init(int screenwidth)
        {
            ScreenWidth = screenwidth;
        }

        public void Update(double dt)
        {
            Position.a -= 500 * dt;
            if (Position.a < -ScreenWidth)
                Destroy = true;
        }
    }

    private int ScreenWidth;
    private Vector<Platform> PlatformList;

    public PlatformManager(int screenwidth)
    {
        ScreenWidth = screenwidth;
    }

    public void Init()
    {
        PlatformList = new Vector<Platform>();
    }

    public void Update(double dt)
    {

    }
}
