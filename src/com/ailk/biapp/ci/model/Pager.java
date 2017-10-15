package com.ailk.biapp.ci.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Pager {
    private Logger log = Logger.getLogger(Pager.class);
    private List result = new ArrayList();
    private int totalPage = 0;      //总页数
    private long totalSize = 0L;    //数据总条数
    private int pageSize = 10;      //每页大小
    private int pageSizeSel = -1;
    private int pageNum = 1;    //当前页
    private boolean firstPage = false;
    private boolean lastPage = false;
    private boolean hasPrevPage = false;
    private boolean hasNextPage = false;
    private boolean goPage = false;
    private int pageStart = 0;
    private String[] pageButton;
    private String pageFlag;
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private String orderBy = null;
    private String order = null;

    public Pager() {
    }

    public Pager(long totalSize) {
        this.totalSize = totalSize;
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
        }

        this.totalPage = (int)Math.ceil((double)totalSize / (double)this.pageSize);
        this.log.debug("总记录数：" + this.totalPage);
    }

    public Pager(long totalSize, long pageSize) {
        this.totalSize = totalSize;
        if(pageSize <= 0L) {
            pageSize = (long)this.pageSizeSel;
        }

        this.totalPage = (int)Math.ceil((double)totalSize / (double)pageSize);
        this.log.debug("总记录数：" + this.totalPage);
    }

    public Pager nextPage() {
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        }

        Pager pager = new Pager();
        pager.setTotalPage(this.totalPage);
        pager.setTotalSize(this.totalSize);
        pager.setPageSize(this.pageSize);
        this.log.info("this.pageSize" + this.pageSize);
        pager.setPageSizeSel(this.pageSizeSel);
        pager.setPageNum(++this.pageNum);
        pager.handlePage();
        pager.setOrder(this.order);
        pager.setOrderBy(this.orderBy);
        return pager;
    }

    public Pager prevPage() {
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        }

        Pager pager = new Pager();
        pager.setTotalPage(this.totalPage);
        pager.setTotalSize(this.totalSize);
        pager.setPageSize(this.pageSize);
        this.log.info("this.pageSize" + this.pageSize);
        pager.setPageSizeSel(this.pageSizeSel);
        pager.setPageNum(--this.pageNum);
        pager.handlePage();
        pager.setOrder(this.order);
        pager.setOrderBy(this.orderBy);
        return pager;
    }

    public Pager firstPage() {
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        } else {
            this.pageSizeSel = this.pageSize;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        }

        Pager pager = new Pager();
        pager.setTotalPage(this.totalPage);
        pager.setTotalSize(this.totalSize);
        pager.setPageSize(this.pageSize);
        this.log.info("this.pageSize" + this.pageSize);
        pager.setPageSizeSel(this.pageSizeSel);
        pager.setPageNum(1);
        pager.handlePage();
        pager.setOrder(this.order);
        pager.setOrderBy(this.orderBy);
        return pager;
    }

    public Pager lastPage() {
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        }

        Pager pager = new Pager();
        pager.setTotalPage(this.totalPage);
        pager.setTotalSize(this.totalSize);
        pager.setPageSize(this.pageSize);
        this.log.info("this.pageSize" + this.pageSize);
        pager.setPageSizeSel(this.pageSizeSel);
        pager.setPageNum(this.totalPage);
        pager.handlePage();
        pager.setOrder(this.order);
        pager.setOrderBy(this.orderBy);
        return pager;
    }

    public Pager goPage() {
        if(this.pageSizeSel != -1) {
            this.pageSize = this.pageSizeSel;
            this.totalPage = (int)Math.ceil((double)this.totalSize / (double)this.pageSize);
        }

        Pager pager = new Pager();
        pager.setTotalPage(this.totalPage);
        pager.setTotalSize(this.totalSize);
        pager.setPageSize(this.pageSize);
        this.log.info("this.pageSize" + this.pageSize);

        pager.setPageSizeSel(this.pageSizeSel);
        pager.setPageNum(this.pageNum);
        pager.handlePage();
        pager.setOrder(this.order);
        pager.setOrderBy(this.orderBy);
        return pager;
    }

    public Pager pagerFlip() {
        Pager pager = null;
        if(StringUtils.isNotBlank(this.pageFlag)) {
            if(this.pageFlag.equals("F")) {
                pager = this.firstPage();
            } else if(this.pageFlag.equals("L")) {
                pager = this.lastPage();
            } else if(this.pageFlag.equals("P")) {
                pager = this.prevPage();
            } else if(this.pageFlag.equals("N")) {
                pager = this.nextPage();
            } else if(this.pageFlag.equals("G")) {
                pager = this.goPage();
            }
        } else {
            pager = this.firstPage();
        }

        if(pager == null) {
            return new Pager();
        } else {
            if(pager.getPageNum() > pager.totalPage) {
                pager.setPageNum(this.totalPage);
            } else if(pager.getPageNum() <= 0 && pager.getTotalPage() > 0) {
                pager.setPageNum(1);
            }

            if(pager.totalPage < 6 && pager.getPageButton() == null) {
                pager.setPageButton(new String[pager.totalPage]);
            }

            this.log.debug("下一个分页信息：totalSize[" + pager.getTotalSize() + "] totalPage[" + pager.getTotalPage() + "] pageSize[" + pager.getPageSize() + "] pageNum[" + pager.getPageNum() + "]");
            return pager;
        }
    }

    private void handlePage() {
        if(this.pageNum > 1) {
            this.hasPrevPage = true;
        }

        if(this.pageNum < this.totalPage) {
            this.hasNextPage = true;
        }

        if(this.pageNum == 1) {
            this.firstPage = true;
        }

        if(this.pageNum == this.totalPage) {
            this.lastPage = true;
        }

        if(this.totalPage > 1) {
            this.goPage = true;
        }

    }

    public int getPageStart() {
        return this.pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public List getResult() {
        return this.result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return this.lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public boolean isHasPrevPage() {
        return this.hasPrevPage;
    }

    public boolean isHasNextPage() {
        return this.hasNextPage;
    }

    public boolean isGoPage() {
        return this.goPage;
    }

    public void setGoPage(boolean goPage) {
        this.goPage = goPage;
    }

    public int getPageSizeSel() {
        return this.pageSizeSel;
    }

    public void setPageSizeSel(int pageSizeSel) {
        this.pageSizeSel = pageSizeSel;
    }

    public String getPageFlag() {
        return this.pageFlag;
    }

    public void setPageFlag(String pageFlag) {
        this.pageFlag = pageFlag;
    }

    public String[] getPageButton() {
        return this.pageButton;
    }

    public void setPageButton(String[] pageButton) {
        this.pageButton = pageButton;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Pager orderBy(String theOrderBy) {
        this.setOrderBy(theOrderBy);
        return this;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        if(StringUtils.isBlank(order)) {
            this.order = null;
        } else {
            String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
            String[] arr$ = orders;
            int len$ = orders.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String orderStr = arr$[i$];
                if(!StringUtils.equals("desc", orderStr) && !StringUtils.equals("asc", orderStr)) {
                    throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
                }
            }

            this.order = StringUtils.lowerCase(order);
        }
    }

    public boolean isOrderBySetted() {
        return StringUtils.isNotBlank(this.orderBy) && StringUtils.isNotBlank(this.order);
    }
}
