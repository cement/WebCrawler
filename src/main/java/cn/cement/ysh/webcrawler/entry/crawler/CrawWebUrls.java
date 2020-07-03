package cn.cement.ysh.webcrawler.entry.crawler;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

// 爬虫标准资源地址
@Document(collection = "craw_web_urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawWebUrls {
      @Id
      private String id;
      private String url;  // url
      private String webName; //网站名称
      private String policeNo;  // 插入民警名称
      private String policeName; // 插入民警编号
      private String insertTime= DateUtil.formatDateTime(new Date()); //插入日期
}
