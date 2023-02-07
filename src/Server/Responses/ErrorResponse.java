package Server.Responses;

import org.json.simple.JSONObject;

public class ErrorResponse extends Response {

    private static final String _class = ErrorResponse.class.getSimpleName();
    private String error;

    public ErrorResponse(String error) {
        // check for null
        if (error == null)
            throw new NullPointerException();
        this.error = error;
    }

    public String getError() { return error; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",   _class);
        obj.put("error",     error);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static ErrorResponse fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String error = (String)obj.get("error");
            return new ErrorResponse(error);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
