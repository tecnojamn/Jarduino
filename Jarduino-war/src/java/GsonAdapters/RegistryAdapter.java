/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonAdapters;

import DAO.Registry;
import DAO.Sensor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Nicolas
 */
public class RegistryAdapter implements JsonSerializer<Registry> {

    @Override
    public JsonElement serialize(Registry r, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("r_id", r.getId());
        jsonObject.addProperty("r_sensor_id", r.getIdsensor());
        jsonObject.addProperty("r_value", r.getValue());
        jsonObject.addProperty("r_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getDate()));
        jsonObject.addProperty("r_date_ts", r.getDate().getTime());
        return jsonObject;
    }
}
