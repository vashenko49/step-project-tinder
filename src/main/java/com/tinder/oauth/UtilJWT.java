package com.tinder.oauth;


import com.tinder.defaultImplementation.UserDefault;
import com.tinder.exception.ConfigFileException;
import com.tinder.start.ConfigFile;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


public final class UtilJWT {
    private static final long HOUR = 3600 * 1000;
    private static volatile UtilJWT instance;
    private final String SECRET_KEY;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private UtilJWT() throws ConfigFileException {
        final ConfigFile CONFIG_FILE = ConfigFile.getInstance();
        SECRET_KEY = CONFIG_FILE.getValueByKey("jwt.secret");
    }

    public static UtilJWT getInstance() throws ConfigFileException {
        if (instance == null) {
            synchronized (UserDefault.class) {
                if (instance == null) {
                    instance = new UtilJWT();
                }
            }
        }
        return instance;
    }

    public String createSingToken(String userID) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        Date date = new Date();
        date.setTime(date.getTime() + 3 * HOUR);
        return Jwts
                .builder()
                .signWith(signingKey, signatureAlgorithm)
                .setSubject(userID)
                .setIssuer("step-project-tinder")
                .setIssuedAt(new Date())
                .setExpiration(date)
                .compact();
    }

    public boolean verifyToken(String jwt)throws SignatureException {
        try {
            final Claims body = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(jwt).getBody();
            return body.getExpiration().after(new Date());
        }catch (ExpiredJwtException | MalformedJwtException e ){
            return false;
        }
    }

    public String getUserFromToken(String jwt) {
        final Claims body = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(jwt).getBody();
        if (body.getExpiration().after(new Date())) {
            return body.getSubject();
        }

        return null;
    }
}
