package ro.uti.ran.core.utils;

import java.io.*;
import java.util.zip.*;

/**
 * Created with IntelliJ IDEA.
 * User: mihai.vaduva
 * Date: 11/25/13
 * Time: 2:21 PM
 * <p/>
 * Uses Java Inflater/Deflater API - ZLIB
 */
public final class ZipUtil {
    private static final String XML_EXTENSION = ".xml";

    public static byte[] compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        byte[] output = outputStream.toByteArray();
        return output;
    }

    public static byte[] decompress(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        byte[] output = outputStream.toByteArray();
        return output;
    }


    /**
     * Verifica daca fluxul este format zip.
     *
     * http://www.java2s.com/Code/Java/File-Input-Output/DeterminewhetherafileisaZIPFile.htm
     * @param is
     * @return
     * @throws IOException
     */
    public static boolean isZipFile(InputStream is) throws IOException {
        if (!is.markSupported()) {
            is = new BufferedInputStream(is);
        }
        is.mark(1000);
        boolean isZip = false;
        try {
            int ch1 = is.read();
            int ch2 = is.read();
            int ch3 = is.read();
            int ch4 = is.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException();

            int test = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));;

            isZip = test == 0x504b0304;
        } catch (Throwable th) {
            isZip = false;
        } finally {
            is.reset();
        }
        return  isZip;
    }

    public static boolean checkZipFileContent(InputStream inputStream, String extension) throws IOException {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry ze = zipInputStream.getNextEntry();
            while (ze != null) {
                if (ze.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
                ze = zipInputStream.getNextEntry();
            }
            return false;
    }
    public static boolean checkZipFileContentForXml(InputStream inputStream) throws IOException {
        return checkZipFileContent(inputStream,XML_EXTENSION);
    }

    /**
     *
     * @param inputStream
     * @param extension - String - vaoloare este de forma '.xml'. Poate fi null.
     * @return Nr de fisiere cu extensia specificata ca si parametru.
     *          In cazul in care extensia este null se va numara toare intrarile in arhiva.
     * @throws IOException
     */
    public static int countZipFileContent(InputStream inputStream, String extension) throws IOException{
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        int nrElemente = 0;
        ZipEntry ze = zipInputStream.getNextEntry();
        while (ze != null) {
            if(extension != null) {
                if (ze.getName().toLowerCase().endsWith(extension)) {
                    return nrElemente++;
                }
            } else {
                nrElemente++;
            }
            ze = zipInputStream.getNextEntry();
        }
        return nrElemente;
    }
    public static int countZipFileContentForXml(InputStream inputStream) throws IOException {
        return countZipFileContent(inputStream,XML_EXTENSION);
    }
}
