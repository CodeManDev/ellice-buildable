package dev.felix.ellice.feature.scaffold;

import java.util.Objects;

public final class ScaffoldBeginService {
  private ScaffoldBeginService() {}

  public static ScaffoldBeginService.State begin(
      long longValue,
      int value,
      ScaffoldDecisionTracker.TargetKey targetKey,
      long currentLongValue,
      int currentValue,
      int nextValue) {
    if (currentLongValue >= 0L && currentValue >= 1 && nextValue >= 1) {
      long nextLongValue = Math.addExact(currentLongValue, (long) currentValue + nextValue);
      return new ScaffoldBeginService.State(
          new ScaffoldBeginService.Key(longValue, value, targetKey),
          ScaffoldBeginService.Phase.GRACE,
          nextLongValue,
          Long.MIN_VALUE);
    } else {
      throw new IllegalArgumentException("Invalid placement reconciliation timing");
    }
  }

  public static ScaffoldBeginService.Transition advance(
      ScaffoldBeginService.State currentState,
      long longValue,
      ScaffoldBeginService.Evidence evidence,
      ScaffoldBeginService.TargetState currentTargetState) {
    Objects.requireNonNull(currentState, "state");
    Objects.requireNonNull(evidence, "exactTargetEvidence");
    Objects.requireNonNull(currentTargetState, "targetState");
    if (!currentState.active()) {
      return new ScaffoldBeginService.Transition(currentState, ScaffoldBeginService.Action.NONE);
    }

    if (evidence == ScaffoldBeginService.Evidence.ACCEPTED) {
      return createTransition(currentState, ScaffoldBeginService.Action.ACCEPT);
    }

    if (evidence == ScaffoldBeginService.Evidence.REJECTED) {
      return createTransition(currentState, ScaffoldBeginService.Action.REJECT);
    }

    if (currentState.phase() == ScaffoldBeginService.Phase.GRACE) {
      if (longValue < currentState.reconcileNotBeforeTick()) {
        return new ScaffoldBeginService.Transition(currentState, ScaffoldBeginService.Action.NONE);
      }

      ScaffoldBeginService.State nextState =
          new ScaffoldBeginService.State(
              currentState.key(),
              ScaffoldBeginService.Phase.PREDICTION_RESOLUTION_REQUESTED,
              currentState.reconcileNotBeforeTick(),
              longValue);
      return new ScaffoldBeginService.Transition(
          nextState, ScaffoldBeginService.Action.REQUEST_PREDICTION_RESOLUTION);
    } else {
      if (longValue <= currentState.resolutionRequestedTick()) {
        return new ScaffoldBeginService.Transition(currentState, ScaffoldBeginService.Action.NONE);
      }

      return switch (currentTargetState) {
        case SUPPORTING -> createTransition(currentState, ScaffoldBeginService.Action.ACCEPT);
        case REPLACEABLE, OTHER ->
            createTransition(currentState, ScaffoldBeginService.Action.TERMINAL_ABORT);
      };
    }
  }

  public static boolean matchesAcknowledgement(ScaffoldBeginService.State state, int value) {
    return state != null && state.active() && value >= state.key().sequence();
  }

  public static boolean matchesTarget(
      ScaffoldBeginService.State state, ScaffoldDecisionTracker.TargetKey targetKey) {
    return state != null && state.active() && state.key().target().equals(targetKey);
  }

  public static ScaffoldBeginService.State retire(ScaffoldBeginService.State currentState) {
    Objects.requireNonNull(currentState, "state");
    return new ScaffoldBeginService.State(
        currentState.key(),
        ScaffoldBeginService.Phase.RETIRED,
        currentState.reconcileNotBeforeTick(),
        currentState.resolutionRequestedTick());
  }

  public static ScaffoldBeginService.Transition terminalAbort(
      ScaffoldBeginService.State currentState) {
    Objects.requireNonNull(currentState, "state");
    return !currentState.active()
        ? new ScaffoldBeginService.Transition(currentState, ScaffoldBeginService.Action.NONE)
        : createTransition(currentState, ScaffoldBeginService.Action.TERMINAL_ABORT);
  }

  private static ScaffoldBeginService.Transition createTransition(
      ScaffoldBeginService.State state, ScaffoldBeginService.Action action) {
    return new ScaffoldBeginService.Transition(retire(state), action);
  }

  public enum Action {
    NONE,
    REQUEST_PREDICTION_RESOLUTION,
    ACCEPT,
    REJECT,
    TERMINAL_ABORT;

    private static ScaffoldBeginService.Action[] $values() {
      return new ScaffoldBeginService.Action[] {
        NONE, REQUEST_PREDICTION_RESOLUTION, ACCEPT, REJECT, TERMINAL_ABORT
      };
    }
  }

  public enum Evidence {
    NONE,
    ACCEPTED,
    REJECTED;

    private static ScaffoldBeginService.Evidence[] $values() {
      return new ScaffoldBeginService.Evidence[] {NONE, ACCEPTED, REJECTED};
    }
  }

  public record Key(long token, int sequence, ScaffoldDecisionTracker.TargetKey target) {
    public Key(long token, int sequence, ScaffoldDecisionTracker.TargetKey target) {
      if (token > 0L && sequence >= 0) {
        Objects.requireNonNull(target, "target");
        this.token = token;
        this.sequence = sequence;
        this.target = target;
      } else {
        throw new IllegalArgumentException("Reconciliation requires an issued token and sequence");
      }
    }
  }

  public enum Phase {
    GRACE,
    PREDICTION_RESOLUTION_REQUESTED,
    RETIRED;

    private static ScaffoldBeginService.Phase[] $values() {
      return new ScaffoldBeginService.Phase[] {GRACE, PREDICTION_RESOLUTION_REQUESTED, RETIRED};
    }
  }

  public record State(
      ScaffoldBeginService.Key key,
      ScaffoldBeginService.Phase phase,
      long reconcileNotBeforeTick,
      long resolutionRequestedTick) {
    public State(
        ScaffoldBeginService.Key key,
        ScaffoldBeginService.Phase phase,
        long reconcileNotBeforeTick,
        long resolutionRequestedTick) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(phase, "phase");
      if (reconcileNotBeforeTick < 0L) {
        throw new IllegalArgumentException("Reconciliation deadline must be non-negative");
      }

      if (phase == ScaffoldBeginService.Phase.PREDICTION_RESOLUTION_REQUESTED
          && resolutionRequestedTick < 0L) {
        throw new IllegalArgumentException("Requested prediction resolution needs its exact tick");
      }

      this.key = key;
      this.phase = phase;
      this.reconcileNotBeforeTick = reconcileNotBeforeTick;
      this.resolutionRequestedTick = resolutionRequestedTick;
    }

    public boolean active() {
      return this.phase != ScaffoldBeginService.Phase.RETIRED;
    }
  }

  public enum TargetState {
    SUPPORTING,
    REPLACEABLE,
    OTHER;

    private static ScaffoldBeginService.TargetState[] $values() {
      return new ScaffoldBeginService.TargetState[] {SUPPORTING, REPLACEABLE, OTHER};
    }
  }

  public record Transition(ScaffoldBeginService.State state, ScaffoldBeginService.Action action) {
    public Transition(ScaffoldBeginService.State state, ScaffoldBeginService.Action action) {
      Objects.requireNonNull(state, "state");
      Objects.requireNonNull(action, "action");
      this.state = state;
      this.action = action;
    }
  }
}
