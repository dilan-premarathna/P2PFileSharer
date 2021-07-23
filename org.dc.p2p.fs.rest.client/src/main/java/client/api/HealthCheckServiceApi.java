package client.api;

import client.handler.ApiException;
import client.handler.ApiClient;
import client.handler.Configuration;
import client.handler.Pair;

import javax.ws.rs.core.GenericType;

import client.model.HealthCheckResponseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthCheckServiceApi {
  private ApiClient apiClient;

  public HealthCheckServiceApi() {
    this(Configuration.getDefaultApiClient());
  }

  public HealthCheckServiceApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Check health of neibouring nodes
   * Check health of neibouring nodes and remove them from the network if they are not working
   * @return HealthCheckResponseDTO
   * @throws ApiException if fails to make API call
   */
  public HealthCheckResponseDTO apiHealthcheckGet() throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/api/healthcheck";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<HealthCheckResponseDTO> localVarReturnType = new GenericType<HealthCheckResponseDTO>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
