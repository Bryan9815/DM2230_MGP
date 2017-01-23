package com.sidm.mgp_2016;

import android.content.Context;
import android.icu.text.MessagePattern;

import java.util.Vector;

/**
 * Created by guanhui1998 on 5/12/2016.
 */

public class ParticleSystem
{
    public Vector<Particle> ParticleList;
    Randomiser rand;

    public ParticleSystem() {
        ParticleList = new Vector<Particle>();
        rand = new Randomiser();
    }

    public void Init()
    {

    }

    public void Update(double dt)
    {
        for (int i = 0; i < ParticleList.size(); i++)
        {
            ParticleList.get(i).Update(dt);
            if (ParticleList.get(i).Destroy)
                ParticleList.remove(i);
        }
    }

    public void Add(float X, float Y, float spd, float lifetime)
    {
        ParticleList.add(new Particle(X,Y,spd,lifetime,rand));
    }

    public class Particle
    {
        Vector3 Position;
        Vector3 Direction;
        int rotate;
        float speed;
        boolean Destroy = false;
        float lifetime = 0.f;
        Particle(float X, float Y,float spd,float lifetime, Randomiser rand)
        {
            Position = new Vector3(X,Y,0.f);
            speed = spd;
            Direction = new Vector3(rand.getRandomFloat(-1.f,1.f),rand.getRandomFloat(-1.f,1.f),0.f);
            this.lifetime = lifetime;
        }

        void Update(double dt)
        {
            if (lifetime <= 0)
                Destroy = true;
            else
                lifetime -= dt;
            Position.operator_PlusEquals(Direction.operator_Multiply(speed * (float)dt));
        }
    }

}
