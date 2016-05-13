package copyeverything.tk.copyeverything;

import android.os.Build;

import java.util.Date;
import java.util.UUID;

/**
 * Created by James on 5/8/2016.
 */

public class Clip<T> {
    UUID clipId;
    T content;
    long timestamp;
    String device;

    public Clip (T content) {
        this.clipId = UUID.randomUUID();
        this.content = content;
        this.timestamp = new Date().getTime();
        this.device = Build.MODEL == null ? "Unknown" : Build.MODEL;
    }

    //TODO: This could be prone to device spoofing
    public Clip (String id, T content, long ts, String device) {
        this.clipId = UUID.fromString(id);
        this.content = content;
        this.timestamp = ts;
        this.device = device;
    }

    //Equality uses UUIDs
    public boolean equals(Clip c) {
        return clipId.equals(c.clipId);
    }
}
