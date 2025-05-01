package vn.khanhduc.fileservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileInformation {
    private String name;
    private String contentType;
    private long size;
    private String md5Checksum;
    private String path;
    private String url;
}
