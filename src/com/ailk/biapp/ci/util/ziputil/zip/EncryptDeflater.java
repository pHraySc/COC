package com.ailk.biapp.ci.util.ziputil.zip;

import com.ailk.biapp.ci.util.ziputil.extend.ZipCrypto;
import com.ailk.biapp.ci.util.ziputil.zip.EncryptZipEntry;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

public class EncryptDeflater extends FilterOutputStream {
    protected Deflater def;
    protected byte[] buf;
    private boolean closed;
    boolean usesDefaultDeflater;
    protected String password;

    public EncryptDeflater(OutputStream out, Deflater def, int size) {
        super(out);
        this.closed = false;
        this.usesDefaultDeflater = false;
        this.password = null;

        try {
            if(out == null || def == null) {
                throw new Exception("out or def is null");
            }

            if(size <= 0) {
                throw new IllegalArgumentException("buffer size <= 0");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.def = def;
        this.buf = new byte[size];
    }

    public EncryptDeflater(OutputStream out, Deflater def) {
        this(out, def, 512);
    }

    public EncryptDeflater(OutputStream out) {
        this(out, new Deflater());
        this.usesDefaultDeflater = true;
    }

    public void write(int b) throws IOException {
        byte[] buf = new byte[]{(byte)(b & 255)};
        this.write(buf, 0, 1);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if(this.def.finished()) {
            throw new IOException("write beyond end of stream");
        } else if((off | len | off + len | b.length - (off + len)) < 0) {
            throw new IndexOutOfBoundsException();
        } else if(len != 0) {
            if(!this.def.finished()) {
                this.def.setInput(b, off, len);

                while(!this.def.needsInput()) {
                    this.deflate();
                }
            }

        }
    }

    public void finish() throws IOException {
        if(!this.def.finished()) {
            this.def.finish();

            while(!this.def.finished()) {
                this.deflate();
            }
        }

    }

    public void close() throws IOException {
        if(!this.closed) {
            this.finish();
            if(this.usesDefaultDeflater) {
                this.def.end();
            }

            this.out.close();
            this.closed = true;
        }

    }

    protected void writeExtData(EncryptZipEntry entry) throws IOException {
        byte[] extData = new byte[12];
        ZipCrypto.initCipher(this.password);

        for(int i = 0; i < 11; ++i) {
            extData[i] = (byte)Math.round(256.0F);
        }

        extData[11] = (byte)((int)(entry.time >> 8 & 255L));
        extData = ZipCrypto.encryptMessage(extData, 12);
        this.out.write(extData, 0, extData.length);
    }

    protected void deflate() throws IOException {
        int len = this.def.deflate(this.buf, 0, this.buf.length);
        if(len > 0) {
            if(this.password != null) {
                byte[] crypto = ZipCrypto.encryptMessage(this.buf, len);
                this.out.write(crypto, 0, len);
                return;
            }

            this.out.write(this.buf, 0, len);
        }

    }
}
