


package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.task.DiedComponent;
import dev.felix.ellice.feature.task.TaskAction;
import dev.felix.ellice.feature.task.TaskBlockPosition;
import dev.felix.ellice.feature.task.TaskData;
import dev.felix.ellice.feature.task.TaskOperationHandler;
import dev.felix.ellice.feature.task.TaskStep;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TaskStateController
implements TaskStep {
    private final Set<String> fae0kk206wge;
    private final TaskBlockPosition feb3hlpfqya2;
    private final Set<Integer> f2ugx66317k3 = new HashSet<Integer>();
    private TravelStatusTracker fhmhxjjflt7e;
    private TravelIsInGoalHandler f5cg635pda3r;
    private int fbpwj5trysyn = Integer.MIN_VALUE;
    private long fi13wv724nbd = -1L;
    private long fajh5ckvbjxv;
    private long fwdww8xc4jp = -1L;
    private TaskAction f2dka5ggacy9;
    private String fb266jyyg1r7 = "Collecting drops";

    public TaskStateController(Set<String> set, TaskBlockPosition taskBlockPosition) {
        this.fae0kk206wge = Set.copyOf(set);
        this.feb3hlpfqya2 = taskBlockPosition;
    }

    @Override
    public String name() {
        return "Collect drops";
    }

    @Override
    public String status() {
        return this.fb266jyyg1r7;
    }

    @Override
    public TaskAction tick(long l, TaskOperationHandler taskOperationHandler) {
        TaskBlockPosition taskBlockPosition;
        if (this.f2dka5ggacy9 != null) {
            return this.f2dka5ggacy9;
        }
        TaskData taskData = taskOperationHandler.sense();
        TaskAction.Abort abort = DiedComponent.critical(taskData);
        if (abort != null) {
            this.f2dka5ggacy9 = abort;
            return this.f2dka5ggacy9;
        }
        if (this.fi13wv724nbd < 0L) {
            this.fi13wv724nbd = l;
        }
        if (l - this.fi13wv724nbd > 15000L) {
            this.f2dka5ggacy9 = new TaskAction.Abort("Drops could not be collected");
            return this.f2dka5ggacy9;
        }
        List<TaskOperationHandler.DroppedItem> list = taskOperationHandler.droppedItems().stream().filter(droppedItem -> (this.fae0kk206wge.contains(droppedItem.id()) && !this.f2ugx66317k3.contains(droppedItem.ref()) ? 1 : 0) != 0).filter(droppedItem -> this.feb3hlpfqya2.distSq(droppedItem.x(), droppedItem.y(), droppedItem.z()) <= Double.longBitsToDouble(4639270566145032192L)).sorted(Comparator.comparingDouble(droppedItem -> taskData.player().distanceSquared(droppedItem.x(), droppedItem.y(), droppedItem.z()))).toList();
        if (list.isEmpty()) {
            if (this.fwdww8xc4jp < 0L) {
                this.fwdww8xc4jp = l;
            }
            if (l - this.fwdww8xc4jp < 1500L) {
                return new TaskAction.Wait("Waiting for drops");
            }
            this.f2dka5ggacy9 = this.f2ugx66317k3.isEmpty() ? new TaskAction.Done("Drop area cleared") : new TaskAction.Abort("Some drops are unreachable");
            return this.f2dka5ggacy9;
        }
        this.fwdww8xc4jp = -1L;
        TaskOperationHandler.DroppedItem droppedItem2 = list.stream().filter(droppedItem -> droppedItem.ref() == this.fbpwj5trysyn).findFirst().orElse(list.getFirst());
        TravelIsInGoalHandler.BlockGoal blockGoal = new TravelIsInGoalHandler.BlockGoal((int)Math.floor(droppedItem2.x()), (int)Math.floor(droppedItem2.y()), (int)Math.floor(droppedItem2.z()));
        if (this.fbpwj5trysyn != droppedItem2.ref()) {
            this.fbpwj5trysyn = droppedItem2.ref();
            this.fajh5ckvbjxv = l;
        }
        if ((taskBlockPosition = taskOperationHandler.foliageInWay(droppedItem2.x(), droppedItem2.y(), droppedItem2.z())) != null) {
            this.fhmhxjjflt7e = null;
            this.fajh5ckvbjxv = l;
            this.fb266jyyg1r7 = "Clearing a path to drops";
            return new TaskAction.Mine(taskBlockPosition, taskOperationHandler.blocks().blockId(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z()));
        }
        if (l - this.fajh5ckvbjxv > 5000L) {
            this.f2ugx66317k3.add(this.fbpwj5trysyn);
            this.fbpwj5trysyn = Integer.MIN_VALUE;
            return new TaskAction.Wait("Trying another drop");
        }
        if (this.fhmhxjjflt7e == null || !blockGoal.equals(this.f5cg635pda3r)) {
            this.fhmhxjjflt7e = new TravelStatusTracker(taskOperationHandler.traversal());
            this.f5cg635pda3r = blockGoal;
            this.fhmhxjjflt7e.start(this.f5cg635pda3r, TravelPolicy.legit(false), taskData.player());
        }
        this.fhmhxjjflt7e.tick(taskData.player());
        if (this.fhmhxjjflt7e.status() == TravelStatusTracker.Status.UNAVAILABLE || this.fhmhxjjflt7e.status() == TravelStatusTracker.Status.UNEXPLORED) {
            this.f2ugx66317k3.add(this.fbpwj5trysyn);
            this.fbpwj5trysyn = Integer.MIN_VALUE;
            return new TaskAction.Wait("Drop unreachable \u2014 trying another");
        }
        this.fb266jyyg1r7 = "Collecting " + droppedItem2.count() + "\u00d7 " + droppedItem2.id().replace("minecraft:", "");
        if (Math.hypot(taskData.player().x - droppedItem2.x(), taskData.player().z - droppedItem2.z()) < Double.longBitsToDouble(0x3FE3333333333333L) && Math.abs(taskData.player().y - droppedItem2.y()) < Double.longBitsToDouble(0x3FF3333333333333L)) {
            return new TaskAction.Wait("Waiting for pickup");
        }
        return TaskAction.go(this.f5cg635pda3r, this.fb266jyyg1r7, this.fhmhxjjflt7e.route(), false, TravelPolicy.legit(false));
    }
}

