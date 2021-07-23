# FileDownloaderServiceApi

All URIs are relative to *http://localhost:5050/p2pfs*

Method | HTTP request | Description
------------- | ------------- | -------------
[**filesFileByNameGet**](FileDownloaderServiceApi.md#filesFileByNameGet) | **GET** /files/file-by-name | Download a file by name

<a name="filesFileByNameGet"></a>
# **filesFileByNameGet**
> File filesFileByNameGet(name)

Download a file by name

Download a file by name

### Example
```java
// Import classes:
//import client.handler.ApiException;
//import client.api.FileDownloaderServiceApi;


FileDownloaderServiceApi apiInstance = new FileDownloaderServiceApi();
String name = "name_example"; // String | file name that need to be downloaded from the peer
try {
    File result = apiInstance.filesFileByNameGet(name);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FileDownloaderServiceApi#filesFileByNameGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **String**| file name that need to be downloaded from the peer |

### Return type

[**File**](File.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/octet-stream, multipart/form-data, application/json

