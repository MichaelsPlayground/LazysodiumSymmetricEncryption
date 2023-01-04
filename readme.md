# Lazysodium symmetric encryption

This app encrypts a string using the **Libsodium** library **Lazysodium for Android** that is available here:

https://github.com/terl/lazysodium-android

As it is a concept study there are **no interactions, buttons or view elements** on the Android-UI. 
All outputs are shown in the console/Logcat.

There are 3 steps in the workflow:
* 1 derive an encryption key using a **key derivation** - here I'm using **Argon2ID** (details see below)
* 2 encrypt a plaintext-string with to a ciphertext (ciphertext is in **hex encoding**) with this algorithm: **ChaCha20Poly130**
* 3 decrypt the ciphertext to a decryptedtext-string using the same algorithm as in step 2

**key derivation parameter for Argon2ID**:
* PwHash.ARGON2ID_OPSLIMIT_INTERACTIVE
* PwHash.MEMLIMIT_INTERACTIVE
* PwHash.Alg.PWHASH_ALG_ARGON2ID13

There is a second encryption workflow using **AES-256 in GCM mode** running the same steps, but **unfortunately 
this mode is not available** on Android (although it should). 

This is the complete logfile:

```plaintext
I/LazysodiumSymmEncr: init Lazysodium
I/LazysodiumSymmEncr: passphrase: This is my passphrase 123
I/LazysodiumSymmEncr: plaintext: The quick brown fox jumps over the lazy dog
I/LazysodiumSymmEncr: ******************************************************************************************
I/LazysodiumSymmEncr: complete encryption workflow for Argon2ID key derivation and ChaCha20Poly1305 encryption
I/LazysodiumSymmEncr: generate a random salt for key derivation: C5A37EDF2E7D64F2FD795097862FEBE270C4028C05746602665F8929B0A5C02B
I/LazysodiumSymmEncr: keyDerivationResult: true
I/LazysodiumSymmEncr: generated random key: A826B0D5F69F11DAA8AFC067240C88D513C0614DE8BD1E1D68AD29085C7E2434
I/LazysodiumSymmEncr: generated random key length: 32
I/LazysodiumSymmEncr: generate a random nonce for encryption: 5A83B0F06C19D150
I/LazysodiumSymmEncr: generate a key from outputKey
I/LazysodiumSymmEncr: encrypt the plaintext using ChaCha20Poly1305 algorithm
I/LazysodiumSymmEncr: ciphertext: CD49BBE0BDB9BD0F1C82418E1D2C9F714656E95F03D4CEADA3036D4806624C4D367095AF712788525872EF6EF8D82BD1D8D34995EBAEBE7354581D
I/LazysodiumSymmEncr: decrypt the ciphertext using the same algorithm, key and nonce
I/LazysodiumSymmEncr: decryptedtext: The quick brown fox jumps over the lazy dog
I/LazysodiumSymmEncr: plaintext equals to decrptedtext: true
I/LazysodiumSymmEncr: ******************************************************************************************
I/LazysodiumSymmEncr: complete encryption workflow for Argon2ID key derivation and AES-256 GCM encryption
I/LazysodiumSymmEncr: generate a random salt for key derivation: 8150B1B23AFFC5C24A18CEAEF443FBDF3CFB5291A1F4F9529CE6C84D9D70F7C4
I/LazysodiumSymmEncr: keyDerivationResult: true
I/LazysodiumSymmEncr: generated random key: 2A6FF3C492FD9C1D7E46D99AA7EC70EEA4BB66954E3AFB3DCD88B8C6B33DE30C
I/LazysodiumSymmEncr: generated random key length: 32
I/LazysodiumSymmEncr: generate a random nonce for encryption: 5076DECF5C54002559A39D99
I/LazysodiumSymmEncr: generate a key from outputKey
I/LazysodiumSymmEncr: encrypt the plaintext using AES-256 GCM algorithm
I/LazysodiumSymmEncr: ciphertext: 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
I/LazysodiumSymmEncr: decrypt the ciphertext using the same algorithm, key and nonce
E/LazysodiumSymmEncr: AEAD AES-256 GCM is not available
```

To run the app you need to load these dependencies in your build.gradle (app):
```plaintext
implementation 'net.java.dev.jna:jna:5.12.1@aar'
implementation 'com.goterl:lazysodium-android:5.1.0@aar'
```


