package cn.cement.ysh.webcrawler.controller;

import cn.cement.ysh.webcrawler.crawler.WordsCrawler;
import cn.cement.ysh.webcrawler.crawler.WorkerCrawler;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.service.CrawlerOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(value = "网络爬虫",tags = "网络爬虫API接口",description = "网络爬虫")
@Slf4j
@RestController
@RequestMapping("/api/crawler")
public class CrawlerCtrl {

    @Autowired
    private CrawlerOrderService orderService;

    /**
     * desc 关键字和正则是或的关系。可以为空。
     * @param seed
     * @param depth
     * @param keywords
     * @param regexs
     */
    @ApiOperation(value = "网络爬虫测试", notes = "网络爬虫测试api接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seed", value = "初始url", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "depth", value = "爬取深度", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "linkRegex", value = "连接正则表达式", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "regexs", value = "正则表达式", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/startCrawler", method = {RequestMethod.GET})
    public ResponseEntity startCrawler(String seed, int depth, String linkRegex, String regexs){
        Assert.state(!StringUtils.isEmpty(regexs),"爬取关键字或正则表达式不能为空");
        if (WorkerCrawler.executor.getActiveCount()>0){
            return ResponseEntity.ok("任务正在进行中......");
        }

        Map<String, String> regexMap = Arrays.asList(regexs.split(",")).stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        WordsCrawler defaultCrawler = new WordsCrawler("orderId-0001",seed, depth,linkRegex, regexMap);
        defaultCrawler.start();
        return ResponseEntity.ok("爬取中。。。");
    }



    @ApiOperation(value = "查询爬虫订单", notes = "查询爬虫订单api接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectId", value = "订单id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/selectOrder", method = {RequestMethod.GET})
    public ResponseEntity selectOrder(String objectId){
        CrawOrder crawOrder = orderService.selectCrawlerorderById(objectId);
        return ResponseEntity.ok(crawOrder);
    }
    @ApiOperation(value = "根据id查询", notes = "查询爬虫订单api接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectId", value = "记录id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectionName", value = "集合名称", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/selectRecord", method = {RequestMethod.GET})
    public ResponseEntity selectRecord(String objectId,String collectionName){
        Map map = orderService.selectCrawlerorderById(objectId, collectionName);
        Map resultMap = Optional.ofNullable(map).orElse(Collections.emptyMap());
        return ResponseEntity.ok(resultMap);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.status(500).body(e.getMessage());
    }


}
