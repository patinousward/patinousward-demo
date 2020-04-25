package com.patinousward.interview;

public class DaemonDemo extends Thread {

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            System.out.println(getName() + " "+ isDaemon() + " " + i);
        }
    }

    public static void main(String[] args) {

        DaemonDemo daemonDemo = new DaemonDemo();

        daemonDemo.setDaemon(true); //如果设置为false，则会打印到99才结束，这里设置为true后，打印到20左右就结束了

        daemonDemo.start();

        for(int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }

    }

}
