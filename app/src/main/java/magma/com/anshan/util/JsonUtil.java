package magma.com.anshan.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class JsonUtil {
    private final static String TAG = "JsonUtil";
    /**
     * 将对象转换为json字符串
     * @param obj   对象，可以是实体对象，List对象，Map对象等
     * @return   json字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("illegal argument");
        }

        return new Gson().toJson(obj);
    }

    /**
     * 将json字符串转换为实体对象
     * @param jsonString   json字符串
     * @param cls   实体类
     * @param <T>  泛型参数
     * @return   实体对象
     */
    public static <T> T getEntity(String jsonString, final Class<T> cls) {
        T t;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, jsonString + " 无法转换为 " + cls.getSimpleName() + " 对象");
            return null;
        }

        return t;
    }

    /**
     * 将json字符串转换为List对象
     * @param jsonString   json字符串
     * @param cls   实体类
     * @param <T>   泛型参数
     * @return   实体List对象
     */
    public static <T> List<T> getEntityList(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();

        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();

        for (final JsonElement elem : array) {
            list.add(new Gson().fromJson(elem, cls));
        }

        return list;
    }
}