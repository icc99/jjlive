package jjlive;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.io.*;
import java.net.Socket;

public class Server {
    // Load this Scala version if Scala has not been loaded in the target process
    private static  NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    private ScriptEngine engine = factory.getScriptEngine("-scripting");
    private DataOutputStream dataOut;

    private String eval(String source) throws Exception {
        String ret = "";
        try {
            Object obj = engine.eval(source);
            if (obj == null) {
                ret = "";
            } else {
                ret = obj + "\n";
            }
            return ret;
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            return errorMsg + "\n";
        }
    }

    public void serve(Socket client, String jar) throws Exception {
        InputStream  in  = client.getInputStream();
        OutputStream out = client.getOutputStream();

        while (true) {
            try {
                String source = Data.read(in);
                String ret = this.eval(source);
                this.dataOut = new DataOutputStream(out);
                byte[] bytes = ret.getBytes("UTF-8");
                dataOut.writeInt(bytes.length);
                dataOut.write(bytes);
                out.flush();
            } catch (Exception e) {
                byte[] exceptionData = getStackTrace(e).getBytes();
                this.dataOut.writeInt(exceptionData.length);
                this.dataOut.write(exceptionData);
            }
        }

    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
