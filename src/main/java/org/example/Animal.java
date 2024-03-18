package org.example;

import java.util.Random;

public abstract class Animal {


    private boolean Gender;
    private int X;
    private int Y;
    private int Speed;

    public Animal(boolean gender, int speed) {
        Random rand = new Random();
        Gender = gender;
        X = rand.nextInt(500);
        Y = rand.nextInt(500);
        Speed = speed;
    }

    public boolean getGender() {
        return Gender;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }


    public void setGender(boolean gender) {
        Gender = gender;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }

    public void moveAnyWhere() {
        Random rand = new Random();
        int direction = rand.nextInt(4); // 0: Yukarı, 1: Aşağı, 2: Sağ, 3: Sol

        switch (direction) {
            case 0:
                setY(getY()+getSpeed());
                break;
            case 1:
                setY(getY()-getSpeed());
                break;
            case 2:
                setX(getX()+getSpeed());
                break;
            case 3:
                setX(getY()-getSpeed());
                break;
        }


        setX(Math.max(0, Math.min(getX(), 499)));
        setY(Math.max(0, Math.min(getY(), 499)));
    }

}


