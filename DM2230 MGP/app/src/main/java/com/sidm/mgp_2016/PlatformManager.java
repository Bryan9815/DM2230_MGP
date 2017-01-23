package com.sidm.mgp_2016;

import android.content.Context;

import java.util.Vector;

/**
 * Created by guanhui1998 on 19/1/2017.
 */

public class PlatformManager {

    GamePanelSurfaceView GamePanel;
    public class Platform
    {
        public float length;
        public Vector3 Position;
        private int ScreenWidth,ScreenHeight;
        private int offset;
        public boolean Destroy = false;

        public Platform()
        {}

        public void Init(int screenwidth, int screenheight,float length)
        {

            Randomiser rand = new Randomiser();
            ScreenWidth = screenwidth;
            ScreenHeight = screenheight;
            this.length = length;
            Destroy = false;
            offset = ScreenWidth/4;
            Position = new Vector3((float)ScreenWidth + offset, (ScreenHeight/2) + rand.getRandomFloat((float)-ScreenHeight/4,(float)ScreenHeight/4),0.f );
        }

        public boolean Update(double dt,int player_x,int player_y)
        {
            if (Position.a < -ScreenWidth)
            {
                Destroy = true;
                return false;
            }
            Position.a -= 500 * dt;

            if (player_y <= (int)Position.b - 5 && player_y > (int)Position.b - 25)
            {
                if (player_x >= Position.a - length/2 && player_x <= Position.a + length/2)
                {
                    player_y = (int)Position.b;
                    return true;
                }
            }
            return false;
        }
    }

    private int ScreenWidth,ScreenHeight;
    public Vector<Platform> PlatformList;
    private float Platform_spawn_timer;
    public float Length;

    public PlatformManager(int screenwidth, int screenheight, float length)
    {
        ScreenWidth = screenwidth;
        ScreenHeight = screenheight;
        Length = length;
    }

    public void Init()
    {
        PlatformList = new Vector<Platform>();
        Platform_spawn_timer = 0.f;
    }

    public boolean Update(double dt, int player_x,int player_y)
    {
        if (Platform_spawn_timer < 1.5f)
            Platform_spawn_timer += dt;
        else
        {
            Platform_spawn_timer = 0.f;
            Platform temp = new Platform();
            temp.Init(ScreenWidth,ScreenHeight, Length);
            PlatformList.add(temp);
        }
        boolean temp = false;

        for (int i = 0; i < PlatformList.size(); i++)
        {
            if (PlatformList.get(i).Update(dt,player_x,player_y))
            {
                temp = true;
            }
            if (PlatformList.get(i).Destroy)
            {
                PlatformList.remove(i);
                //continue;
            }
        }
        return temp;
    }

}
