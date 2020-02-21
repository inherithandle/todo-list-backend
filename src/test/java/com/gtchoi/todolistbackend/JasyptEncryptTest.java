package com.gtchoi.todolistbackend;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JasyptEncryptTest {

    /**
     * maria DB password를 암호화 할 때 사용했습니다.
     */
    @Test
    public void encryptionAndDecryption() {
        final String key = "your-key";
        final String dbPassword = "I'm a DB password. please encrypt me so that someone can't recognize me.";

        StandardPBEStringEncryptor encryptor = getEncryptor(key);
        final String encrypted = encryptor.encrypt(dbPassword);
        final String decrypted = encryptor.decrypt(encrypted);
        assertThat(decrypted, is(dbPassword));

        System.out.println("encrypted DB password = " + encrypted);
    }

    private StandardPBEStringEncryptor getEncryptor(final String key) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        // excerpt from https://github.com/ulisesbocchio/jasypt-spring-boot#password-based-encryption-configuration.
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setKeyObtentionIterations(1000);
        encryptor.setProviderName("SunJCE");
        encryptor.setSaltGenerator(new RandomSaltGenerator());
        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setStringOutputType("base64");
        encryptor.setPassword(key);

        return encryptor;
    }
}
