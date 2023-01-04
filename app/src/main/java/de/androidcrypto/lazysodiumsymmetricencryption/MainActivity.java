package de.androidcrypto.lazysodiumsymmetricencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.goterl.lazysodium.LazySodiumAndroid;
import com.goterl.lazysodium.SodiumAndroid;
import com.goterl.lazysodium.interfaces.AEAD;
import com.goterl.lazysodium.interfaces.PwHash;
import com.goterl.lazysodium.utils.Key;

import java.nio.charset.StandardCharsets;

import javax.crypto.AEADBadTagException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LazysodiumSymmEncr";
    protected LazySodiumAndroid ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lazysodium workflow
        Log.i(TAG, "init Lazysodium");
        // init Lazysodium
        ls = new LazySodiumAndroid(new SodiumAndroid());

        String passphrase = "This is my passphrase 123";
        Log.i(TAG, "passphrase: " + passphrase);

        // encrypt a plaintext
        String plaintext = "The quick brown fox jumps over the lazy dog";
        Log.i(TAG, "plaintext: " + plaintext);

        // run ChaCha20Poly1305 encryption
        chaCha20Poly1305EncryptionWorkflow(passphrase, plaintext);

        // run AES-256 GCM encryption
        aes256GcmEncryptionWorkflow(passphrase, plaintext);
    }

    private void chaCha20Poly1305EncryptionWorkflow(String passphrase, String plaintext) {
        Log.i(TAG, "******************************************************************************************");
        Log.i(TAG, "complete encryption workflow for Argon2ID key derivation and ChaCha20Poly1305 encryption");
        // generate salt
        byte[] saltByte = ls.randomBytesBuf(32);
        Log.i(TAG, "generate a random salt for key derivation: " + ls.sodiumBin2Hex(saltByte));
        // generate the 256 bit / 32 byte long encryption key
        byte[] outputKey = new byte[32];
        int outputKeyLen = 32;
        byte[] passPhraseByte = ls.bytes(passphrase);
        int passPhraseLength = passPhraseByte.length;
        boolean result = ls.cryptoPwHash(
                outputKey,
                outputKeyLen,
                passPhraseByte,
                passPhraseLength,
                saltByte,
                PwHash.ARGON2ID_OPSLIMIT_INTERACTIVE,
                PwHash.MEMLIMIT_INTERACTIVE,
                PwHash.Alg.PWHASH_ALG_ARGON2ID13);
        Log.i(TAG, "keyDerivationResult: " + result);
        Log.i(TAG, "generated random key: " + ls.sodiumBin2Hex(outputKey));
        Log.i(TAG, "generated random key length: " + outputKey.length);
        // generate a random nonce
        byte[] nonceByte = ls.nonce(AEAD.CHACHA20POLY1305_NPUBBYTES);
        Log.i(TAG, "generate a random nonce for encryption: " + ls.sodiumBin2Hex(nonceByte));
        // generate a key from outputKey
        Key key = Key.fromBytes(outputKey);
        Log.i(TAG, "generate a key from outputKey");
        // encrypt
        Log.i(TAG, "encrypt the plaintext using ChaCha20Poly1305 algorithm");
        String ciphertext = ls.encrypt(plaintext, null, nonceByte, key, AEAD.Method.CHACHA20_POLY1305);
        // ciphertext is in hex encoding
        Log.i(TAG, "ciphertext: " + ciphertext);
        // decrypt
        Log.i(TAG, "decrypt the ciphertext using the same algorithm, key and nonce");
        String decryptedtext = "";
        try {
            decryptedtext = ls.decrypt(ciphertext, null , nonceByte, key, AEAD.Method.CHACHA20_POLY1305);
        } catch (AEADBadTagException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, "decryptedtext: " + decryptedtext);
        Log.i(TAG, "plaintext equals to decrptedtext: " + decryptedtext.equals(plaintext));
    }

    private void aes256GcmEncryptionWorkflow(String passphrase, String plaintext) {
        Log.i(TAG, "******************************************************************************************");
        Log.i(TAG, "complete encryption workflow for Argon2ID key derivation and AES-256 GCM encryption");
        // generate salt
        byte[] saltByte = ls.randomBytesBuf(32);
        Log.i(TAG, "generate a random salt for key derivation: " + ls.sodiumBin2Hex(saltByte));
        // generate the 256 bit / 32 byte long encryption key
        byte[] outputKey = new byte[32];
        int outputKeyLen = 32;
        byte[] passPhraseByte = ls.bytes(passphrase);
        int passPhraseLength = passPhraseByte.length;
        boolean result = ls.cryptoPwHash(
                outputKey,
                outputKeyLen,
                passPhraseByte,
                passPhraseLength,
                saltByte,
                PwHash.ARGON2ID_OPSLIMIT_INTERACTIVE,
                PwHash.MEMLIMIT_INTERACTIVE,
                PwHash.Alg.PWHASH_ALG_ARGON2ID13);
        Log.i(TAG, "keyDerivationResult: " + result);
        Log.i(TAG, "generated random key: " + ls.sodiumBin2Hex(outputKey));
        Log.i(TAG, "generated random key length: " + outputKey.length);
        // generate a random nonce
        byte[] nonceByte = ls.nonce(AEAD.AES256GCM_NPUBBYTES);
        Log.i(TAG, "generate a random nonce for encryption: " + ls.sodiumBin2Hex(nonceByte));
        // generate a key from outputKey
        Key key = Key.fromBytes(outputKey);
        Log.i(TAG, "generate a key from outputKey");
        // encrypt
        Log.i(TAG, "encrypt the plaintext using AES-256 GCM algorithm");
        String ciphertext = ls.encrypt(plaintext, null, nonceByte, key, AEAD.Method.AES256GCM);
        // ciphertext is in hex encoding
        Log.i(TAG, "ciphertext: " + ciphertext);
        // decrypt
        Log.i(TAG, "decrypt the ciphertext using the same algorithm, key and nonce");
        String decryptedtext = "";
        if (ls.cryptoAeadAES256GCMIsAvailable()) {
            Log.i(TAG, "AEAD AES-256 GCM is available");
            try {
                decryptedtext = ls.decrypt(ciphertext, null, nonceByte, key, AEAD.Method.AES256GCM);
            } catch (AEADBadTagException e) {
                e.printStackTrace();
                //Log.e(TAG, e.getMessage());
            }
            Log.i(TAG, "decryptedtext: " + decryptedtext);
            Log.i(TAG, "plaintext equals to decrptedtext: " + decryptedtext.equals(plaintext));
        } else {
            Log.e(TAG, "AEAD AES-256 GCM is not available");
        }
    }
}