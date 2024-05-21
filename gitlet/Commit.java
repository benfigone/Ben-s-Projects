package gitlet;
import java.io.IOException;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static gitlet.Utils.sha1;


/** Represents a gitlet commit object.
 *
 *  @author Victor Cruz Ramos and Ben Figone
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;


    private String timeStamp;
    private Commit parent;

    private HashMap<String, String> blobcommit;

    private final String commitId;

    public Commit(String msg, Commit parent,
                  HashMap<String, String> blobcommit) throws IOException {
        //create new hashmap for each commit
        this.message = msg;
        this.parent = parent;
        this.blobcommit = blobcommit;
        Object[] items = {message, parent, blobcommit};
        this.commitId = sha1(Utils.serialize(items));
        if (this.parent == null) {
            //this.timeStamp = "00:00:00 UTC, Thursday, 1 January 1970";
            LocalDateTime timeRn = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
            OffsetDateTime time = timeRn.atOffset(ZoneOffset.UTC);
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy ZZZ");
            timeStamp = time.format(formatter);
        } else {
            // new LocalDateTime(0);
            LocalDateTime timeRn = LocalDateTime.now();
            OffsetDateTime time = timeRn.atOffset(ZoneOffset.UTC);
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy ZZZ");
            timeStamp = time.format(formatter);

        }
    }

    public String getMessage() {
        return this.message;
    }



    public String getDate() {
        return this.timeStamp;
    }

    public String getCommit() {
        return this.commitId;
    }

    public String getBlob(String name) {
        return blobcommit.get(name);
    }

    public HashMap<String, String> getBlobHash() {
        return blobcommit;
    }


    public Commit getParent() {
        return this.parent;
    }
}
