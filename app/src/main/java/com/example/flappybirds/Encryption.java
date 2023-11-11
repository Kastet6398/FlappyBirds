package com.example.flappybirds;

public class Encryption {
    public String encrypt(String plainText, EncryptionType encryptionType) {
        if (encryptionType == EncryptionType.CAESAR) {
            StringBuilder encryptedText = new StringBuilder();
            for (char c : plainText.toCharArray()) encryptedText.append(c + 3);
            return encryptedText.toString();
        }
        throw new UnsupportedOperationException("Encryption type not supported");
    }
    public String decrypt(String encryptedText, EncryptionType encryptionType) {
        if (encryptionType == EncryptionType.CAESAR) {
            StringBuilder decryptedText = new StringBuilder();
            for (char c : encryptedText.toCharArray()) decryptedText.append(c - 3);
            return decryptedText.toString();
        }
        throw new UnsupportedOperationException("Encryption type not supported");
    }
}
