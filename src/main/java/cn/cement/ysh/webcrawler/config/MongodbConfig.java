package cn.cement.ysh.webcrawler.config;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class MongodbConfig {



    public static final String ZIP =".zip";
    /*用于生成唯一标识后四位*/
    public static  final AtomicLong imageMark = new AtomicLong(0);



    /*mongodb图片存储桶名称*/
    public static   String  bucketName;
    @Value("${mongo.gridfs.bucket.name:fs}")
    public  void setBuketName(String bucketName) {
        MongodbConfig.bucketName = bucketName;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory factory){
        return new MongoTransactionManager(factory);
    }

    @Bean
    public MongoTemplate mongoTemplete(MongoDatabaseFactory factory){
        return new MongoTemplate(factory);
    }

    @Bean("MongoGridFsTemplate")
    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory dbFactory, MongoConverter converter) {
        return new GridFsTemplate(dbFactory, converter, bucketName);
    }

    @Bean("MongoGridFsBucket")
    public GridFSBucket getGridFSBucket(MongoDatabaseFactory factory){
        GridFSBucket bucket = GridFSBuckets.create(factory.getMongoDatabase(),bucketName);
        return bucket;
    }

    /**
     * 生成唯一标识，格式：yyyyMMddHHmmss-顺序号(只取后四位)
     * @return
     */
    public static String  generateUniqueId(){
        String dateid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String format = String.format("%04d", imageMark.incrementAndGet());
        String squeid = format.substring(format.length() - 4);
        String newId = dateid + "-" + squeid;

        return newId;
    }

}
