package br.com.srgenex.utils.serializer;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unused")
public class Base64Serializer {

    @SneakyThrows
    public static String serialize(Object object) {
        if(object == null) return null;
        ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = null;
        try {
            gzipOut = new GZIPOutputStream(new Base64OutputStream(byteaOut));
            gzipOut.write(new Gson().toJson(object).getBytes(StandardCharsets.UTF_8));
        } finally {
            if (gzipOut != null) try {
                gzipOut.close();
            } catch (IOException ignored) {
            }
        }
        return byteaOut.toString();
    }

    @SneakyThrows
    public static <T> T deserialize(String string, Type type) {
        if(string == null) return null;
        ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
        GZIPInputStream gzipIn = null;
        try {
            gzipIn = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8))));
            for (int data; (data = gzipIn.read()) > -1; ) byteaOut.write(data);
        } finally {
            if (gzipIn != null) try {
                gzipIn.close();
            } catch (IOException ignored) {
            }
        }
        return new Gson().fromJson(byteaOut.toString(), type);
    }

}
