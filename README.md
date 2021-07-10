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

### How to Run 