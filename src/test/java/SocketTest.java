/*
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

*/
/**
 * <p>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2016年03月31日
 *
 * @author Alex
 * @since dwf 1.1
 *//*

public class SocketTest {
    @Test
    public void testSocket() throws Exception {
        int port = 10009;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket socket = serverSocket.accept();
                    while (true) {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        DataInputStream dataIn = new DataInputStream(in);
                        DataOutputStream dataOut = new DataOutputStream(out);
                        int length = dataIn.readInt();
                        byte[] bytes = new byte[length];
                        dataIn.read(bytes);
                        System.out.println(new String(bytes));
                        dataOut.writeInt(bytes.length);
                        dataOut.write(bytes);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
        Socket socket = new Socket("127.0.0.1", port);
        while (true) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            DataInputStream dataIn = new DataInputStream(in);
            DataOutputStream dataOut = new DataOutputStream(out);
            String hello = "hello";
            dataOut.writeInt(hello.length());
            dataOut.write(hello.getBytes());
            dataOut.flush();
            int length = dataIn.readInt();
            byte[] bytes = new byte[length];
            int n = dataIn.read(bytes);
            System.out.println(new String(bytes)+"~~~~~~~~~~~~~~~~~~"+n);
        }
    }
}
*/
