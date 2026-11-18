import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIFO and LRU Algorithms

public class FifoLru {

    // First-In, First-Out (FIFO)
    public static Models.Result runFIFO(int[] pages, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        List<Models.Step> steps = new ArrayList<>();
        int index = 0; // Points to the oldest page
        int hits = 0;
        int faults = 0;

        for (int page : pages) {
            String status;
            if (frames.contains(page)) {
                status = "HIT";
                hits++;
            } else {
                status = "FAULT";
                faults++;
                if (frames.size() < frameCount) {
                    frames.add(page);
                } else {
                    frames.set(index, page);
                    index = (index + 1) % frameCount;
                }
            }
            steps.add(new Models.Step(page, Models.copyFrames(frames, frameCount), status));
        }

        return new Models.Result("FIFO", steps, new ArrayList<>(frames), pages.length, hits, faults);
    }

    // Least Recently Used (LRU)
    public static Models.Result runLRU(int[] pages, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        Map<Integer, Integer> lastUsed = new HashMap<>();
        List<Models.Step> steps = new ArrayList<>();
        int hits = 0;
        int faults = 0;

        for (int time = 0; time < pages.length; time++) {
            int page = pages[time];
            String status;

            if (frames.contains(page)) {
                status = "HIT";
                hits++;
            } else {
                status = "FAULT";
                faults++;
                if (frames.size() < frameCount) {
                    frames.add(page);
                } else {
                    // Find the page with the oldest lastUsed time
                    int oldestPage = frames.get(0);
                    int oldestTime = lastUsed.get(oldestPage);

                    for (int f : frames) {
                        if (lastUsed.get(f) < oldestTime) {
                            oldestTime = lastUsed.get(f);
                            oldestPage = f;
                        }
                    }
                    int replacePos = frames.indexOf(oldestPage);
                    frames.set(replacePos, page);
                }
            }

            lastUsed.put(page, time);
            steps.add(new Models.Step(page, Models.copyFrames(frames, frameCount), status));
        }

        return new Models.Result("LRU", steps, new ArrayList<>(frames), pages.length, hits, faults);
    }
}
