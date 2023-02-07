package Server;

import org.json.simple.JSONObject;

public class Message {
    // class name to be used as tag in JSON representation
    private static final String _class = Message.class.getSimpleName();

    private final String body;
    private final String author;
    private final long timestamp;

    // Constructor; throws NullPointerException if arguments are null
    public Message(String body, String author, long timestamp) {
        if (body == null || author == null)
            throw new NullPointerException();
        this.body      = body;
        this.author    = author;
        this.timestamp = timestamp;
    }

    public String getBody()      { return body; }
    public String getAuthor()    { return author; }
    public long   getTimestamp() { return timestamp; }

    public String toString() {
        return author + ": " + body + " (" + timestamp + ")";
    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("body",      body);
        obj.put("author",    author);
        obj.put("timestamp", timestamp);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static Message fromJSON(Object val) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String body    = (String)obj.get("body");
            String author  = (String)obj.get("author");
            long timestamp = (long)obj.get("timestamp");
            return new Message(body, author, timestamp);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
