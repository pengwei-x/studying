package netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: pengwei
 * @date: 2019/11/27
 * netty
 * 1. readThread里面使用readSelector.selectNow();而不是使用select方法，原因是如果同一个selector的register
 * （这行代码socketChannel.register(readSelector, SelectionKey.OP_READ)）和select方法如果在不同的线程中会导致一个线程一直阻塞等待另一个线程的锁，
 *  而另一个线程由于没有新的事件到来而没有唤醒，同时所持有的锁一直释放不掉，可以将上面readTread里的selectNow改成select然后启动，
 *  并创建新的连接，查看线程堆栈信息看到该问题；
 * 2. netty和这里不同的地方是新连接到来后的register方法是在worker线程调用的，这里为了简单起见没有这
 */
public class NioServer3 {
    private ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    private Selector connectSelector = Selector.open();
    private Selector readSelector = Selector.open();

    public NioServer3() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        NioServer3 server3 = new NioServer3();
        ConnectedTask connectedTask = new ConnectedTask(server3);
        ReadTask readTask = new ReadTask(server3);

        Thread connectThread = new Thread(connectedTask);
        connectThread.setName("connect-thread");
        Thread readThread = new Thread(readTask);
        readThread.setName("read-thread");

        connectThread.start();
        readThread.start();
    }

    public void processConnected(int port) throws IOException {
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        SelectionKey selectionKey1 = serverSocketChannel.register(connectSelector, 0);

        selectionKey1.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            connectSelector.select();

            Set<SelectionKey> selectionKeys = connectSelector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    System.out.println(Thread.currentThread() + " new connected" + socketChannel.getRemoteAddress());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(readSelector, SelectionKey.OP_READ);
                }
            }
        }
    }

    public void processRead() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            try {

                int selectNow = readSelector.selectNow();
                if (selectNow <= 0) {
                    continue;
                }
            } catch (IOException ioexception) {
                System.out.println("readSelector.selectNow");
                ioexception.printStackTrace();
                continue;
            }
            Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();

                if (next.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) next.channel();
                    if (socketChannel.isConnected()) {
                        try {
                            System.out.println(Thread.currentThread() + " new data from " + socketChannel.getLocalAddress());
                            byteBuffer.clear();
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String receivedMessage =
                                    String.valueOf(charset.decode(byteBuffer));
                            System.out.println(socketChannel + ":" + receivedMessage);

                        } catch (IOException e) {
                            System.out.println("socketChannel.read(byteBuffer)");
                            try {
                                socketChannel.close();
                            } catch (IOException e1) {

                            }
                            e.printStackTrace();
                        }
                    }
                }

                iterator.remove();
            }

        }
    }
    public static class ConnectedTask implements Runnable {
        private NioServer3 server3;
        public ConnectedTask(NioServer3 server3) {
            this.server3 = server3;
        }
        @Override
        public void run() {
            try {
                server3.processConnected(8080);
            } catch (IOException e) {

            }
        }
    }

    public static class ReadTask implements Runnable {
        private NioServer3 server3;

        public ReadTask(NioServer3 server3) {
            this.server3 = server3;
        }

        @Override
        public void run() {
            server3.processRead();
        }
    }
}