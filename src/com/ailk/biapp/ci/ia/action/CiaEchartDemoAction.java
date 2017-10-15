package com.ailk.biapp.ci.ia.action;

import com.opensymphony.xwork2.ActionContext;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class CiaEchartDemoAction {
    private static Logger log = Logger.getLogger(CiaEchartDemoAction.class);

    public CiaEchartDemoAction() {
    }

    public String echartDemo() {
        log.debug("-------------------------echart demo page!");
        return "demo";
    }

    public void sendJson(String jsonStr) {
        try {
            ActionContext e = ActionContext.getContext();
            HttpServletResponse response = (HttpServletResponse)e.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
            response.setContentType("text/json; charset=UTF-8");
            response.setHeader("progma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.print(jsonStr);
            out.flush();
            out.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public List<Map<String, Object>> getDataBaseData() {
        Random r = new Random();
        ArrayList list = new ArrayList();

        for(int i = 0; i < 15; ++i) {
            HashMap map = new HashMap();
            map.put("name", "Ê±¼ä-" + i);
            map.put("scatterDimCol", Integer.valueOf(r.nextInt(100)));
            map.put("value1", Integer.valueOf(r.nextInt(100)));
            map.put("value2", Integer.valueOf(r.nextInt(100)));
            list.add(map);
        }

        return list;
    }
}
