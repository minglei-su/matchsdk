package demo;

import com.pboc.sdk.Entity;
import com.pboc.sdk.Query;

/**
 * Created by mingleis on 2014/12/8.
 */
public class Demo {
    public static void main(String args[]) {
        new Demo().test();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.accumulate("aa","bb");
//        System.out.println(jsonObject.toString());
    }

    public void test() {
        Query query = new Query();
        Entity[] result;
        Entity entity = new Entity("1","中国人民银行","海淀市","100010");
        if (query.add(entity)) {
            System.out.println("add success");
        }
        result = query.queryMatchedByPost(new Entity("人民银行"),10);
        if (result != null) {
            for (int index = 0; index < result.length; index++) {
                try {
                    System.out.print("result is " + new String(result[index].getName().getBytes("utf8"),"gbk")+",score :"+result[index].getScore());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //删除索引
//        query.deleteIndexById("1");
//        result = query.queryMatchedByPost(new Entity("人民银行"),10);
//        if (result != null) {
//            for (int index = 0; index < result.length; index++) {
//                System.out.print("result is " +  result[index].getName());
//            }
//        }
    }
}
