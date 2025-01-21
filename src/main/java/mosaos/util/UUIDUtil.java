package mosaos.util;

import java.nio.ByteBuffer;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public class UUIDUtil {
    public static byte[] uuidtoBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID bytesToUuid(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    /**
     * Get UUID from path parameter.
     * @param req HttpServletRequest
     * @return UUID of user
     */
    public static String getUuid(HttpServletRequest req) {
        String uuid = null;
        String pathInfo = req.getPathInfo(); // */{uuid}
        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 0) {
                uuid = pathParts[1]; // {uuid}
            }
        }
        return uuid;
    }
}
