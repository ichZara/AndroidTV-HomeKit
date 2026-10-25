package uk.org.fancy.AndroidTvHomeKit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import android.util.Base64;

/**
 * Generates Apple HomeKit X-HM:// setup URI (for QR code pairing) and the 4-byte SHA-512 Setup Hash ("sh" TXT record).
 */
public final class HomeKitSetupPayload {
    public static final String DEFAULT_SETUP_ID = "PTV8";

    private HomeKitSetupPayload() {}

    public static String generateSetupUri(String pinWithDashes, int category, String setupId) {
        String digits = pinWithDashes.replace("-", "").trim();
        long pinInt = Long.parseLong(digits);
        long payload = 0L;
        // Version (3 bits: 44..42) = 0
        // Reserved (4 bits: 41..39) = 0
        // Category (8 bits: 38..31) = category (31 for Television)
        payload |= ((long) category & 0xFFL) << 31;
        // Flags (4 bits: 30..27) = 2 (IP transport = bit 28; bit 30 Paired/WAC = 0)
        payload |= (2L & 0xFL) << 27;
        // Setup Code (27 bits: 26..0)
        payload |= (pinInt & 0x7FFFFFFL);

        String base36 = Long.toString(payload, 36).toUpperCase(Locale.US);
        StringBuilder sb = new StringBuilder("X-HM://");
        for (int i = base36.length(); i < 9; i++) {
            sb.append('0');
        }
        sb.append(base36);
        sb.append(setupId);
        return sb.toString();
    }

    public static String generateSetupHash(String setupId, String macAddress) {
        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            String input = setupId + macAddress.toUpperCase(Locale.US);
            byte[] digest = sha512.digest(input.getBytes(StandardCharsets.UTF_8));
            byte[] first4 = Arrays.copyOf(digest, 4);
            return Base64.encodeToString(first4, Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }
}
