package cas.server;
 
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Verification;

import database.DB;
import domains.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
 
/**
 * JWTUtils工具类，生成jwt和解析jwt
 * JSON WEB TOKEN 结构组成：
 * (1)Header(头部)：包含加密算法，通常直接使用 HMAC SHA256
 * (2)Payload(负载)：存放有效信息，比如消息体、签发者、过期时间、签发时间等
 * (3)Signature(签名)：由header(base64后的)+payload(base64后的)+secret(秘钥)三部分组合，然后通过head中声明的算法进行加密
 * @author sixmonth
 * @date 2019年3月20日
 *
 */
public class JWTUtils {
	
	private static String SECRETKEY = "KJHUhjjJYgYUllVbXhKDHXhkSyHjlNiVkYzWTBac1Yxkjhuad";
	private static String WrongToken="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ODM1NTk5ZmRhMmI0MWEwYjlmYmQ4NWNiZDE0OGUwNCIsImlhdCI6MTYwMzM1NDI1NCwic3ViIjoiMTIzIiwiZXhwIjoxNjAzMzU0MjY0fQ.KDPvrsCCFEq9S1dkRSzRYR4OVbTj_4q3qFG006PBJ_I";
	/**
	 * 由字符串生成加密key
	 * 
	 * @return
	 */
	public static SecretKey generalKey(String stringKey) {
		byte[] encodedKey = Base64.decodeBase64(stringKey);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}
	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
   }
	/**
	 * 创建jwt
	 * @param id 唯一id，uuid即可
	 * @param subject json形式字符串或字符串，增加用户非敏感信息存储，如用户id或用户账号，与token解析后进行对比，防止乱用
	 * @param expirationDate  生成jwt的有效期，单位秒
	 * @return jwt token
	 * @throws Exception
	 */
	public static String createJWT(String uuid, String subject, long expirationDate) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey key = generalKey(SECRETKEY);
		JwtBuilder builder = Jwts.builder().setIssuer("").setId(uuid).setIssuedAt(now).setSubject(subject)
				.signWith(signatureAlgorithm, key);
		if (expirationDate >= 0) {
			long expMillis = nowMillis + expirationDate*1000;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
		
	}
 
	/**
	 * 解密jwt，获取实体
	 * @param jwt
	 */
	public static Claims parseJWT(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException {
		SecretKey key = generalKey(SECRETKEY);
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		return claims;
	}
	   /**
     * 验证Token
     * @param token
     * @return
     * @throws Exception
     */

	/**
	* 实例演示
	*/
	public static void main(String[] args) {
		try {
			User user =DB.addUser("123","456");
			String token = createJWT(getUUID(), user.getId(), 10);//10秒过期
			System.out.println(token);
			Claims claims = parseJWT(WrongToken);
			System.out.println("解析jwt："+claims.getSubject());
			user=DB.getUser(claims.getSubject());
			//System.out.println(user.getId());	
			//System.out.println(claims.getExpiration()+"///"+claims.getExpiration().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

}
}