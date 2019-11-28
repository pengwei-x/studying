package 线程;

/**
 * @author: pengwei
 * @date: 2019/11/19
 */
class MyThread extends Thread {
    @Override
    public void run() {
        int n = 0;
        //中断线程
        while (! isInterrupted()) {
            n ++;
            System.out.println(n + " hello!");
        }
    }
}

