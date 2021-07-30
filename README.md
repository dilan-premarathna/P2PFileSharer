# Project - Distributed Content Searching
This repo contains complete design of the distributed P2P overlay-based system which contains a set of nodes that can search and share contents among each other. 


## Goals
Develop a simple overlay-based solution that allows a set of nodes to share contents (e.g., music files) among each other. Consider a set of nodes connected via some overlay topology. Each of the nodes has a set of files that it is willing to share with other nodes. Suppose node x is interested in a file f. x issues a  search query to the overlay to locate a at least one node y containing that particular file. Once the node is identified, the file f can be exchanged between X and y.

After completing this project, you will have developed a solution to search and download contents in a distributed system. You will be able to:

* design, develop, and debug overlay-based applications such as a simple search engine to find contents in a distributed system
* use RPCs, web services, or REST APIs to develop distributed systems
* measure an analyze the performance of a distributed system

### How to build
Execute the Below command inside the project home directory.

`mavn clean install`

This will build the project modules individually. The final runnable jar file packaging all the other submodule dependency jars will get created inside "P2PFileSharer/org.dc.p2p.fs.rest.service/target" directory.

### How to Run 

 - copy the following files to a single directory. ex - node1
    1. P2PFileSharer/org.dc.p2p.fs.rest.service/target/org.dc.p2p.fs.rest.service-1.0.0.jar
    2. P2PFileSharer/org.dc.p2p.fs.rest.service/src/main/resources/application.properties
    3. P2PFileSharer/org.dc.p2p.fs.ui/src/main/resources/config.properties
    4. P2PFileSharer/org.dc.p2p.fs.ui/src/main/resources/FileNames.txt
   
 - Give unique values for the following configuration in application.properties file.
      1. "server.port" - This is the REST API listener port. We need to provide different values per node if we are running in the same vm. ex- 5050, 5051

 - Give unique values for the following configuration in config.properties file.
      1. "SERVER_PORT" - This is the UDP listener port of the node. We need to provide different values per node if we are running in the same vm. ex- 5001, 5002
      2. "REST_SERVICE_PORT" - Configure the same value as the "server.port" in application.properties file.
      3. "SERVER_NAME" - Unique name to identify the node. ex - server1, server2
      4. "BS_PORT" - Bootstrap server port. This will be unique to all nodes.
      5. "SO_TIMEOUT" - This is the UDP socket time out. The connection will be closed after waiting this amount of time for server response.
      6. "RETRY_LIMIT" - Retry count to connect with the BS server when re-registering.
      7. "FILE_STORAGE_DIR" - The temporary directory to store generated file in the server side. Need to provide a valid directory path. ex - ~/node1/tmp
      8. "FILE_DOWNLOAD_DIR" - The file download directory of the client. Need to provide a valid directory path. ex - ~/node1/downloads
      9. "FILE_NAME_LIST" - The file containing the list of file names that needs to randomly initialized at the server startup. Needs to provide the valid file path.
   
 - Run the application using the following command.

   ```sh
   java -DpropFileLocation="~/path_to_config.properties_file/config.properties" -jar org.dc.p2p.fs.rest.service-1.0.0.jar
   ```
