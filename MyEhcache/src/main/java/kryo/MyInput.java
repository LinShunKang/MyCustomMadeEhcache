package kryo;

import com.esotericsoftware.kryo.io.Input;

import java.io.InputStream;

/**
 * Created by LinShunkang on 7/9/17.
 */
public class MyInput extends Input {

    public MyInput() {
    }

    public MyInput(int bufferSize) {
        super(bufferSize);
    }

    public MyInput(byte[] buffer) {
        super(buffer);
    }

    public MyInput(byte[] buffer, int offset, int count) {
        super(buffer, offset, count);
    }

    public MyInput(InputStream inputStream) {
        super(inputStream);
    }

    public MyInput(InputStream inputStream, int bufferSize) {
        super(inputStream, bufferSize);
    }

    public void adjustChars() {
        if (chars.length > 2048) {
            chars = new char[chars.length / 2];
        } else if (chars.length > 1024) {
            chars = new char[1024];
        }
    }
}
