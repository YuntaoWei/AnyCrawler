package us.codecraft.webmagic.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

/**
 * @author code4crafter@gmail.com
 *         Date: 17/3/27
 */
public abstract class HttpClientUtils {

    public static Map<String,List<String>> convertHeaders(Headers headers){
        Map<String,List<String>> results = new HashMap<String, List<String>>();
        Set<String> names = headers.names();
        for (String name : names
             ) {
            List<String> list = results.get(name);
            if (list == null) {
                list = new ArrayList<String>();
                results.put(name, list);
            }
            list.add(headers.get(name));
        }
        return results;
    }
}
