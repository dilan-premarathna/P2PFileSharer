package client.api;

import client.handler.ApiException;
import client.handler.ApiClient;
import client.handler.Configuration;
import client.handler.Pair;

import javax.ws.rs.core.GenericType;

import client.model.ApiResponseDTO;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDownloaderServiceApi {
  private ApiClient apiClient;

  public FileDownloaderServiceApi() {
    this(Configuration.getDefaultApiClient());
  }

  public FileDownloaderServiceApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Download a file by name
   * Download a file by name
   * @param name file name that need to be downloaded from the peer (required)
   * @return File
   * @throws ApiException if fails to make API call
   */
  public File filesFileByNameGet(String name) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling filesFileByNameGet");
    }
    // create path and map variables
    String localVarPath = "/files/file-by-name";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "name", name));


    final String[] localVarAccepts = {
            "multipart/form-data"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<File> localVarReturnType = new GenericType<File>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
