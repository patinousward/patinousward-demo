package com.patinousward.interview;

public class Job implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(5000); //有用
            //Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ai");
    }
}

class Job2 extends Thread{
    @Override
    public void run() {
        try {
            //Thread.sleep(1000); //有用
            sleep(1000);//有用
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ai");
    }
}

class TestSleep{
    public static void main(String[] args) {
        //new Thread(new Job()).start();
        //new Job2().start();
    }
}
