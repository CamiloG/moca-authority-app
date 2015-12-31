package camilog.authorityapp;

import java.math.BigInteger;

public class PrivateKey {

    BigInteger n, l, w, v, si, i;
    BigInteger[] vi;

    public PrivateKey(BigInteger[][] values) {
        vi = values[1];
        n = values[0][0];
        l = values[0][1];
        w = values[0][2];
        v = values[0][3];
        si = values[0][4];
        i = values[0][5];
    }

}
