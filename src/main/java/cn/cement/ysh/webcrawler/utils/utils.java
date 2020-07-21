package cn.cement.ysh.webcrawler.utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeFilter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class utils {

    public static List<String> findAllLinks(Element element) {
        List<String> linkList = element.select("a[href]").eachAttr("abs:href");
        return linkList;
    }

    public static List<String> findAllLinks(Element element, NodeFilter nodeFilter) {
        List<String> linkList = element.select("a[href]").filter(nodeFilter).eachAttr("abs:href");
        return linkList;
    }

    public static String findPartByRegex(String document, String regex) {
        String Regex = "[^\\.。,，;； ]*"+regex+"[^\\.。,，;； ]*";
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(document);
        LinkedList<String> contentList = new LinkedList<>();
        while (matcher.find()) {
            String group = matcher.group();
            contentList.add(group);
        }
        return contentList.stream().collect(Collectors.joining(","));
    }

    public static List<String> findContentByRegex(String document, String regex) {
        String Regex = "[^\\.。]*"+regex+"[^\\.。]*";
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(document);
        LinkedList<String> contentList = new LinkedList<>();
        while (matcher.find()) {
            String group = matcher.group();
            contentList.add(group);
        }
        return contentList;
    }

//    public static List<String> findContentByRegex(String document, String regex) {
//        String Regex = "[^\\n\\r]*"+regex+"[^\\n\\r]*";
//        Pattern pattern = Pattern.compile(Regex);
//        Matcher matcher = pattern.matcher(document);
//        LinkedList<String> list = new LinkedList<>();
//        while (matcher.find()) {
//            String group = matcher.group();
//            list.add(group);
//        }
//        return list;
//    }


    public List<String> findByRegex(Element element, String regex, String groupName) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(element.text());
        LinkedList<String> list = new LinkedList<>();
        while (matcher.find()) {
            String group = matcher.group(groupName);
            list.add(group);
        }
        return list;
    }

    public static Map<Integer, List<String>> findGroupsByRegex(Element element, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(element.text());
        Map<Integer, List<String>> linkedMap = new LinkedHashMap();
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if (linkedMap.get(i) == null){
                    linkedMap.put(i,new LinkedList<String>());
                }
                linkedMap.get(i).add(group);
            }
        }
        return linkedMap;
    }




}
