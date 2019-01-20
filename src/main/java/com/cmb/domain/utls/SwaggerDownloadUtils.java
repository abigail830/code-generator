package com.cmb.domain.utls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SwaggerDownloadUtils {

    @Value("${file.server.url}")
    private String downLoadServerUrl;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 通过附件uri地址下载
     * @param fileUri
     * @return
     */
    public byte[] downLoadSwagger(String fileUri) {
        String url = downLoadServerUrl + "?fileUri=" + fileUri;
        return download(url);
    }

    /**
     * 通过附件id下载
     * @param swaggerFileId
     * @return
     */
    public byte[] downLoadSwagger(Long swaggerFileId) {
        String url = downLoadServerUrl + "/" + swaggerFileId;
        return download(url);
    }

    private byte[] download(String url) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET,
                httpEntity, byte[].class);

        byte[] value = response.getBody();
        return value;
    }
}
