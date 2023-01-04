# Lazysodium symmetric encryption

This app encrypts a string using the **Libsodium** library **Lazysodium for Android** that is available here:

https://github.com/terl/lazysodium-android

As it is a concept study there are **no interactions, buttons or view elements** on the Android-UI. 
All outputs are shown in the console/Logcat.

There are 3 steps in the workflow:
* 1 derive an encryption key using a **key derivation** - here I'm using **Argon2ID** (details see below)
* 2 encrypt a plaintext-string with to a ciphertext (ciphertext is in **hex encoding**) with this algorithm: **ChaCha20Poly130**
* 3 decrypt the ciphertext to a decryptedtext-string using the same algorithm as in step 2

**key derivation with Argon2ID**:
* PwHash.ARGON2ID_OPSLIMIT_INTERACTIVE
* PwHash.MEMLIMIT_INTERACTIVE
* PwHash.Alg.PWHASH_ALG_ARGON2ID13



To run the app you need to load these dependencies in your build.gradle (app):
```plaintext
implementation 'net.java.dev.jna:jna:5.12.1@aar'
implementation 'com.goterl:lazysodium-android:5.1.0@aar'
```


