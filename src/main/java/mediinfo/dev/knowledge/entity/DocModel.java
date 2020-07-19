package mediinfo.dev.knowledge.entity;

import lombok.Data;
import mediinfo.dev.knowledge.utils.Utils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author ：vivaciousis
 * @date ：Created in 2020/7/18 13:10
 * @description：文档实体类
 */
@Data
@Document(indexName = "knowledge",type = "_doc",useServerConfiguration = true,createIndex = false)
public class DocModel {
    @Id
    private String Id;

    @Field(type = FieldType.Text)
    private String docId;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String author;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Text)
    private String createTime;
    @Field(type = FieldType.Text)
    private String updateTime;

    public DocModel(String title, String author, String content, String createTime, String updateTime) {
        this.docId = Utils.getShortUuid();
        this.title = title;
        this.author = author;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
