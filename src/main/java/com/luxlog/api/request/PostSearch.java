package com.luxlog.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearch {
    @Builder.Default
    private int page=1;
    @Builder.Default
    private int size=10;
    private final int MAX_SIZE = 100;

    public long getOffSet() {
        return (long) Math.max(0,(page-1))*Math.min(size,MAX_SIZE);
    }
//    @Builder
//    public PostSearch(int page, int size) {
//        this.page = page;
//        this.size = size;
//    }
}
