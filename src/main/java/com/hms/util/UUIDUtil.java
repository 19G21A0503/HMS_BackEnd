package com.hms.util;

import java.util.UUID;

public final class UUIDUtil {

    private UUIDUtil() {
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

}