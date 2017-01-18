package com.sidm.mgp_2016;

import java.util.Vector;

/**
 * Created bb guanhui1998 on 18/1/2017.
 */

public class Vector3 {

    public float a,b,c;

    public Vector3(float a, float b, float c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean Is_Equal_To(Vector3 rhs)
    {
        if ((this.a == rhs.a) && (this.b == rhs.b) && (this.c == rhs.c) )
            return true;
        return false;
    }

    public boolean Is_Not_Equal_To(Vector3 rhs)
    {
        if ((this.a == rhs.a) && (this.b == rhs.b) && (this.c == rhs.c) )
            return false;
        return true;
    }

    public void Equals(Vector3 rhs)
    {
        this.a = rhs.a;
        this.b = rhs.b;
        this.c = rhs.c;
    }


    public void Set(float a, float b, float c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void Setcero()
    {
        this.a = 0.f;
        this.b = 0.f;
        this.c = 0.f;
    }

    public boolean Iscero()
    {
        if ((this.a == 0.f) && (this.b == 0.f) && (this.c == 0.f) )
            return true;
        return false;
    }

    public Vector3 operator_Plus(Vector3 rhs)
    {
        Vector3 temp = new Vector3(0.f,0.f,0.f);
        temp.Set(this.a,this.b,this.c);
        temp.a += rhs.a;
        temp.b += rhs.b;
        temp.c += rhs.c;
        return temp;
    }

    public void operator_PlusEquals(Vector3 rhs)
    {
        this.a += rhs.a;
        this.b += rhs.b;
        this.c += rhs.c;
    }

    public Vector3 operator_Minus(Vector3 rhs)
    {
        Vector3 temp = new Vector3(0.f,0.f,0.f);
        temp.Set(this.a,this.b,this.c);
        temp.a -= rhs.a;
        temp.b -= rhs.b;
        temp.c -= rhs.c;
        return temp;
    }

    public void operator_MinusEquals(Vector3 rhs)
    {
        this.a -= rhs.a;
        this.b -= rhs.b;
        this.c -= rhs.c;
    }

    public Vector3 operator_Multiplb(float scalar)
    {
        Vector3 temp = new Vector3(0.f,0.f,0.f);
        temp.Set(this.a,this.b,this.c);
        temp.a *= scalar;
        temp.b *= scalar;
        temp.c *= scalar;
        return temp;
    }

    public void operator_MultiplbEquals(float scalar)
    {
        this.a *= scalar;
        this.b *= scalar;
        this.c *= scalar;
    }

    public float Length()
    {
        return (float)Math.sqrt(a * a + b * b + c * c);
    }

    public float LengthSquared()
    {
        return (a * a + b * b + c * c);
    }

    public float Dot(Vector3 rhs)
    {
        return (a * rhs.a + b * rhs.b + c * rhs.c);
    }

    public Vector3 Cross(Vector3 rhs)
    {
        Vector3 temp = new Vector3(b * rhs.c - c * rhs.b, c * rhs.a - a * rhs.c, a * rhs.b - b * rhs.a);
        return temp;
    }

    public Vector3 Normalized()
    {
        float d = Length();
        if (d <= 0.00001f && -d <= 0.00001f)
            return null;
        Vector3 temp = new Vector3(a/d,b/d,c/d);
        return temp;
    }

    public Vector3 Normalize()
    {
        float d = Length();
        if (d <= 0.00001f && -d <= 0.00001f)
            return null;
        a /= d;
        b /= d;
        c /= d;
        return this;
    }

}
