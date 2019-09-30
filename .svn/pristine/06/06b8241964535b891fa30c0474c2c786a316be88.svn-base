package cn.appoa.aframework.okgo;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * OkGo封装网络请求
 */
public final class ZmOkGo {

    /**
     * get请求
     *
     * @param url 接口地址
     */
    public static GetRequest<String> request(String url) {
        GetRequest<String> request = OkGo.<String>get(url);
        return request;
    }

    /**
     * post请求
     *
     * @param url    接口地址
     * @param params 请求参数
     */
    public static PostRequest<String> request(String url, Map<String, String> params) {
        PostRequest<String> request = OkGo.<String>post(url);
        for (String key : params.keySet()) {
            request.params(key, params.get(key));
        }
        return request;
    }

    /**
     * post请求(带请求头)
     *
     * @param url     接口地址
     * @param params  请求参数
     * @param headers 请求头
     */
    public static PostRequest<String> request(String url, Map<String, String> params,
                                              Map<String, String> headers) {
        PostRequest<String> request = request(url, params);
        for (String key : headers.keySet()) {
            request.headers(key, headers.get(key));
        }
        return request;
    }

    /**
     * 上传文件(一个key一个文件)
     *
     * @param url     接口地址
     * @param params  请求参数
     * @param fileKey 文件的键
     * @param file    文件
     */
    public static PostRequest<String> upload(String url, Map<String, String> params,
                                             String fileKey, File file) {
        PostRequest<String> request = request(url, params);
        request.params(fileKey, file);
        return request;
    }

    /**
     * 上传文件(一个key多个文件)
     *
     * @param url      接口地址
     * @param params   请求参数
     * @param fileKey  文件的键
     * @param fileList 文件集合
     */
    public static PostRequest<String> upload(String url, Map<String, String> params,
                                             String fileKey, List<File> fileList) {
        PostRequest<String> request = request(url, params);
        request.addFileParams(fileKey, fileList);
        return request;
    }

    /**
     * 上传文件(多个key多个文件)
     *
     * @param url     接口地址
     * @param params  请求参数
     * @param fileMap 文件键值对
     */
    public static PostRequest<String> upload(String url, Map<String, String> params, Map<String, File> fileMap) {
        PostRequest<String> request = request(url, params);
        for (String key : fileMap.keySet()) {
            request.params(key, fileMap.get(key));
        }
        return request;
    }

    /**
     * 上传文件(一个key多个文件+多个key多个文件)
     *
     * @param url      接口地址
     * @param params   请求参数
     * @param fileKey  文件的键
     * @param fileList 文件集合
     * @param fileMap  文件键值对
     */
    public static PostRequest<String> upload(String url, Map<String, String> params,
                                             String fileKey, List<File> fileList, Map<String, File> fileMap) {
        PostRequest<String> request = request(url, params);
        request.addFileParams(fileKey, fileList);
        for (String key : fileMap.keySet()) {
            request.params(key, fileMap.get(key));
        }
        return request;
    }

    /**
     * 上传文件(一个key一个文件)(带请求头)
     *
     * @param url     接口地址
     * @param params  请求参数
     * @param headers 请求头
     * @param fileKey 文件的键
     * @param file    文件
     */
    public static PostRequest<String> upload(String url, Map<String, String> params, Map<String, String> headers,
                                             String fileKey, File file) {
        PostRequest<String> request = request(url, params, headers);
        request.params(fileKey, file);
        return request;
    }

    /**
     * 上传文件(一个key多个文件)(带请求头)
     *
     * @param url      接口地址
     * @param params   请求参数
     * @param headers  请求头
     * @param fileKey  文件的键
     * @param fileList 文件集合
     */
    public static PostRequest<String> upload(String url, Map<String, String> params, Map<String, String> headers,
                                             String fileKey, List<File> fileList) {
        PostRequest<String> request = request(url, params, headers);
        request.addFileParams(fileKey, fileList);
        return request;
    }

    /**
     * 上传文件(多个key多个文件)(带请求头)
     *
     * @param url     接口地址
     * @param params  请求参数
     * @param headers 请求头
     * @param fileMap 文件键值对
     */
    public static PostRequest<String> upload(String url, Map<String, String> params, Map<String, String> headers,
                                             Map<String, File> fileMap) {
        PostRequest<String> request = request(url, params, headers);
        for (String key : fileMap.keySet()) {
            request.params(key, fileMap.get(key));
        }
        return request;
    }

    /**
     * 上传文件(一个key多个文件+多个key多个文件)(带请求头)
     *
     * @param url      接口地址
     * @param params   请求参数
     * @param headers  请求头
     * @param fileKey  文件的键
     * @param fileList 文件集合
     * @param fileMap  文件键值对
     */
    public static PostRequest<String> upload(String url, Map<String, String> params, Map<String, String> headers,
                                             String fileKey, List<File> fileList, Map<String, File> fileMap) {
        PostRequest<String> request = request(url, params, headers);
        request.addFileParams(fileKey, fileList);
        for (String key : fileMap.keySet()) {
            request.params(key, fileMap.get(key));
        }
        return request;
    }

}
