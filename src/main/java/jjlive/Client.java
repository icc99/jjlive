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
    private VirtualMachine vm;

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
        this.vm = VirtualMachine.attach(pid+"");
        this.port = this.getFreePort();
        this.vm.loadAgent(agentJar,  agentJar  + " " + port);
        this.clientSock = new Socket("127.0.0.1", this.port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
               if (Client.this.vm != null) {
                   try {
                       Client.this.vm.detach();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }
        });
    }

    private void jjps() {
        listJvmProcesses();
    }
    private void exit() throws IOException {
        this.vm.detach();
        System.exit(0);
    }

    public void openREPL() throws SecurityException, IOException {
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
                        System.out.print(ret);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.vm.detach();
        }
    }

    private String callServer(String command) throws Exception {
        if (command == null || command.matches("\\s*")) {
            return command;
        }
        InputStream in = clientSock.getInputStream();
        OutputStream out = clientSock.getOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(command.getBytes().length);
        dataOut.write(command.getBytes());
        out.flush();
        String ret = Data.read(in);
        return ret;
    }

}
