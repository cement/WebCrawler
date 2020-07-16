package cn.cement.ysh.webcrawler.config;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongodbConfig {



    /*mongodb图片存储桶名称,配置文件指定*/
    public static   String  bucketName;
    @Value("${mongo.gridfs.bucket.name:fs}")
    public  void setBuketName(String bucketName) {
        MongodbConfig.bucketName = bucketName;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory factory){
        return new MongoTransactionManager(factory);
    }

    @Bean
    public MongoTemplate mongoTemplete(MongoDbFactory factory){
        return new MongoTemplate(factory);
    }

    @Bean("MongoGridFsTemplate")
    public GridFsTemplate gridFsTemplate(MongoDbFactory dbFactory, MongoConverter converter) {
        return new GridFsTemplate(dbFactory, converter, bucketName);
    }

    @Bean("MongoGridFsBucket")
    public GridFSBucket getGridFSBucket(MongoDbFactory factory){
        GridFSBucket bucket = GridFSBuckets.create(factory.getDb(),bucketName);
        return bucket;
    }


}
