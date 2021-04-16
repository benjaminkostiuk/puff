package com.unityTest.testrunner.models.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
abstract class BasePage<T> {

    @Data
    @ApiModel(value = "PageInfo", description = "Provides details about the page returned")
    private class PageInfo {
        @ApiModelProperty(value = "True if page request is sorted", example = "false")
        private boolean isSorted;

        @ApiModelProperty(value = "Page number", example = "0")
        private int pageNumber;

        @ApiModelProperty(value = "Total number of pages", example = "2")
        private int totalPages;

        @ApiModelProperty(value = "Size of page returned", example = "3")
        private int pageSize;

        @ApiModelProperty(value = "Element offset in page")
        private long offset;

        PageInfo(Pageable pageable, int totalPages) {
            this.isSorted = pageable.getSort().isSorted();
            this.pageNumber = pageable.getPageNumber();
            this.totalPages = totalPages;
            this.pageSize = pageable.getPageSize();
            this.offset = pageable.getOffset();
        }
    }

    @ApiModelProperty(value = "Number of elements returned", example = "1")
    private int count;

    @ApiModelProperty(value = "Elements returned")
    private List<T> data;

    @ApiModelProperty(value = "Information about the returned page")
    private PageInfo pageable;

    @ApiModelProperty(value = "Total number of elements", example = "5")
    private long totalElements;

    @ApiModelProperty(value = "True if the request shows the last page", example = "false")
    private boolean isLast;

    @ApiModelProperty(value = "True if the request shows the first page", example = "true")
    private boolean isFirst;

    @ApiModelProperty(value = "True if there exists more pages with elements", example = "true")
    private boolean hasMoreData;

    @ApiModelProperty(value = "True if the page contains no elements", example = "false")
    private boolean isEmpty;

    public BasePage(Page<T> page) {
        this.count = page.getNumberOfElements();
        this.data = page.getContent();
        this.pageable = new PageInfo(page.getPageable(), page.getTotalPages());
        this.totalElements = page.getTotalElements();
        this.isLast = page.isLast();
        this.isFirst = page.isFirst();
        this.hasMoreData = page.getNumber() < page.getTotalPages() - 1;
        this.isEmpty = page.isEmpty();
    }
}