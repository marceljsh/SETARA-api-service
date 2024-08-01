package org.synrgy.setara.vendor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EwalletResponse {
    private String id;
    private String name;
    private String imagePath;
}
