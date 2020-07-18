package mediinfo.dev.knowledge.controller;

import mediinfo.dev.knowledge.entity.DocModel;
import mediinfo.dev.knowledge.repository.IRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：vivaciousis
 * @date ：Created in 2020/7/17 23:52
 * @description：
 */

@RestController
public class QueryFileController {

    @Autowired
    IRepository iRepository;


    @PostMapping("/search")
    public Object search(@RequestBody String keyword){

        keyword = "liuhuanping";

        HashMap<String, Object> map = new HashMap<>();

        StopWatch watch = new StopWatch();
        watch.start();

        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        builder.should(QueryBuilders.matchPhraseQuery("title",keyword));
        builder.should(QueryBuilders.matchPhraseQuery("content",keyword));

        String queryString = builder.toString();
        System.out.println(queryString);


        List<DocModel> content = new ArrayList<>();
        try {
            Page<DocModel> search = (Page<DocModel>) iRepository.search(builder);
            content = search.getContent();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }

        map.put("list",content);

        watch.stop();
        long mi = watch.getTotalTimeMillis();
        map.put("duration",mi);
        return map;
    }
}
