package com.company;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class User implements Serializable {
    //dlaczego user ma dziedziczyc po Person to ja nie wiem doslownie
    private String login, password;
    private SecretKey userkey;
    private IvParameterSpec userIv;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    //TEN KOD SŁUŻY DO SZYFROWANIA OBIEKTU KLASY
    public void generateKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String salt = "50";
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey result = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        this.userkey = result;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        this.userIv = new IvParameterSpec(iv);
    }

    public static SealedObject encryptObj(String algorithm, Serializable object, SecretKey key, IvParameterSpec iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE,key,iv);
        SealedObject sealedObject = new SealedObject(object,cipher);
        return sealedObject;
    }
    public static Serializable decryptObject(String algorithm, SealedObject sealedObject, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException,
            IllegalBlockSizeException, IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }

    public static void toEncryptedFile(User[] users, String path){
        try{
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(int i = 0; i < users.length; i++){
                oos.writeObject(encryptObj("AES/CBC/PKCS5Padding",users[i],users[i].userkey,users[i].userIv));
            }
        }catch (IOException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException | NoSuchPaddingException |
                IllegalBlockSizeException | InvalidKeyException e){
            e.printStackTrace();
        }
    }

    public static User[] fromEncryptedFile(String path){
        ArrayList<User> users = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream iis = new ObjectInputStream(fis);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        User[] result = users.toArray(new User[0]);
        return result;
    }
}

/* TEN KOD SŁUŻY DO ENKRYPCJI STRINGA XDDDD NIE KLASY
    public static SecretKey generateKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey result = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return result;
    }
    public static IvParameterSpec generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decodedText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(decodedText);
    }

   */
