package cn.cement.ysh.webcrawler.service;

import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.entry.crawler.DicCrawWord;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//爬虫
@Service
public class CrawlerOrderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Map selectCrawlerorderById(String objectId,String collectionName){
        Query query = Query.query(Criteria.where("_id").is(objectId));
        Map one = mongoTemplate.findOne(query, Map.class, collectionName);
        return one;
    }

    public CrawOrder selectCrawlerorderById(String objectId){
        Query query = Query.query(Criteria.where("_id").is(objectId));
        CrawOrder one = mongoTemplate.findOne(query, CrawOrder.class);
        return one;
    }

    public List<DicCrawWord>  selecKeywords(){
        List<DicCrawWord> all = mongoTemplate.findAll(DicCrawWord.class);
        return all;
    }


}
