package dev.felix.ellice.security;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@ElliceKeep
public final class ElliceStringVault {
   private static final ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>();

   private ElliceStringVault() {
   }

   public static Object resolve(Lookup caller, String name, Class<?> type, String payload, long keyA, long keyB) throws GeneralSecurityException {
      if (type != String.class) {
         throw new IllegalArgumentException("Expected a string constant");
      } else {
         return decrypt(caller, name, payload, keyA, keyB).intern();
      }
   }

   public static CallSite link(Lookup caller, String name, MethodType type, String payload, long keyA, long keyB, int kind) throws Throwable {
      String target = decrypt(caller, name, payload, keyA, keyB);
      MethodHandle method;
      if (kind == 0) {
         method = caller.findStatic(caller.lookupClass(), target, type);
      } else {
         if (kind != 1 || type.parameterCount() <= 0 || type.parameterType(0) != caller.lookupClass()) {
            throw new IllegalArgumentException("Invalid private call site");
         }

         method = caller.findVirtual(caller.lookupClass(), target, type.dropParameterTypes(0, 1));
      }

      return new ConstantCallSite(method.asType(type));
   }

   private static String decrypt(Lookup caller, String name, String payload, long keyA, long keyB) throws GeneralSecurityException {
      byte[] encoded = Base64.getDecoder().decode(payload);
      if (encoded.length < 28) {
         throw new IllegalArgumentException("Invalid string payload");
      }

      byte[] key = ByteBuffer.allocate(16).putLong(keyA).putLong(keyB).array();
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      cipher.init(2, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, Arrays.copyOf(encoded, 12)));
      cipher.updateAAD((caller.lookupClass().getName() + "|" + name).getBytes(StandardCharsets.UTF_8));
      ByteBuffer plain = ByteBuffer.wrap(cipher.doFinal(encoded, 12, encoded.length - 12));
      if ((plain.remaining() & 1) != 0) {
         throw new IllegalArgumentException("Invalid string length");
      }

      char[] chars = new char[plain.remaining() / 2];

      for (int i = 0; i < chars.length; i++) {
         chars[i] = plain.getChar();
      }

      return new String(chars);
   }

   public static String open(String payload) {
      return payload != null && !payload.isEmpty() ? CACHE.computeIfAbsent(payload, ElliceStringVault::decode) : "";
   }

   private static String decode(String payload) {
      byte[] encoded = Base64.getDecoder().decode(payload);
      int key = encoded[0] & 255;
      byte[] plain = new byte[encoded.length - 1];

      for (int i = 0; i < plain.length; i++) {
         plain[i] = (byte)(encoded[i + 1] ^ (byte)(key + i * 31));
      }

      return new String(plain, StandardCharsets.UTF_8);
   }
}
