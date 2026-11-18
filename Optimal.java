import java.util.ArrayList;
import java.util.List;

// Optimal Page Replacement Algorithm

public class Optimal {

    // Optimal (OPT)
    public static Models.Result runOptimal(int[] pages, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        List<Models.Step> steps = new ArrayList<>();
        int hits = 0;
        int faults = 0;

        for (int now = 0; now < pages.length; now++) {
            int page = pages[now];
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
                    int replacePos = findFarthestPage(frames, pages, now);
                    frames.set(replacePos, page);
                }
            }

            steps.add(new Models.Step(page, Models.copyFrames(frames, frameCount), status));
        }

        return new Models.Result("Optimal", steps, new ArrayList<>(frames), pages.length, hits, faults);
    }

    // Looks ahead to find which page in frames is needed farthest in the future
    private static int findFarthestPage(List<Integer> frames, int[] pages, int now) {
        int replacePos = 0;
        int farthest = -1;

        for (int i = 0; i < frames.size(); i++) {
            int currentFramePage = frames.get(i);
            int nextUse = Integer.MAX_VALUE; // Used if page never appears again

            for (int next = now + 1; next < pages.length; next++) {
                if (pages[next] == currentFramePage) {
                    nextUse = next;
                    break;
                }
            }

            if (nextUse > farthest) {
                farthest = nextUse;
                replacePos = i;
            }
        }
        return replacePos;
    }
}
