package com.example.TestAuthSpringBoot3.security.key;

import java.security.Key;

public interface KeyUtils {

    Key getAccessTokenPrivateKey();

    Key getAccessTokenPublicKey();

    Key getRefreshTokenPrivateKey();

    Key getRefreshTokenPublicKey();

}
