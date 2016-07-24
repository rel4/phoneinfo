package com.example.phoneinfo.phoneinfo;

import java.io.IOException;

public class Base64 {
    private static  char[] ALPHABET;
    private static int[] valueDecoding;

    static {
        Base64.ALPHABET = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        Base64.valueDecoding = new int[128];
        int v0;
        for(v0 = 0; v0 < Base64.valueDecoding.length; ++v0) {
            Base64.valueDecoding[v0] = -1;
        }

        for(v0 = 0; v0 < Base64.ALPHABET.length; ++v0) {
            Base64.valueDecoding[Base64.ALPHABET[v0]] = v0;
        }
    }

    private Base64() {
        super();
    }

    public static byte[] decode(String encoded) throws IOException {
        return Base64.decode(encoded, 0, encoded.length());
    }

    public static byte[] decode(String encoded, int offset, int length) throws IOException {
        int v1 = 61;
        if(length % 4 != 0) {
            throw new IOException("Base64   string   length   is   not   multiple   of   4");
        }

        int v5 = length / 4 * 3;
        if(encoded.charAt(offset + length - 1) == v1) {
            --v5;
            if(encoded.charAt(offset + length - 2) == v1) {
                --v5;
            }
        }

        byte[] v4 = new byte[v5];
        int v6 = 0;
        for(v5 = 0; v6 < length; v5 += 3) {
            Base64.decodeQuantum(encoded.charAt(offset + v6), encoded.charAt(offset + v6 + 1), encoded
                    .charAt(offset + v6 + 2), encoded.charAt(offset + v6 + 3), v4, v5);
            v6 += 4;
        }

        return v4;
    }

    private static void decodeQuantum(char in1, char in2, char in3, char in4, byte[] out, int outOffset)
            throws IOException {
        int v7 = 61;
        int v2 = 0;
        int v3 = 0;
        int v4 = 0;
        int v0 = Base64.valueDecoding[in1 & 127];
        int v1 = Base64.valueDecoding[in2 & 127];
        if(in4 == v7) {
            v4 = 1;
            if(in3 == v7) {
                ++v4;
            }
            else {
                v2 = Base64.valueDecoding[in3 & 127];
            }
        }
        else {
            v2 = Base64.valueDecoding[in3 & 127];
            v3 = Base64.valueDecoding[in4 & 127];
        }

        if(v0 >= 0 && v1 >= 0 && v2 >= 0 && v3 >= 0) {
            out[outOffset] = ((byte)(v0 << 2 & 252 | v1 >>> 4 & 3));
            if(v4 < 2) {
                out[outOffset + 1] = ((byte)(v1 << 4 & 240 | v2 >>> 2 & 15));
                if(v4 < 1) {
                    out[outOffset + 2] = ((byte)(v2 << 6 & 192 | v3 & 63));
                }
            }

            return;
        }

        throw new IOException("Invalid   character   in   Base64   string");
    }

    public static String encode(byte[] data) {
        return Base64.encode(data, 0, data.length);
    }

    public static String encode(byte[] data, int offset, int length) {
        char[] v0 = new char[(length + 2) / 3 * 4];
        int v2 = 0;
        int v1;
        for(v1 = 0; v1 < v0.length; v1 += 4) {
            Base64.encodeQuantum(data, offset + v2, length - v2, v0, v1);
            v2 += 3;
        }

        return new String(v0);
    }

    private static void encodeQuantum(byte[] in, int inOffset, int len, char[] out, int outOffset) {
        int v1;
        char v7 = '=';
        int v0 = in[inOffset];
        out[outOffset] = Base64.ALPHABET[v0 >>> 2 & 63];
        if(len > 2) {
            v1 = in[inOffset + 1];
            int v2 = in[inOffset + 2];
            out[outOffset + 1] = Base64.ALPHABET[(v0 << 4 & 48) + (v1 >>> 4 & 15)];
            out[outOffset + 2] = Base64.ALPHABET[(v1 << 2 & 60) + (v2 >>> 6 & 3)];
            out[outOffset + 3] = Base64.ALPHABET[v2 & 63];
        }
        else if(len > 1) {
            v1 = in[inOffset + 1];
            out[outOffset + 1] = Base64.ALPHABET[(v0 << 4 & 48) + (v1 >>> 4 & 15)];
            out[outOffset + 2] = Base64.ALPHABET[v1 << 2 & 60];
            out[outOffset + 3] = v7;
        }
        else {
            out[outOffset + 1] = Base64.ALPHABET[v0 << 4 & 48];
            out[outOffset + 2] = v7;
            out[outOffset + 3] = v7;
        }
    }
}

