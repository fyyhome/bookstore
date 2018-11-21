package com.WebToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JJwtUtil {

    protected static final String JJWT_KEY = "javaweb_bookstore_be_token_key_hs256";
    protected static final long JJWT_TTL = 60 * 60 * 1000; // ms

    public static SecretKey generalKey() {
        byte[] encodeKey = JJWT_KEY.getBytes();
        SecretKey Key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
        return Key;
    }

    public static String createJWT(String id, String user) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMIllis = System.currentTimeMillis();
        Date now = new Date(nowMIllis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(user)
                .signWith(key, signatureAlgorithm);
        if (JJWT_TTL > 0) {
            long expMillis = nowMIllis + JJWT_TTL;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseJJWT(String jjwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jjwt)
                .getBody();
        return claims;
    }
}
