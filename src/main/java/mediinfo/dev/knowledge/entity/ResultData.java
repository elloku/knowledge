package mediinfo.dev.knowledge.entity;

import java.util.List;

/**
 * @author ：vivaciousis
 * @date ：Created in 2020/7/19 19:15
 * @description：
 */
public class ResultData {

    public ResultData() {
    }

    public ResultData(long total, int size, int page, int pageTotal, List<DocModel> data) {
        this.total = total;
        this.size = size;
        this.page = page;
        this.pageTotal = pageTotal;
        this.data = data;
    }

    private long total;

    private int size;

    private int page;

    private int pageTotal;

    private List<DocModel> data;

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<DocModel> getData() {
        return data;
    }

    public void setData(List<DocModel> data) {
        this.data = data;
    }


}
