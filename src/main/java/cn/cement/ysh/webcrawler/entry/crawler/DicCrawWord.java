package cn.cement.ysh.webcrawler.entry.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// 单个爬虫关键字类
@Document(collection = "dic_craw_word")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DicCrawWord {
       @Id
       private String wordId; // 编号
       private String word ; // 关键字
       private String regx; //正则表达式
}
