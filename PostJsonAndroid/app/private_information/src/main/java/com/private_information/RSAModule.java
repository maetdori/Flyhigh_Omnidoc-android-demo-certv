package com.private_information;

import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class RSAModule {
    static byte[] encryptRSA(byte[] key,byte[] input)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

        // Turn the encoded key into a real RSA public key.
        // Public keys are encoded in X.509.
        X509EncodedKeySpec ukeySpec = new X509EncodedKeySpec(key);
        KeyFactory ukeyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            publicKey = ukeyFactory.generatePublic(ukeySpec);
        } catch (InvalidKeySpecException e) {
            PKCS8EncodedKeySpec rKeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory rkeyFactory = KeyFactory.getInstance("RSA");
            privateKey = rkeyFactory.generatePrivate(rKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            Log.i("RSAMODULE", "EncryptWithPrivateKey");
            byte[] cipherText = cipher.doFinal(input);
            return cipherText;
        }

        // 공개키를 전달하여 암호화
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        Log.i("RSAMODULE", "EncryptWithPublicKey");
        byte[] cipherText = cipher.doFinal(input);
        return cipherText;
    }
    static byte[] decryptRSA(byte[] key, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        // Turn the encoded key into a real RSA private key.
        // Private keys are encoded in PKCS#8.
        PKCS8EncodedKeySpec rkeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory rkeyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            privateKey = rkeyFactory.generatePrivate(rkeySpec);
        } catch (InvalidKeySpecException e) {
            X509EncodedKeySpec ukeySpec = new X509EncodedKeySpec(key);
            KeyFactory ukeyFactory = KeyFactory.getInstance("RSA");
            publicKey = ukeyFactory.generatePublic(ukeySpec);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            Log.i("RSAMODULE","DecryptWithPublicKey");
            byte[] plainText = cipher.doFinal(cipherText);
            return plainText;
        }
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        Log.i("RSAMODULE","DecryptWithPrivateKey");
        byte[] plainText = cipher.doFinal(cipherText);
        return plainText;
    }

}
