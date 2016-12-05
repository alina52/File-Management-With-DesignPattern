package Util;

/**
 * Created by samsung on 2016/11/15.
 */
public class String {
    public static java.lang.String dec2bin(int dec) {
        final int PointerSize = 5;

        java.lang.String ret = Integer.toBinaryString(dec);
        int length = ret.length();
        for (int i = 0; i < PointerSize - length; i++) {
            ret = '0' + ret;
        }
        return ret;
    }
}
