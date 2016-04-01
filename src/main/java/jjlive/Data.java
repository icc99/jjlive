package jjlive;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * <p>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2016年03月31日
 *
 * @author Alex
 * @since dwf 1.1
 */
public class Data {
    private int length;
    private String content;

    public static String read(InputStream in) throws Exception {
        DataInputStream dataIn = new DataInputStream(in);
        Data data = new Data();
        data.length = dataIn.readInt();
        byte[] bytes = new byte[data.length];
        int hadRead = dataIn.read(bytes);
        return new String(bytes);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
