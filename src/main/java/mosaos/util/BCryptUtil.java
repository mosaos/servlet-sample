package mosaos.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BCryptUtil {

    private static final String DEFAULT_RAW = "admin";
    private static final String DEFAULT_ENCODED = "$2y$12$TKubBzAs4MBuEU.5DwXiZe9.Z/9chC49dYU2/yqZy1xvdk0Hx0yk6";

    // BCryptPasswordEncoder is thread-safe.
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        switch (args.length) {
        case 1:
            String encoded = encode(args[0]);
            log.info("{} was encoded to {}",args[0], encoded);
            break;
        case 2:
            boolean isMatch = isMatch(args[0], args[1]);
            log.info("raw : {}, encoded : {}", args[0], args[1]);
            log.info("is match? : {}", isMatch);
            break;
        default:
            System.out.println("Specify parameters plz.");
            System.out.println("-- when 1 param, encode it. like the followings. --");
            encode(DEFAULT_RAW);
            System.out.println("-- when 2 params, check match or not. 1st=raw, 2nd=encoded. --");
            isMatch(DEFAULT_RAW, DEFAULT_ENCODED);
        }
    }

    public static boolean isMatch(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }

    public static String encode(String raw) {
        return encoder.encode(raw);
    }
}
