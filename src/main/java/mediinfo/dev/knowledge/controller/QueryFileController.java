package mediinfo.dev.knowledge.controller;

import mediinfo.dev.knowledge.entity.DocModel;
import mediinfo.dev.knowledge.entity.ResultData;
import mediinfo.dev.knowledge.repository.IRepository;
import mediinfo.dev.knowledge.utils.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    private BoolQueryBuilder builder = QueryBuilders.boolQuery();

    private List<DocModel> content = new ArrayList<>();

    /**
     * 查询es中的所有数据
     *
     * @return 所有数据集合
     */
    @GetMapping("/searchAll")
    public ResultData searchAll() {
        // 查询数据
        return this.search(SearchType.should, builder, 1, 10);
    }

    /**
     * 根据页码和页大小查询es中的数据
     * searchByPage?pageIndex=1&pageSize=1
     * @return 数据集合
     */
    @GetMapping("/searchByPage")
    public ResultData searchByPage(int pageIndex,int pageSize) {
        // 查询数据
        return this.search(SearchType.should, builder, pageIndex, pageSize);
    }


    /**
     * 根据标题关键字查询
     *
     * @param titleKey 标题关键字
     * @return 查询到的数据集合
     */
    @PostMapping("/searchByTitleKey")
    public List<DocModel> searchByTitleKey(@RequestBody String titleKey) {

        // 构建查询条件
        builder.should(QueryBuilders.matchPhraseQuery("title", titleKey));

        try {
            // 查询数据
            Page<DocModel> search = (Page<DocModel>) iRepository.search(builder);
            content = search.getContent();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return content;
    }

    /**
     * 根据作者查询
     *
     * @param authorName 作者
     * @return 查询到的数据集合
     */
    @PostMapping("/searchByAuthor")
    public List<DocModel> searchByAuthor(@RequestBody String authorName) {

        // 构建查询条件
        builder.should(QueryBuilders.matchPhraseQuery("author", authorName));

        try {
            // 查询数据
            Page<DocModel> search = (Page<DocModel>) iRepository.search(builder);
            content = search.getContent();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return content;
    }

    /**
     * 根据内容关键字查询
     *
     * @param contentKey 内容关键字
     * @return 查询到的数据集合
     */
    @PostMapping("/searchByContentKey")
    public List<DocModel> searchByContentKey(@RequestBody String contentKey) {

        // 构建查询条件
        builder.should(QueryBuilders.matchPhraseQuery("content", contentKey));

        try {
            // 查询数据
            Page<DocModel> search = (Page<DocModel>) iRepository.search(builder);
            content = search.getContent();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return content;
    }

    /**
     * 根据关键字查询
     *
     * @param key 关键字
     * @return 查询到的数据集合
     */
    @PostMapping("/searchByKey")
    public List<DocModel> search(@RequestBody String key) {

        // test data keyword = "liuhuanping";

        // 构建查询条件
        builder.should(QueryBuilders.matchPhraseQuery("title", key));
        builder.should(QueryBuilders.matchPhraseQuery("author", key));
        builder.should(QueryBuilders.matchPhraseQuery("content", key));

        try {
            // 查询数据
            Page<DocModel> search = (Page<DocModel>) iRepository.search(builder);
            content = search.getContent();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return content;
    }

    private String sortField = "createTime";
    private SortOrder sortOrder = SortOrder.DESC;

    public ResultData search(SearchType searchType, QueryBuilder queryBuilder, int pageIndex, int pageSize) {

        //1 创建查询构建器s
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //2. 多条件查询  使用boolQuery    可以new  但是可以使用静态 建议使用静态
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (searchType == SearchType.should) {
            boolQuery.should(queryBuilder);
        }

        // 根据某个字段进行排序 排序查询会报异常 UncategorizedElasticsearchException(未分类)
        //nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortOrder));

        // 分页  elasticsearch 页数从0开始
        nativeSearchQueryBuilder.withPageable(PageRequest.of((pageIndex - 1), pageSize));

        nativeSearchQueryBuilder.withQuery(boolQuery);

        ResultData resultData = new ResultData();

        try {
            Page<DocModel> pageList = this.iRepository.search(nativeSearchQueryBuilder.build());
            List<DocModel> list = new ArrayList<>();
            for (DocModel model : pageList) {
                list.add(new DocModel(model.getTitle(), model.getAuthor(), model.getContent(), model.getCreateTime(), model.getUpdateTime()));
            }

            resultData = new ResultData(pageList.getTotalElements(), pageSize, pageIndex, pageList.getTotalPages(), list);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return resultData;
    }
}
