package Server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChannelList {

    private static final String _class = ChannelList.class.getSimpleName();
    private List<Channel> channels = new ArrayList<>();

    public ChannelList() {}

    public void addChannel(Channel channel) {
        if (channel == null)
            throw new NullPointerException();
        channels.add(channel);
    }

    public boolean checkIfExists(String channelName) {
        for (Channel c : channels)
            if(c.getChannelName().equals(channelName))
                return true;
        return false;
    }

    public Channel getChannel(String channelName) {
        for (Channel c : channels)
            if(c.getChannelName().equals(channelName))
                return c;
        return null;
    }

    public List<Channel> getChannels() { return channels; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Get messages
        JSONArray arr = new JSONArray();
        for (Channel ch : channels)
            arr.add(ch.toJSON());

        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channels",  arr);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static ChannelList fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get messages from JSONArray
            JSONArray arr = (JSONArray)obj.get("channels");
            List<Channel> channels = new ArrayList<>();
            for (Object ch_obj : arr)
                channels.add(Channel.fromJSON(ch_obj));

            // Get values and create object
            return new ChannelList();

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    public void save() {
        JSONObject obj = (JSONObject) this.toJSON();
        try (FileWriter file = new FileWriter("Server/Data/Channels.json")) {
            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void read() {

        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("Server/Data/Channels.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            List<Channel> temp = ChannelList.fromJSON(jsonObject).getChannels();
            temp = (List) ((JSONObject) jsonObject).get("channels");


            /*System.out.println(jsonObject);

            String name = (String) jsonObject.get("name");
            System.out.println(name);

            long age = (Long) jsonObject.get("age");
            System.out.println(age);

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
