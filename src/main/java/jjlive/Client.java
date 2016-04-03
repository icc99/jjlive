package jjlive;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

public class Client {
    private static final InetAddress LOCALHOST = getLocalHostAddress();
    private static Console cnsl = System.console();
    private int port;
    private Socket clientSock;

    private static InetAddress getLocalHostAddress() {
        try {
            return InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    public int getFreePort() throws Exception {
        ServerSocket server = new ServerSocket(0, 0, LOCALHOST);
        int port = server.getLocalPort();
        server.close();
        return port;
    }

    private void listJvmProcesses() {
        System.out.println("JVM processes:");
        System.out.println("#pid\tDisplay name");

        Iterator<VirtualMachineDescriptor> it = VirtualMachine.list().iterator();
        while (it.hasNext()) {
            VirtualMachineDescriptor vmd = it.next();
            System.out.println(vmd.id() + "\t" + vmd.displayName());
        }
    }

    private void attach(String command) throws Exception {
        String param = command.split("\\s")[1];
        param = param.trim();
        doAttach(Integer.parseInt(param));
    }

    private void doAttach(int pid) throws Exception {
        String agentJar = "jjlive";
        agentJar = Classpath.findJar(new String[]{"."}, agentJar);
        System.out.println(agentJar);
        final VirtualMachine vm = VirtualMachine.attach(pid+"");
        this.port = this.getFreePort();
        vm.loadAgent(agentJar,  agentJar  + " " + port);
        this.clientSock = new Socket("127.0.0.1", this.port);
    }

    private void jjps() {
        listJvmProcesses();
    }
    private void exit() {
        System.exit(0);
    }

    public void openREPL() throws SecurityException {
        try {
            System.out.println("jjlive started");
            String command = "";
            while (!command.equals(":exit")) {
                command = cnsl.readLine("jjlive>>");
                switch (command) {
                    default:
                        command = command.trim();
                        if (command.startsWith(":"))  {
                            command = command.substring(1);
                            String cmd = command.split(" ")[0];
                            Method method = null;
                            try {
                                method = Client.class.getDeclaredMethod(cmd);
                            } catch (Exception e) {
                                method = Client.class.getDeclaredMethod(cmd, String.class);
                            }
                            Class<?>[] paramTypes = method.getParameterTypes();
                            method.setAccessible(true);
                            if (paramTypes.length == 1 && paramTypes[0] == String.class) {
                                method.invoke(this, command);
                            } else {
                                method.invoke(this);
                            }
                            break;
                        }
                        String ret = callServer(command);
                        System.out.println(ret);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String callServer(String command) throws Exception {
        InputStream in = clientSock.getInputStream();
        OutputStream out = clientSock.getOutputStream();
        int length = command.length();
        DataInputStream dataIn = new DataInputStream(in);
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(command.getBytes().length);
        dataOut.write(command.getBytes());
        out.flush();
        String ret = Data.read(in);
        System.out.println("ret:");
        return ret;
    }

    public static void connectToRepl(int port) throws Exception {
        final Socket client = new Socket(LOCALHOST, port);
        final InputStream in = client.getInputStream();
        final OutputStream out = client.getOutputStream();

        System.out.println("[Scalive] Attached to remote process at port " + port);

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStreamReader reader = new InputStreamReader(in);
                boolean closed = false;
                while (!closed) {
                    try {
                        int i = reader.read();
                        closed = i < 0;
                        if (!closed) System.out.print((char) i);
                    } catch (Exception e) {
                        closed = true;
                    }
                }

                System.out.println("[Scalive] Connection to remote process closed");
                System.exit(0);
            }
        }).start();

        boolean closed = false;
        while (!closed) {
            try {
                String line = cnsl.readLine("jjlive>>");
                out.write(line.getBytes());
                out.write('\n');
                out.flush();
            } catch (Exception e) {
                closed = true;
            }
        }

        client.close();
    }
}
