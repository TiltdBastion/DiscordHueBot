import com.google.gson.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author TiltdBastion
 */
public class HueClient {

    private String addressPrefix;
    private String allLightsGroupId;

    public HueClient(String token, String bridgeAddr){
        this.addressPrefix = "http://" + bridgeAddr + "/api/" + token;
        this.allLightsGroupId = checkForAllLightGroup();
    }

    public String getAllLightsGroupId() {
        return allLightsGroupId;
    }

    public void setColor(String id, double x, double y){
        String url = addressPrefix+"/lights/"+id+"/state";
        JsonObject data = new JsonObject();
        data.add("on",new JsonPrimitive(true));
        JsonArray xy = new JsonArray();
        xy.add(new JsonPrimitive(x));
        xy.add(new JsonPrimitive(y));
        data.add("xy",xy);
        HttpUtil.put(url, data.toString());
    }

    public void setGroupColor(String id, double x, double y){
        String url = addressPrefix+"/groups/"+id+"/action";
        JsonObject data = new JsonObject();
        data.add("on",new JsonPrimitive(true));
        JsonArray xy = new JsonArray();
        xy.add(new JsonPrimitive(x));
        xy.add(new JsonPrimitive(y));
        data.add("xy",xy);
        HttpUtil.put(url, data.toString());
    }

    public void startGroupColorloop(String groupId){
        String url = addressPrefix+"/groups/"+groupId+"/action";
        JsonObject data = new JsonObject();
        data.add("on",new JsonPrimitive(true));
        data.add("bri",new JsonPrimitive(150));
        data.add("sat",new JsonPrimitive(254));
        data.add("effect",new JsonPrimitive("colorloop"));
        HttpUtil.put(url, data.toString());
    }

    public void stopColorloop(String id){
        String url = addressPrefix+"/lights/"+id+"/state";
        JsonObject data = new JsonObject();
        data.add("on",new JsonPrimitive(true));
        data.add("effect",new JsonPrimitive("none"));
        HttpUtil.put(url, data.toString());
    }

    public void stopGroupColorloop(String groupId){
        String url = addressPrefix+"/groups/"+groupId+"/action";
        JsonObject data = new JsonObject();
        data.add("on",new JsonPrimitive(true));
        data.add("effect",new JsonPrimitive("none"));
        HttpUtil.put(url, data.toString());
    }

    public void blinkGroup(){
        String url = addressPrefix+"/groups/"+ allLightsGroupId +"/action";
        JsonObject data = new JsonObject();
        data.add("alert",new JsonPrimitive("select"));
        HttpUtil.put(url, data.toString());
    }

    public String getLights(){
        String data = HttpUtil.get(addressPrefix+"/lights");
        JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
        StringBuilder builder = new StringBuilder();
        dataJson.entrySet().forEach(element ->{
            builder.append(element.getKey());
            builder.append(" - ");
            builder.append(element.getValue().getAsJsonObject().get("name").toString().replace("\"",""));
            builder.append("\n");
        });
        return builder.toString();
    }

    public ArrayList<String> getLightsId(){
        String data = HttpUtil.get(addressPrefix+"/lights");
        JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
        ArrayList<String> ids = new ArrayList<>();
        dataJson.entrySet().forEach(element -> ids.add(element.getKey()));
        return ids;
    }

    public String checkForAllLightGroup(){
        String address = addressPrefix+"/groups";
        String groups = HttpUtil.get(address);
        JsonObject groupsJson = new JsonParser().parse(groups).getAsJsonObject();
        for (Map.Entry<String, JsonElement> element : groupsJson.entrySet()) {
            if(element.getValue().getAsJsonObject().get("lights").getAsJsonArray().size() == this.getLightsId().size()){
                return element.getKey();
            }
        }
        JsonObject groupCreationJson = new JsonObject();
        JsonArray lightsIds = new JsonArray();
        getLightsId().forEach(id -> lightsIds.add(new JsonPrimitive(id)));
        groupCreationJson.add("lights", lightsIds);
        groupCreationJson.add("name",new JsonPrimitive("allLights"));
        String response = HttpUtil.post(address,groupCreationJson.toString());
        return new JsonParser().parse(response).getAsJsonArray().get(0).getAsJsonObject().get("success").getAsJsonObject().get("id").getAsString();
    }

    public double[] rgbToXY(double red, double green, double blue){

        double normalizedRed = red/255;
        double normalizedGreen = green/255;
        double normalizedBlue = blue/255;


        red = normalizedRed>0.04045 ? Math.pow((normalizedRed + 0.55)/(1.055), 2.4) : normalizedRed/12.92;
        green = normalizedGreen>0.04045 ? Math.pow((normalizedGreen + 0.55)/(1.055), 2.4) : normalizedGreen/12.92;
        blue = normalizedBlue>0.04045 ? Math.pow((normalizedBlue + 0.55)/(1.055), 2.4) : normalizedBlue/12.92;

        double x = red*0.664511 + green*0.154324 + blue*0.162028;
        double y = red*0.283881 + green*0.668433 + blue*0.047685;
        double z = red*0.000000  + green*0.072310 + blue*0.986039;

        double[] xyz = new double[3];

        if(x+y+z == 0){
            xyz[0] = 0;
            xyz[1] = 0;
        }else{
            xyz[0] = x/(x+y+z);
            xyz[1] = y/(x+y+z);
        }
        xyz[2] = Math.round(y*255);

        return xyz;
    }
}
