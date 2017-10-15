package test;

import com.ailk.biapp.ci.localization.sichuan.util.PasswordUtil;
import com.asiainfo.biframe.utils.config.Configure;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    static public void main(String argc[]) throws Exception {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateStr=dateFormat.format(date);
        //ai_shencheng  64D7A2854F7F89841A1AF2FDE9047CDA
        //ai_duhongzhi  b71b8153a7a286488e6407614752f5ad
        //ai_lujianguo  7876139AB8326E1EDDE98989D16DC94A
        String str1="loginno=ai_duhongzhi&pwd=b71b8153a7a286488e6407614752f5ad &op_time="+dateStr;
        String str2="loginno=ai_shencheng&pwd=64D7A2854F7F89841A1AF2FDE9047CDA &op_time="+dateStr;
        System.out.println(str1);
        try {
            System.out.println("àê"+"http://10.109.215.28:9000/coc/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
            System.out.println("http://10.109.215.30:9000/coc/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
            System.out.println("http://10.109.215.30:8060/COC/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
            System.out.println("http://localhost:8080/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
            System.out.println("http://10.113.254.17:8080/coc/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
            System.out.println("http://10.113.254.17:8080/coc/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str2));
            System.out.println("http://10.113.254.17:8080/coc_test/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str2));
//            System.out.println("http://10.109.215.28:9000/govcoc/ci/ciIndexAction!labelIndex.ai2do?param="+PasswordUtil.encrypt(str1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
