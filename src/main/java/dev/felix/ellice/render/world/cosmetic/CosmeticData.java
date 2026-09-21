package dev.felix.ellice.render.world.cosmetic;

import dev.felix.ellice.event.EventAttackInputService;

public record CosmeticData(
    EventAttackInputService.WorldRender event,
    int framebufferWidth,
    int framebufferHeight,
    double timeSeconds) {}
