package cn.joker.util;

import cn.joker.namespace.StdName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 生成一个MD5码
 *
 * @author Administrator
 */
public class MD5Util {
    MD5Util(){
        throw new IllegalStateException(StdName.UTILCLASS);
    }
    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    public static String encodePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return password;
        }

        try {
            return getMD5(password.getBytes(StdName.UTF));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String getMD5(byte[] source) {
        String s = null;
        char []hexDigits = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte []tmp = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char []str = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return s;
    }

    public static String MD5(String inputStr) {
        // 用于加密的字符
        char []md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            // 使用utf-8字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = inputStr.getBytes(StdName.UTF);

            // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char []str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) { // i = 0
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }

            // 返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }

    public static String encryption(String plain) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain.getBytes(StdName.UTF));
            byte []b = md.digest();

            int i;

            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return re_md5;
    }
}