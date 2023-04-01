package com.example.TestAuthSpringBoot3.security.keys;

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface KeyUtils {

    Key getAccessTokenPrivateKey();

    Key getAccessTokenPublicKey();

    Key getRefreshTokenPrivateKey();

    Key getRefreshTokenPublicKey();

}
