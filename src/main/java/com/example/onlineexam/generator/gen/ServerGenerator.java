package com.example.onlineexam.generator.gen;

import com.example.onlineexam.generator.util.DbUtil;
import com.example.onlineexam.generator.util.Field;
import com.example.onlineexam.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static boolean readOnly = true;
    static String serverPath = "src/main/java/com/example/onlineexam/";
    static String pomPath = "pom.xml";
    static String module = "";
    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> param = new HashMap<>();
        // 读取table节点
        String generatorPath = getGeneratorPath();
        Document document = new SAXReader().read(generatorPath);
        // 为DbUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();
        //System.out.println("url: " + connectionURL.getText());
        //System.out.println("user: " + userId.getText());
        //System.out.println("password: " + password.getText());
        Node table = document.selectSingleNode("//table");
        List<Node> nodes = document.selectNodes("//table/@domainObjectName");
        List<Node> tableNameList = document.selectNodes("//table/@tableName");
        // domainObjectNames 值
        List<String> domainObjectNames = new ArrayList<>();
        // tableNames 值
        List<String> tableNames = new ArrayList<>();
        for (Node node : nodes) {
            domainObjectNames.add(node.getText());
        }
        for (Node tableName : tableNameList) {
            tableNames.add(tableName.getText());
        }
        for (int i = 0; i < domainObjectNames.size() ; i++) {
            System.out.println(domainObjectNames.get(i));
            String tableNameCn =DbUtil.getTableComment(domainObjectNames.get(i)) ;
            List<Field> fieldList = DbUtil.getColumnByTableName(tableNames.get(i));
            Set<String> typeSet = getJavaTypes(fieldList);
            param.put("tableNameCn", tableNameCn);
            param.put("fieldList", fieldList);
            param.put("typeSet", typeSet);
//            gen(domainObjectNames.get(i), param, "req", "req");
//            gen(domainObjectNames.get(i), param, "resp", "resp");
            gen(domainObjectNames.get(i),param, "controller", "controller");
            gen(domainObjectNames.get(i),param, "service", "service");
        }


        //gen("Comment",param, "service", "service");
        //gen("Apps", param, "req", "req");
        //gen("Apps", param, "resp", "resp");
//
//        gen("Category",param, "controller", "controller");
//        gen("Category",param, "service", "service");
//        gen("Chat",param, "controller", "controller");
//        gen("Chat",param, "service", "service");
//        gen("ChatDetailed",param, "controller", "controller");
//        gen("ChatDetailed",param, "service", "service");
//        gen("Comment",param, "controller", "controller");
//        gen("Comment",param, "service", "service");
//        gen("Danmu",param, "controller", "controller");
//        gen("Danmu",param, "service", "service");
//        gen("Favorite",param, "controller", "controller");
//        gen("Favorite",param, "service", "service");
//        gen("FavoriteVideo",param, "controller", "controller");
//        gen("FavoriteVideo",param, "service", "service");
//        gen("MsgUnread",param, "controller", "controller");
//        gen("MsgUnread",param, "service", "service");
//        gen("User",param, "controller", "controller");
//        gen("User",param, "service", "service");
//        gen("UserVideo",param, "controller", "controller");
//        gen("UserVideo",param, "service", "service");
//        gen("Video",param, "controller", "controller");
//        gen("Video",param, "service", "service");
//        gen("VideoStats",param, "controller", "controller");
//        gen("VideoStats",param, "service", "service");

//        gen("Category", param, "req", "req");
//        gen("Category", param, "resp", "resp");
//        gen("Chat", param, "req", "req");
//        gen("Chat", param, "resp", "resp");
//        gen("ChatDetailed", param, "req", "req");
//        gen("ChatDetailed", param, "resp", "resp");
//        gen("Comment", param, "req", "req");
//        gen("Comment", param, "resp", "resp");
//        gen("Danmu", param, "req", "req");
//        gen("Danmu", param, "resp", "resp");
//        gen("Favorite", param, "req", "req");
//        gen("Favorite", param, "resp", "resp");
//        gen("FavoriteVideo", param, "req", "req");
//        gen("FavoriteVideo", param, "resp", "resp");
//        gen("MsgUnread", param, "req", "req");
//        gen("MsgUnread", param, "resp", "resp");
//        gen("User", param, "req", "req");
//        gen("User", param, "resp", "resp");
//        gen("UserVideo", param, "req", "req");
//        gen("UserVideo", param, "resp", "resp");
//        gen("Video", param, "req", "req");
//        gen("Video", param, "resp", "resp");
//        gen("VideoStats", param, "req", "req");
//        gen("VideoStats", param, "resp", "resp");
    }

    private static void gen(String Domain,Map<String, Object> param,String packageName, String target) throws IOException, TemplateException {
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        param.put("Domain",Domain);
        param.put("domain",domain);
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + packageName + "/";
        new File(toPath).mkdirs();
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }
    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
