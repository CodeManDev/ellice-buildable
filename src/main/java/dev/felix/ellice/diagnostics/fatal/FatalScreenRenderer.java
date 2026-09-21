package dev.felix.ellice.diagnostics.fatal;

import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL12;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FatalScreenRenderer {
   private static final Logger logger = LoggerFactory.getLogger("ellice-fatal");
   private static final String text = "#version 330 core\nlayout(location = 0) in vec2 aPos;\nlayout(location = 1) in vec2 aUv;\nout vec2 vUv;\nvoid main() {\n    gl_Position = vec4(aPos, 0.0, 1.0);\n    vUv = aUv;\n}\n";
   private static final String text2 = "#version 330 core\nin vec2 vUv;\nuniform sampler2D uTex;\nout vec4 fragColor;\nvoid main() {\n    fragColor = texture(uTex, vec2(vUv.x, 1.0 - vUv.y));\n}\n";
   private int count;
   private int count2;
   private int count3;
   private int count4;
   private int count5;
   private int count6;
   private boolean enabled;
   private int count7;

   boolean ready() {
      return this.enabled;
   }

   boolean ensure() {
      if (this.enabled) {
         return true;
      }

      int value = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
      int currentValue = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);

      try {
         int nextValue = calculateValue(
            GL20.GL_VERTEX_SHADER,
            "#version 330 core\nlayout(location = 0) in vec2 aPos;\nlayout(location = 1) in vec2 aUv;\nout vec2 vUv;\nvoid main() {\n    gl_Position = vec4(aPos, 0.0, 1.0);\n    vUv = aUv;\n}\n"
         );
         int previousValue = calculateValue(
            GL20.GL_FRAGMENT_SHADER,
            "#version 330 core\nin vec2 vUv;\nuniform sampler2D uTex;\nout vec4 fragColor;\nvoid main() {\n    fragColor = texture(uTex, vec2(vUv.x, 1.0 - vUv.y));\n}\n"
         );
         this.count = GL20.glCreateProgram();
         GL20.glAttachShader(this.count, nextValue);
         GL20.glAttachShader(this.count, previousValue);
         GL20.glBindAttribLocation(this.count, 0, "aPos");
         GL20.glBindAttribLocation(this.count, 1, "aUv");
         GL20.glLinkProgram(this.count);
         GL20.glDeleteShader(nextValue);
         GL20.glDeleteShader(previousValue);
         if (GL20.glGetProgrami(this.count, GL20.GL_LINK_STATUS) == 0) {
            throw new IllegalStateException(GL20.glGetProgramInfoLog(this.count));
         }

         this.count2 = GL30.glGenVertexArrays();
         this.count3 = GL15.glGenBuffers();
         GL30.glBindVertexArray(this.count2);
         GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.count3);
         FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);

         try {
            floatBuffer.put(
                  new float[]{
                     -1.0F,
                     -1.0F,
                     0.0F,
                     0.0F,
                     1.0F,
                     -1.0F,
                     1.0F,
                     0.0F,
                     -1.0F,
                     1.0F,
                     0.0F,
                     1.0F,
                     1.0F,
                     1.0F,
                     1.0F,
                     1.0F
                  }
               )
               .flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
         } finally {
            MemoryUtil.memFree(floatBuffer);
         }

         GL20.glEnableVertexAttribArray(0);
         GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 16, 0L);
         GL20.glEnableVertexAttribArray(1);
         GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 16, 8L);
         GL30.glBindVertexArray(0);
         GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         this.count4 = GL11.glGenTextures();
         this.enabled = true;
         return true;
      } catch (Throwable exception) {
         this.count7++;
         if (this.count7 <= 3 || this.count7 % 60 == 0) {
            logger.error("Isolated crash overlay GPU init failed ({})", this.count7, exception);
         }

         return false;
      } finally {
         GL30.glBindVertexArray(value);
         GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, currentValue);
      }
   }

   void upload(int[] ints, int value, int currentValue) {
      if (this.ensure() && ints != null && value > 0 && currentValue > 0) {
         ByteBuffer byteBuffer = MemoryUtil.memAlloc(value * currentValue * 4);
         int nextValue = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
         int previousValue = GL11.glGetInteger(GL21.GL_PIXEL_UNPACK_BUFFER_BINDING);
         int[] currentInts = new int[]{GL11.GL_UNPACK_ALIGNMENT, GL11.GL_UNPACK_ROW_LENGTH, GL11.GL_UNPACK_SKIP_ROWS, GL11.GL_UNPACK_SKIP_PIXELS};
         int[] currentLength = new int[currentInts.length];

         for (int index = 0; index < currentInts.length; index++) {
            currentLength[index] = GL11.glGetInteger(currentInts[index]);
         }

         try {
            for (int sourceValue : ints) {
               byteBuffer.put((byte)(sourceValue >> 16 & 0xFF));
               byteBuffer.put((byte)(sourceValue >> 8 & 0xFF));
               byteBuffer.put((byte)(sourceValue & 0xFF));
               byteBuffer.put((byte)(sourceValue >> 24 & 0xFF));
            }

            byteBuffer.flip();
            GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.count4);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
            if (value == this.count5 && currentValue == this.count6) {
               GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, value, currentValue, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            } else {
               GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, value, currentValue, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
               this.count5 = value;
               this.count6 = currentValue;
            }

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
         } finally {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, nextValue);
            GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, previousValue);

            for (int currentIndex = 0; currentIndex < currentInts.length; currentIndex++) {
               GL11.glPixelStorei(currentInts[currentIndex], currentLength[currentIndex]);
            }

            MemoryUtil.memFree(byteBuffer);
         }
      }
   }

   void blit(int x, int y) {
      if (this.ensure() && this.count5 > 0) {
         int width = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
         int height = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
         int value = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
         int currentValue = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
         GL13.glActiveTexture(GL13.GL_TEXTURE0);
         int nextValue = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
         int previousValue = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
         int sourceValue = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
         GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
         int targetValue = GL11.glGetInteger(GL11.GL_DRAW_BUFFER);
         boolean enabled = GL11.glIsEnabled(GL11.GL_BLEND);
         boolean currentEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
         boolean nextEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);
         boolean previousEnabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
         boolean sourceEnabled = GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
         boolean targetEnabled = GL11.glIsEnabled(GL30.GL_FRAMEBUFFER_SRGB);
         boolean inputEnabled = GL11.glIsEnabled(GL30.GL_RASTERIZER_DISCARD);
         boolean outputEnabled = GL11.glIsEnabled(GL11.GL_COLOR_LOGIC_OP);
         ByteBuffer byteBuffer = MemoryUtil.memAlloc(4);
         GL11.glGetBooleanv(GL11.GL_COLOR_WRITEMASK, byteBuffer);
         int[] ints = new int[2];
         GL11.glGetIntegerv(GL11.GL_POLYGON_MODE, ints);
         int[] currentInts = new int[4];
         GL11.glGetIntegerv(GL11.GL_VIEWPORT, currentInts);

         try {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL30.GL_FRAMEBUFFER_SRGB);
            GL11.glDisable(GL30.GL_RASTERIZER_DISCARD);
            GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
            GL11.glColorMask(true, true, true, true);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glDrawBuffer(GL11.GL_BACK);
            GL11.glViewport(0, 0, x, y);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.count4);
            GL33.glBindSampler(0, 0);
            GL20.glUseProgram(this.count);
            int currentCount = GL20.glGetUniformLocation(this.count, "uTex");
            if (currentCount >= 0) {
               GL20.glUniform1i(currentCount, 0);
            }

            GL30.glBindVertexArray(this.count2);
            GL11.glDrawArrays(5, 0, 4);
         } finally {
            GL20.glUseProgram(width);
            GL30.glBindVertexArray(height);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, value);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, nextValue);
            GL33.glBindSampler(0, previousValue);
            GL13.glActiveTexture(currentValue);
            GL11.glDrawBuffer(targetValue);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, sourceValue);
            GL11.glColorMask(byteBuffer.get(0) != 0, byteBuffer.get(1) != 0, byteBuffer.get(2) != 0, byteBuffer.get(3) != 0);
            MemoryUtil.memFree(byteBuffer);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, ints[0]);
            GL11.glViewport(currentInts[0], currentInts[1], currentInts[2], currentInts[3]);
            updateState(GL11.GL_BLEND, enabled);
            updateState(GL11.GL_DEPTH_TEST, currentEnabled);
            updateState(GL11.GL_CULL_FACE, nextEnabled);
            updateState(GL11.GL_SCISSOR_TEST, previousEnabled);
            updateState(GL11.GL_STENCIL_TEST, sourceEnabled);
            updateState(GL30.GL_FRAMEBUFFER_SRGB, targetEnabled);
            updateState(GL30.GL_RASTERIZER_DISCARD, inputEnabled);
            updateState(GL11.GL_COLOR_LOGIC_OP, outputEnabled);
         }
      }
   }

   static void fallbackClear() {
      try {
         int value = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
         boolean enabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
         boolean currentEnabled = GL11.glIsEnabled(GL30.GL_FRAMEBUFFER_SRGB);
         boolean nextEnabled = GL11.glIsEnabled(GL30.GL_RASTERIZER_DISCARD);
         float[] floats = new float[4];
         GL11.glGetFloatv(GL11.GL_COLOR_CLEAR_VALUE, floats);
         GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
         int currentValue = GL11.glGetInteger(GL11.GL_DRAW_BUFFER);
         ByteBuffer byteBuffer = MemoryUtil.memAlloc(4);
         GL11.glGetBooleanv(GL11.GL_COLOR_WRITEMASK, byteBuffer);

         try {
            GL11.glDrawBuffer(GL11.GL_BACK);
            GL11.glColorMask(true, true, true, true);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glDisable(GL30.GL_FRAMEBUFFER_SRGB);
            GL11.glDisable(GL30.GL_RASTERIZER_DISCARD);
            GL11.glClearColor(0.04F, 0.04F, 0.06F, 1.0F);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
         } finally {
            GL11.glColorMask(byteBuffer.get(0) != 0, byteBuffer.get(1) != 0, byteBuffer.get(2) != 0, byteBuffer.get(3) != 0);
            MemoryUtil.memFree(byteBuffer);
            GL11.glClearColor(floats[0], floats[1], floats[2], floats[3]);
            updateState(GL11.GL_SCISSOR_TEST, enabled);
            updateState(GL30.GL_FRAMEBUFFER_SRGB, currentEnabled);
            updateState(GL30.GL_RASTERIZER_DISCARD, nextEnabled);
            GL11.glDrawBuffer(currentValue);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, value);
         }
      } catch (Throwable exception) {
      }
   }

   private static void updateState(int value, boolean enabled) {
      if (enabled) {
         GL11.glEnable(value);
      } else {
         GL11.glDisable(value);
      }
   }

   private static int calculateValue(int value, String text) {
      int shaderId = GL20.glCreateShader(value);
      GL20.glShaderSource(shaderId, text);
      GL20.glCompileShader(shaderId);
      if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
         String currentText = GL20.glGetShaderInfoLog(shaderId);
         GL20.glDeleteShader(shaderId);
         throw new IllegalStateException(currentText);
      } else {
         return shaderId;
      }
   }
}

