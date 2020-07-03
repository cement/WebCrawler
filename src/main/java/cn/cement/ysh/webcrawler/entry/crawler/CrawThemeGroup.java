package cn.cement.ysh.webcrawler.entry.crawler;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

//爬虫主题组
@Document(collection = "craw_theme_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawThemeGroup {
    @Id
    private String id;
    private String themeName;// 主题组名称
    private List<CrawWebUrls> crawWebUrlsList; // 主题组包含网站
    private String policeNo;  // 插入民警名称
    private String policeName; // 插入民警编号
    private String insertTime= DateUtil.formatDateTime(new Date()); //插入日期
}
