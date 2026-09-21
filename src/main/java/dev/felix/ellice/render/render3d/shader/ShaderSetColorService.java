package dev.felix.ellice.render.render3d.shader;

import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class ShaderSetColorService {
  private final ConcurrentHashMap<String, ShaderSetColorService.UniformValue> text =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<Integer, ShaderSetColorService.TextureBinding> entries =
      new ConcurrentHashMap<>();

  public ShaderSetColorService set(String currentText, int value) {
    this.text.put(createText(currentText), new ShaderSetColorService.IntValue(value));
    return this;
  }

  public ShaderSetColorService set(String currentText, float value) {
    this.text.put(createText(currentText), new ShaderSetColorService.FloatValue(value));
    return this;
  }

  public ShaderSetColorService set(String currentText, float value, float currentValue) {
    this.text.put(
        createText(currentText), new ShaderSetColorService.Vec2Value(value, currentValue));
    return this;
  }

  public ShaderSetColorService set(
      String currentText, float value, float currentValue, float nextValue) {
    this.text.put(
        createText(currentText),
        new ShaderSetColorService.Vec3Value(value, currentValue, nextValue));
    return this;
  }

  public ShaderSetColorService set(
      String currentText, float value, float currentValue, float nextValue, float previousValue) {
    this.text.put(
        createText(currentText),
        new ShaderSetColorService.Vec4Value(value, currentValue, nextValue, previousValue));
    return this;
  }

  public ShaderSetColorService set(String text, Vector2f vector2f) {
    return vector2f == null ? this.remove(text) : this.set(text, vector2f.x, vector2f.y);
  }

  public ShaderSetColorService set(String text, Vector3f vector3f) {
    return vector3f == null
        ? this.remove(text)
        : this.set(text, vector3f.x, vector3f.y, vector3f.z);
  }

  public ShaderSetColorService set(String text, Vector4f vector4f) {
    return vector4f == null
        ? this.remove(text)
        : this.set(text, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
  }

  public ShaderSetColorService setColor(String text, int color) {
    float value = (color >>> 24 & 0xFF) / 255.0F;
    float currentValue = (color >>> 16 & 0xFF) / 255.0F;
    float nextValue = (color >>> 8 & 0xFF) / 255.0F;
    float previousValue = (color & 0xFF) / 255.0F;
    return this.set(text, currentValue, nextValue, previousValue, value);
  }

  public ShaderSetColorService set(String currentText, Matrix4f matrix4f) {
    if (matrix4f == null) {
      return this.remove(currentText);
    }

    float[] floats = new float[16];
    matrix4f.get(floats);
    this.text.put(createText(currentText), new ShaderSetColorService.Matrix4Value(floats));
    return this;
  }

  public ShaderSetColorService remove(String currentText) {
    if (currentText != null) {
      this.text.remove(currentText);
    }

    return this;
  }

  public synchronized ShaderSetColorService texture(
      String text, RhiBlendStateService.TextureHandle textureHandle, int textureId) {
    return this.texture(text, textureHandle, RhiBlendStateService.SamplerHandle.NONE, textureId);
  }

  public synchronized ShaderSetColorService texture(
      String text,
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.SamplerHandle samplerHandle,
      int textureId) {
    if (textureId >= 0 && textureId < 16) {
      String currentText = createText(text);
      RhiBlendStateService.TextureHandle currentTextureHandle =
          textureHandle == null ? RhiBlendStateService.TextureHandle.NONE : textureHandle;
      RhiBlendStateService.SamplerHandle currentSamplerHandle =
          samplerHandle == null ? RhiBlendStateService.SamplerHandle.NONE : samplerHandle;
      this.entries
          .entrySet()
          .removeIf(
              entry ->
                  entry.getKey() != textureId && entry.getValue().uniform().equals(currentText));
      this.entries.put(
          textureId,
          new ShaderSetColorService.TextureBinding(
              currentText, currentTextureHandle, currentSamplerHandle, textureId));
      return this;
    } else {
      throw new IllegalArgumentException(
          "Texture unit out of portable range [0, 16): " + textureId);
    }
  }

  public synchronized ShaderSetColorService removeTexture(int textureId) {
    this.entries.remove(textureId);
    return this;
  }

  public void apply(RhiCommandBuffer rhiCommandBuffer) {
    for (Entry entry : this.text.entrySet()) {
      ((ShaderSetColorService.UniformValue) entry.getValue())
          .apply(rhiCommandBuffer, (String) entry.getKey());
    }

    ArrayList arrayList = new ArrayList<>(this.entries.values());
    arrayList.sort(Comparator.comparingInt(ShaderSetColorService.TextureBinding::unit));

    for (ShaderSetColorService.TextureBinding textureBinding :
        (Iterable<ShaderSetColorService.TextureBinding>) (Iterable<?>) (arrayList)) {
      if (textureBinding.sampler().valid()) {
        rhiCommandBuffer.bindTexture(
            textureBinding.texture(), textureBinding.sampler(), textureBinding.unit());
      } else {
        rhiCommandBuffer.bindTexture(textureBinding.texture(), textureBinding.unit());
      }

      rhiCommandBuffer.pushInt(textureBinding.uniform(), textureBinding.unit());
    }
  }

  private static String createText(String text) {
    if (text != null && !text.isBlank()) {
      return text;
    } else {
      throw new IllegalArgumentException("Uniform name is required");
    }
  }

  private record FloatValue(float value) implements ShaderSetColorService.UniformValue {
    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      rhiCommandBuffer.pushFloat(text, this.value);
    }
  }

  private record IntValue(int value) implements ShaderSetColorService.UniformValue {
    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      rhiCommandBuffer.pushInt(text, this.value);
    }
  }

  private record Matrix4Value(float[] values) implements ShaderSetColorService.UniformValue {
    private Matrix4Value(float[] values) {
      if (values.length != 16) {
        throw new IllegalArgumentException("mat4 requires 16 floats");
      }

      values = (float[]) values.clone();
      this.values = values;
    }

    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
        FloatBuffer floatBuffer = stack.mallocFloat(16);
        floatBuffer.put(this.values).flip();
        rhiCommandBuffer.pushMatrix4(text, floatBuffer);
      } catch (Throwable exception) {
        if (stack != null) {
          try {
            stack.close();
          } catch (Throwable currentException) {
            exception.addSuppressed(currentException);
          }
        }

        throw exception;
      }

      if (stack != null) {
        stack.close();
      }
    }
  }

  private record TextureBinding(
      String uniform,
      RhiBlendStateService.TextureHandle texture,
      RhiBlendStateService.SamplerHandle sampler,
      int unit) {}

  private sealed interface UniformValue
      permits ShaderSetColorService.IntValue,
          ShaderSetColorService.FloatValue,
          ShaderSetColorService.Vec2Value,
          ShaderSetColorService.Vec3Value,
          ShaderSetColorService.Vec4Value,
          ShaderSetColorService.Matrix4Value {
    void apply(RhiCommandBuffer rhiCommandBuffer, String text);
  }

  private record Vec2Value(float x, float y) implements ShaderSetColorService.UniformValue {
    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      rhiCommandBuffer.pushVec2(text, this.x, this.y);
    }
  }

  private record Vec3Value(float x, float y, float z)
      implements ShaderSetColorService.UniformValue {
    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      rhiCommandBuffer.pushVec3(text, this.x, this.y, this.z);
    }
  }

  private record Vec4Value(float x, float y, float z, float w)
      implements ShaderSetColorService.UniformValue {
    @Override
    public void apply(RhiCommandBuffer rhiCommandBuffer, String text) {
      rhiCommandBuffer.pushVec4(text, this.x, this.y, this.z, this.w);
    }
  }
}
