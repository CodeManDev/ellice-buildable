package dev.felix.ellice.feature.notebot;

import dev.felix.ellice.feature.notebot.NotebotPositionData;
import dev.felix.ellice.feature.notebot.NotebotTitleService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public record NotebotChannelData(List<Channel> channels, List<Hit> hits, int tuningClicks, int matchedPercent) {
    public NotebotChannelData {
        channels = List.copyOf(channels);
        hits = List.copyOf(hits);
    }

    public static NotebotChannelData create(NotebotTitleService notebotTitleService, List<NotebotPositionData> list, boolean bl, int n) {
        int matchedNotes = 0;
        if (n < 1 || n > 3) {
            throw new IllegalArgumentException("Voices must be 1\u20133");
        }
        List<NotebotPositionData> list2 = list.stream().filter(NotebotPositionData::melodic).distinct().limit(64L).toList();
        if (list2.isEmpty()) {
            throw new IllegalArgumentException("No reachable melodic note blocks.");
        }
        if (notebotTitleService.notes().isEmpty()) {
            throw new IllegalArgumentException("No clear pitches found in this audio.");
        }
        ArrayList<NotebotTitleService.Note> arrayList = new ArrayList<NotebotTitleService.Note>();
        int n2 = -1;
        int n3 = 0;
        for (NotebotTitleService.Note object2 : notebotTitleService.notes()) {
            if (object2.tick() != n2) {
                n2 = object2.tick();
                n3 = 0;
            }
            if (n3++ >= n) continue;
            arrayList.add(object2);
        }
        double[] noteWeights = new double[128];
        for (NotebotTitleService.Note note : arrayList) {
            int n4 = note.midi();
            noteWeights[n4] += note.strength();
        }
        ArrayList<Channel> arrayList2 = new ArrayList<Channel>();
        if (!bl) {
            for (NotebotPositionData notebotPositionData : list2) {
                arrayList2.add(new Channel(notebotPositionData, notebotPositionData.pitch()));
            }
        } else {
            double[] dArray = new double[128];
            Arrays.fill(dArray, Double.longBitsToDouble(4652007308841189376L));
            HashSet<NotebotPositionData.Position> hashSet = new HashSet<NotebotPositionData.Position>();
            while (hashSet.size() < list2.size()) {
                double d = Double.longBitsToDouble(4517329193108106637L);
                Channel channel2 = null;
                for (NotebotPositionData notebotPositionData : list2) {
                    if (hashSet.contains(notebotPositionData.position())) continue;
                    for (int i = 0; i <= 24; ++i) {
                        double d2 = 0.0;
                        for (int j = 0; j < noteWeights.length; ++j) {
                            if (noteWeights[j] == 0.0) continue;
                            d2 += noteWeights[j] * Math.max(0.0, dArray[j] - NotebotChannelData.mc9cfbc0owbx(j, notebotPositionData.baseMidi() + i));
                        }
                        if (!((d2 -= (double)notebotPositionData.clicksTo(i) * Double.longBitsToDouble(4502148214488346440L)) > d)) continue;
                        d = d2;
                        channel2 = new Channel(notebotPositionData, i);
                    }
                }
                if (channel2 != null) {
                    arrayList2.add(channel2);
                    hashSet.add(channel2.block().position());
                    for (int i = 0; i < dArray.length; ++i) {
                        dArray[i] = Math.min(dArray[i], NotebotChannelData.mc9cfbc0owbx(i, channel2.midi()));
                    }
                    continue;
                }
                break;
            }
        }
        LinkedHashMap<Long, Hit> linkedHashMap = new LinkedHashMap<Long, Hit>();
        int n5 = 0;
        for (NotebotTitleService.Note note : arrayList) {
            int n6 = 0;
            for (int i = 1; i < arrayList2.size(); ++i) {
                if (!(NotebotChannelData.mc9cfbc0owbx(note.midi(), ((Channel)arrayList2.get(i)).midi()) < NotebotChannelData.mc9cfbc0owbx(note.midi(), ((Channel)arrayList2.get(n6)).midi()))) continue;
                n6 = i;
            }
            if (Math.floorMod(note.midi() - ((Channel)arrayList2.get(n6)).midi(), 12) == 0) {
                ++matchedNotes;
            }
            long l = (long)note.tick() << 32 | (long)n6;
            linkedHashMap.putIfAbsent(l, new Hit(note.tick(), n6, note.midi(), note.strength()));
        }
        int n7 = arrayList2.stream().mapToInt(channel -> channel.block().clicksTo(channel.targetPitch())).sum();
        return new NotebotChannelData(arrayList2, linkedHashMap.values().stream().sorted(Comparator.comparingInt(Hit::tick).thenComparing(Comparator.comparingDouble(Hit::strength).reversed())).toList(), n7, Math.round((float)matchedNotes * Float.intBitsToFloat(1120403456) / (float)arrayList.size()));
    }

    private static double mc9cfbc0owbx(int n, int n2) {
        int n3 = Math.abs(n - n2);
        int n4 = Math.min(n3 % 12, 12 - n3 % 12);
        return (double)n4 * Double.longBitsToDouble(0x4010000000000000L) + (double)n3 * Double.longBitsToDouble(4590429028186199163L);
    }

    public record Channel(NotebotPositionData block, int targetPitch) {
        public int midi() {
            return this.block.baseMidi() + this.targetPitch;
        }
    }

    public record Hit(int tick, int channel, int sourceMidi, float strength) {
    }
}
