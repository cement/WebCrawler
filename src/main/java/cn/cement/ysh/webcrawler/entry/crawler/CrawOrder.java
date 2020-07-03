package cn.cement.ysh.webcrawler.entry.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

// 爬虫爬取命令类
// email:\\w+?@\\w+?.com
//        tel:(?<!\\d)(?:(?:1[358]\\d{9})|(?:861[358]\\d{9}))(?!\\d)
//        idcard:\\d{17}[\\d|x]|\\d{15}
@Document(collection = "craw_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawOrder {
     @Id
     private String orderId; // 一次爬网页命令id

     private String memo; // 本次爬寻网页备注
     private List<String> webUrls ; // 本次要爬的网址集合
     private String keyWord;// 爬询关键字集合，逗号分隔
     private List<DicCrawWord> crawWords; // 爬询类别字集合（电话，邮箱，身份证）
     private String themeId; // 主题组id
     private String themeName; // 主题组名称
     private int deep; // 爬虫最大深度
     private int outDeep; // 爬虫最大外部链接深度
     private boolean iused; // 是否启用标志

     private String policeNo; // 本次爬寻制作民警警号
     private String policeName; // 民警名称
     private String crawDate; // 爬寻日期
}
