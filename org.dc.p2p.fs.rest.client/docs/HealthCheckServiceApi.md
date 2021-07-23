# HealthCheckServiceApi

All URIs are relative to *http://localhost:5050/p2pfs*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiHealthcheckGet**](HealthCheckServiceApi.md#apiHealthcheckGet) | **GET** /api/healthcheck | Check health of neibouring nodes

<a name="apiHealthcheckGet"></a>
# **apiHealthcheckGet**
> HealthCheckResponseDTO apiHealthcheckGet()

Check health of neibouring nodes

Check health of neibouring nodes and remove them from the network if they are not working

### Example
```java
// Import classes:
//import client.handler.ApiException;
//import client.api.HealthCheckServiceApi;


HealthCheckServiceApi apiInstance = new HealthCheckServiceApi();
try {
    HealthCheckResponseDTO result = apiInstance.apiHealthcheckGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HealthCheckServiceApi#apiHealthcheckGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**HealthCheckResponseDTO**](HealthCheckResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

