/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonAdapters;

import DAO.Sensor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author Nicolas
 */
public class SensorAdapter implements JsonSerializer<Sensor> {
  @Override
  public JsonElement serialize(Sensor s, Type type, JsonSerializationContext jsc) {
         JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("s_id", s.getId());
    jsonObject.addProperty("s_name", s.getName());
    jsonObject.addProperty("s_description", s.getDescription());
    jsonObject.addProperty("s_type_name", s.getType().getName());
    return jsonObject;      
  }

   
}
