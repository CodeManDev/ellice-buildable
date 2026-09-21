package dev.felix.ellice.event;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public final class EventAttackInputService {
  public static final EventTypeService<EventAttackInputService.Tick> TICK =
      EventTypeService.of(EventAttackInputService.Tick.class);
  public static final EventTypeService<EventAttackInputService.GameplayInput> GAMEPLAY_INPUT =
      EventTypeService.of(EventAttackInputService.GameplayInput.class);
  public static final EventTypeService<EventAttackInputService.AttackInput> ATTACK_INPUT =
      EventTypeService.of(EventAttackInputService.AttackInput.class);
  public static final EventTypeService<EventAttackInputService.PlayerPositionCorrected>
      PLAYER_POSITION_CORRECTED =
          EventTypeService.of(EventAttackInputService.PlayerPositionCorrected.class);
  public static final EventTypeService<EventAttackInputService.PrePlayerMovement>
      PRE_PLAYER_MOVEMENT = EventTypeService.of(EventAttackInputService.PrePlayerMovement.class);
  public static final EventTypeService<EventAttackInputService.PreMovementPacket>
      PRE_MOVEMENT_PACKET = EventTypeService.of(EventAttackInputService.PreMovementPacket.class);
  public static final EventTypeService<EventAttackInputService.MovementPacketPrepare>
      MOVEMENT_PACKET_PREPARE =
          EventTypeService.of(EventAttackInputService.MovementPacketPrepare.class);
  public static final EventTypeService<EventAttackInputService.PostMovementPacket>
      POST_MOVEMENT_PACKET = EventTypeService.of(EventAttackInputService.PostMovementPacket.class);
  public static final EventTypeService<EventAttackInputService.Render> RENDER =
      EventTypeService.of(EventAttackInputService.Render.class);
  public static final EventTypeService<EventAttackInputService.Key> KEY =
      EventTypeService.of(EventAttackInputService.Key.class);
  public static final EventTypeService<EventAttackInputService.Packet> PACKET =
      EventTypeService.of(EventAttackInputService.Packet.class);
  public static final EventTypeService<EventAttackInputService.OutgoingPacketAccepted>
      OUTGOING_PACKET_ACCEPTED =
          EventTypeService.of(EventAttackInputService.OutgoingPacketAccepted.class);
  public static final EventTypeService<EventAttackInputService.Chat> CHAT =
      EventTypeService.of(EventAttackInputService.Chat.class);
  public static final EventTypeService<EventAttackInputService.World> WORLD =
      EventTypeService.of(EventAttackInputService.World.class);
  public static final EventTypeService<EventAttackInputService.WorldRenderPrepare>
      WORLD_RENDER_PREPARE = EventTypeService.of(EventAttackInputService.WorldRenderPrepare.class);
  public static final EventTypeService<EventAttackInputService.WorldRender> WORLD_RENDER =
      EventTypeService.of(EventAttackInputService.WorldRender.class);
  public static final EventTypeService<EventAttackInputService.WorldPostRender> WORLD_POST_RENDER =
      EventTypeService.of(EventAttackInputService.WorldPostRender.class);

  private EventAttackInputService() {}

  public record AttackInput() {
    public static final EventAttackInputService.AttackInput INSTANCE =
        new EventAttackInputService.AttackInput();
  }

  public static final class Chat implements EventIsCancelledHandler {
    private String text;
    private final EventIsCancelledHandler.State state = new EventIsCancelledHandler.State();

    public Chat(String currentText) {
      this.text = currentText;
    }

    public String message() {
      return this.text;
    }

    public void setMessage(String currentText) {
      this.text = currentText;
    }

    @Override
    public boolean isCancelled() {
      return this.state.isCancelled();
    }

    @Override
    public void cancel() {
      this.state.cancel();
    }
  }

  public record GameplayInput() {
    public static final EventAttackInputService.GameplayInput INSTANCE =
        new EventAttackInputService.GameplayInput();
  }

  public static final class Key implements EventIsCancelledHandler {
    private final int count;
    private final int count2;
    private final int count3;
    private final EventIsCancelledHandler.State state2 = new EventIsCancelledHandler.State();

    public Key(int value, int currentValue, int nextValue) {
      this.count = value;
      this.count2 = currentValue;
      this.count3 = nextValue;
    }

    public int keyCode() {
      return this.count;
    }

    public int scanCode() {
      return this.count2;
    }

    public int action() {
      return this.count3;
    }

    public boolean isPress() {
      return this.count3 == 1;
    }

    public boolean isRelease() {
      return this.count3 == 0;
    }

    public boolean isRepeat() {
      return this.count3 == 2;
    }

    @Override
    public boolean isCancelled() {
      return this.state2.isCancelled();
    }

    @Override
    public void cancel() {
      this.state2.cancel();
    }
  }

  public record MovementPacketPrepare() {
    public static final EventAttackInputService.MovementPacketPrepare INSTANCE =
        new EventAttackInputService.MovementPacketPrepare();
  }

  public record OutgoingPacketAccepted(net.minecraft.network.protocol.Packet<?> packet) {}

  public static final class Packet implements EventIsCancelledHandler {
    private final net.minecraft.network.protocol.Packet<?> packet2;
    private final EventAttackInputService.Packet.Direction direction2;
    private final EventIsCancelledHandler.State state3 = new EventIsCancelledHandler.State();

    public Packet(
        net.minecraft.network.protocol.Packet<?> packet,
        EventAttackInputService.Packet.Direction direction) {
      this.packet2 = packet;
      this.direction2 = direction;
    }

    public net.minecraft.network.protocol.Packet<?> packet() {
      return this.packet2;
    }

    public EventAttackInputService.Packet.Direction direction() {
      return this.direction2;
    }

    public boolean isIncoming() {
      return this.direction2 == EventAttackInputService.Packet.Direction.IN;
    }

    public boolean isOutgoing() {
      return this.direction2 == EventAttackInputService.Packet.Direction.OUT;
    }

    @Override
    public boolean isCancelled() {
      return this.state3.isCancelled();
    }

    @Override
    public void cancel() {
      this.state3.cancel();
    }

    public enum Direction {
      IN,
      OUT;

      private static EventAttackInputService.Packet.Direction[] $values() {
        return new EventAttackInputService.Packet.Direction[] {IN, OUT};
      }
    }
  }

  public record PlayerPositionCorrected() {
    public static final EventAttackInputService.PlayerPositionCorrected INSTANCE =
        new EventAttackInputService.PlayerPositionCorrected();
  }

  public record PostMovementPacket() {
    public static final EventAttackInputService.PostMovementPacket INSTANCE =
        new EventAttackInputService.PostMovementPacket();
  }

  public record PreMovementPacket() {
    public static final EventAttackInputService.PreMovementPacket INSTANCE =
        new EventAttackInputService.PreMovementPacket();
  }

  public record PrePlayerMovement() {
    public static final EventAttackInputService.PrePlayerMovement INSTANCE =
        new EventAttackInputService.PrePlayerMovement();
  }

  public record Render(Object context, float tickDelta) {}

  public record Tick() {
    public static final EventAttackInputService.Tick INSTANCE = new EventAttackInputService.Tick();
  }

  public record World(@Nullable ClientLevel world) {
    public boolean isJoining() {
      return this.world != null;
    }

    public boolean isLeaving() {
      return this.world == null;
    }
  }

  public record WorldPostRender(EventAttackInputService.WorldRender frame) {}

  public record WorldRender(
      Camera camera,
      Vec3 cameraPos,
      Matrix4f viewMatrix,
      Matrix4f projectionMatrix,
      float tickDelta) {
    public WorldRender(Camera camera, Matrix4f matrix4f, Matrix4f currentMatrix4f, float value) {
      this(camera, createVec3(camera), matrix4f, currentMatrix4f, value);
    }

    private static Vec3 createVec3(Camera camera) {
      if (camera == null) {
        return new Vec3(0.0, 0.0, 0.0);
      }

      for (String text : new String[] {"position", "getPosition"}) {
        try {
          if (camera.getClass().getMethod(text).invoke(camera) instanceof Vec3 vec3) {
            return vec3;
          }
        } catch (ReflectiveOperationException reflectiveOperationException) {
        }
      }

      return new Vec3(0.0, 0.0, 0.0);
    }
  }

  public record WorldRenderPrepare(float tickDelta) {}
}
