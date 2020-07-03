package cn.cement.ysh.webcrawler.entry.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// 爬虫爬寻结果类
@Document(collection = "craw_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawResult {
    @Id
    private ObjectId _id;
    private String orderId; //爬寻命令id
    private String webUrl; // 网址
    private String crawWord; // 爬寻的关键词
    private String result; // 爬寻结果 多个结果以分号分隔
    private String sourceText; // 原文
    private String crawTime; //爬寻时间

}
