import java.util.*;


public class VirtualMemory {
    public static int fifo(String pageRefs) {
        int PageFrames = 3;
        int PageFaults = 0;
        Queue<Integer> frames = new LinkedList<Integer>();

        String[] pageReferences = pageRefs.split(","); //  Split the page reference string into an array based off comma
        for (int i = 0; i < pageReferences.length; i++) { // increment the loop
            int pageRef = Integer.parseInt(pageReferences[i]); //takes integer value from the string

            // Check if the page reference exists
            if (frames.contains(pageRef)) {
                continue;
            }

            // If there is a free frame, add the page reference to it
            if (frames.size() < PageFrames) {
                frames.add(pageRef);
                PageFaults++; // increment page faults since it wasnt in memory before
            } else {
                // Otherwise, remove the oldest page and add the current page reference to the back of the queue
                frames.remove();
                frames.add(pageRef);
                PageFaults++;
            }
        }

        return PageFaults; // return number of page faults

    }

    public static int opt(String pageRefs) {
        int PageFrames = 3;
        int PageFaults = 0;
        Queue<Integer> frames = new LinkedList<>(); // Queue to hold the page frames

        String[] pageReferences = pageRefs.split(",");
        int[] pages = new int[pageReferences.length]; // Array to hold the page references as integers
        for (int i = 0; i < pageReferences.length; i++) {
            pages[i] = Integer.parseInt(pageReferences[i]); // Convert the page reference string to an integer using parseInt
        }

        for (int i = 0; i < pages.length; i++) {
            int pageRef = pages[i]; // Get page ref  at the current index

            // If page ref exists currently
            if (frames.contains(pageRef)) {
                continue; // Move to the next page reference if the current one is already in memory
            }

            // If there is a free frame, add the page reference to it
            if (frames.size() < PageFrames) {
                frames.add(pageRef); // Add the page reference to the frames queue
                PageFaults++; // Increment   the number of page faults
            } else {
                // Find the page in memory that will not be referenced for the longest period of time
                int farIndex = -1; // Index of the farthest page reference
                int pageToRemove = -1;

                for (Integer frame : frames) {
                    int farthest = -1; // Index of the farthest occurrence of the frame
                    for (int j = i + 1; j < pages.length; j++) { // Search for the farthest occurrence of the frame
                        if (pages[j] == frame) {
                            farthest = j;
                            break;
                        }
                    }
                    if (farthest == -1) { // If frame isnt referenced
                        pageToRemove = frame; // Set the page reference to remove
                        break;
                    }
                    if (farthest > farIndex) { // If the farthest occurrence is greater than  farIndex
                        farIndex = farthest; // Update the farIndex
                        pageToRemove = frame; // Update the page reference to remove
                    }
                }

                frames.remove(pageToRemove); // Remove the page reference that will not be referenced for the longest period
                frames.add(pageRef); // Add the current page reference to memory
                PageFaults++; // Increase the number of page faults
            }
        }

        return PageFaults; // Return the total number of page faults
    }

    public static int lru(String pageRefs) {
        int pageFrames = 3;
        int pageFaults = 0;
        List<Integer> frames = new ArrayList<>();

        String[] pageReferences = pageRefs.split(",");
        for (String pageRefString : pageReferences) {
            int pageRef = Integer.parseInt(pageRefString);

            // Check if the page reference is already in memory
            if (frames.contains(pageRef)) {
                frames.remove((Integer) pageRef);  // Remove the page reference from its current position
                frames.add(pageRef);  // Add the page reference to the end of the list (representing the most recently used)
            } else {
                // If there is a free frame, add the page reference to it
                if (frames.size() < pageFrames) {
                    frames.add(pageRef);
                } else {
                    frames.remove(0);  // Remove the least recently used page (first element in the list)
                    frames.add(pageRef);  // Add the page reference to the end of the list
                }
                pageFaults++;  // Increment the number of page faults
            }
        }

        return pageFaults;
    }


    public static int SEC(String pageRefs) {
        int PageFrames = 3;
        int PageFaults = 0;
        List<Integer> frames = new ArrayList<>(); // ArrayList to hold the page frames
        Map<Integer, Boolean> referenceBits = new HashMap<>(); // Map to track reference bits for each frame
        int pointer = 0; // Use Pointer to keep track  for page replacement

        String[] pageReferences = pageRefs.split(","); // Split the page reference string into an array based off comma
        for (String pageRefString : pageReferences) {
            int pageRef = Integer.parseInt(pageRefString); // Convert the page reference to an integer

            if (!frames.contains(pageRef)) { // If the page ref isnt found then its a page fault
                if (frames.size() < PageFrames) { // If there are free frames available, add page ref to frames
                    frames.add(pageRef);
                } else {
                    while (referenceBits.getOrDefault(frames.get(pointer), false)) {
                        // While the reference bit of the page at the current pointer is true
                        referenceBits.put(frames.get(pointer), false); // Set the reference bit to false
                        pointer = (pointer + 1) % PageFrames; // Move the pointer to the next frame
                    }

                    frames.set(pointer, pageRef); // Replace the page at the pointer position with the new page reference
                    pointer = (pointer + 1) % PageFrames; // Move the pointer to the next frame
                }
                PageFaults++; // Increase the number of page faults
            } else {
                referenceBits.put(pageRef, true); // Set the reference bit to true for the existing page reference
            }
        }

        return PageFaults; // Return the total number of page faults
    }


    public static void main(String[] args){
        String[] referenceStrings = {
                "2,6,9,2,4,2,1,7,3,0,5,2,1,2,9,5,7,3,8,5",
                "0,6,3,0,2,6,3,5,2,4,1,3,0,6,1,4,2,3,5,7",
                "3,1,4,2,5,4,1,3,5,2,0,1,1,0,2,3,4,5,0,1",
                "4,2,1,7,9,8,3,5,2,6,8,1,0,7,2,4,1,3,5,8",
                "0,1,2,3,4,4,3,2,1,0,0,1,2,3,4,4,3,2,1,0"
        };


        // loop each reference string to display each of them
        for (String referenceString : referenceStrings) {
            System.out.println("Page-Reference String: " + referenceString);

            // Showcase Results from the Paging Algorithms
            System.out.println("OPT: " + opt(referenceString) + " " + "FIFO:" + fifo(referenceString) + " " + "LRU:" + lru(referenceString) + " " + "SEC:" + SEC(referenceString));


        }
    }
}

