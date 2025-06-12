#### What is gRPC?

gRPC (Google Remote Procedure Call) is a high-performance, open-source RPC (Remote Procedure Call) framework developed by Google.  

It allows client and server applications to communicate directly by calling methods on remote objects as if they were local.  
It is built on:  

    HTTP/2 for transport
    Protocol Buffers (Protobuf) for serialization
    Auto-generated client/server code for multiple languages
#### Key Components of gRPC
| Component                       | Description                                                                        |  
| ------------------------------- | ---------------------------------------------------------------------------------- |  
| **Client**                      | Makes remote procedure calls (RPC) to the server.                                  |  
| **Server**                      | Implements the service, listens to RPC requests.                                   |  
| **Service Definition (.proto)** | Declares the RPC services and message types using Protocol Buffers.                |  
| **Protocol Buffers**            | A compact binary format used for defining and serializing structured data.         |  
| **gRPC Stub**                   | Auto-generated client/server code based on `.proto` file, handles network details. |  
