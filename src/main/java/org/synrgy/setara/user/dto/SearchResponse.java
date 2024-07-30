package org.synrgy.setara.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {
    private String no;
    private String name;
    private String serviceName;
}
