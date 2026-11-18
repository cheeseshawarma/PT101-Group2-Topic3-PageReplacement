import java.util.List;

// Models & Input Validation - Holds the step by step data structures and validates user inputs.

public class Models {

    // Stores one column (step) of the trace table
    public static class Step {
        public int page;
        public String[] frameValues;
        public String status; // "HIT" or "FAULT"

        public Step(int page, String[] frameValues, String status) {
            this.page = page;
            this.frameValues = frameValues;
            this.status = status;
        }
    }

    // Stores the complete result of an algorithm run
    public static class Result {
        public String name;
        public List<Step> steps;
        public List<Integer> finalFrames;
        public int totalPages;
        public int hits;
        public int faults;

        public Result(String name, List<Step> steps, List<Integer> finalFrames,
                      int totalPages, int hits, int faults) {
            this.name = name;
            this.steps = steps;
            this.finalFrames = finalFrames;
            this.totalPages = totalPages;
            this.hits = hits;
            this.faults = faults;
        }

        public double getHitRate() {
            return (hits * 100.0) / totalPages;
        }

        public double getFaultRate() {
            return (faults * 100.0) / totalPages;
        }
    }

    // Converts user text input into an array of page numbers
    public static int[] getPages(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a page-reference string.");
        }

        String cleanText = text.trim().replace(",", " ");
        String[] parts = cleanText.split("\\s+");
        int[] pages = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            try {
                int num = Integer.parseInt(parts[i]);
                if (num < 0) {
                    throw new IllegalArgumentException("Page numbers cannot be negative: " + num);
                }
                pages[i] = num;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number '" + parts[i] + "'. Use spaces or commas between numbers.");
            }
        }
        return pages;
    }

    // Validates and returns the number of frames
    public static int getFrameCount(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the number of frames.");
        }
        try {
            int count = Integer.parseInt(text.trim());
            if (count < 1 || count > 15) {
                throw new IllegalArgumentException("Number of frames must be between 1 and 15.");
            }
            return count;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Number of frames must be a positive whole number.");
        }
    }

    // Fills empty frame slots with "-" so the table stays aligned
    public static String[] copyFrames(List<Integer> frames, int frameCount) {
        String[] copy = new String[frameCount];
        for (int i = 0; i < frameCount; i++) {
            copy[i] = (i < frames.size()) ? String.valueOf(frames.get(i)) : "-";
        }
        return copy;
    }
}

