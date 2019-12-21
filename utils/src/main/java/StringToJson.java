import com.alibaba.fastjson.JSON;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: pengwei
 * @date: 2019/12/21
 */
public class StringToJson {

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void main(String[] args) {
        //JSONObject jsonObject =  JSON.parseObject();

        Map<String, Object> map = new LinkedHashMap<>();
        getJosn1(map);


    }


    private static void getJosn1(Map<String, Object> map) {
        String sJosn = "installBelong: \n" +
                "name: \n" +
                "tel: \n" +
                "brand: \n" +
                "model: \n" +
                "frame_no: \n" +
                "plate_no: \n" +
                "planInstallTime: \n" +
                "dealer: \n" +
                "contact_name: \n" +
                "contact_tel: \n" +
                "install_remark: \n" +
                "install_province: \n" +
                "install_city: \n" +
                "install_location: \n" +
                "contact_status: \n" +
                "deviceposition:\n" +
                "customerId: ";

        String s = replaceBlank(sJosn);
        String[] split = s.split(":");
        for (int i = 0; i < split.length; i++) {
            map.put(split[i], "");
        }

        String json = JSON.toJSONString(map);
        System.out.println(json);
    }

}
