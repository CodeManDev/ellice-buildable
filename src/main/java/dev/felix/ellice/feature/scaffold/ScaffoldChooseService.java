package dev.felix.ellice.feature.scaffold;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class ScaffoldChooseService {
  private ScaffoldChooseService() {}

  public static Choice choose(List<Stack> list, int n, int n2, Policy policy) {
    List<Stack> list2 =
        list.stream()
            .filter(stack -> (stack.safe() && stack.count() > 0 ? 1 : 0) != 0)
            .filter(stack -> !policy.excluded().contains(stack.item()))
            .filter(stack -> (policy.autoSelect() || stack.slot() == n ? 1 : 0) != 0)
            .filter(
                stack ->
                    (!policy.autoSelect()
                                || !"Fixed Slot".equals(policy.order())
                                || stack.slot() == policy.fixedSlot()
                            ? 1
                            : 0)
                        != 0)
            .filter(
                stack ->
                    (!policy.preferredOnly() || policy.preferred().contains(stack.item()) ? 1 : 0)
                        != 0)
            .toList();
    int n3 = list2.stream().mapToInt(Stack::count).sum();
    int n4 = Math.max(0, n3 - policy.reserve());
    if (n4 == 0) {
      return new Choice(-1, n3, 0, n3 == 0 ? "No usable blocks" : "Reserve reached");
    }
    Comparator<Stack> comparator =
        Comparator.comparingInt(stack -> ScaffoldChooseService.m5lbg7eg741(stack, n, policy));
    comparator =
        comparator
            .thenComparingInt(stack -> stack.slot() == n2 ? 0 : 1)
            .thenComparingInt(
                stack -> "Smallest Stack".equals(policy.order()) ? stack.count() : -stack.count())
            .thenComparingInt(Stack::slot);
    return new Choice(list2.stream().min(comparator).orElseThrow().slot(), n3, n4, "");
  }

  private static int m5lbg7eg741(Stack stack, int n, Policy policy) {
    if ("Held First".equals(policy.order())) {
      return stack.slot() == n ? 0 : 1;
    }
    if (!"Preferred Blocks".equals(policy.order())) {
      return 0;
    }
    int n2 = policy.preferred().indexOf(stack.item());
    return n2 < 0 ? Integer.MAX_VALUE : n2;
  }

  public record Policy(
      boolean autoSelect,
      String order,
      int fixedSlot,
      List<String> preferred,
      boolean preferredOnly,
      Set<String> excluded,
      int reserve) {
    public Policy {
      preferred = List.copyOf(preferred);
      excluded = Set.copyOf(excluded);
    }
  }

  public record Choice(int slot, int total, int available, String reason) {}

  public record Stack(int slot, String item, int count, boolean safe) {}
}
