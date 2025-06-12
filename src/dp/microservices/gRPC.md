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

### How gRPC Works – Internally

1. Define the API using .proto  
```
syntax = "proto3";

service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply);
}

message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}
```

2. Generate Code

    * The protoc compiler generates code (client and server stubs) in your language (Java, Go, Python, etc.)

3. Server Side

    * Implements the generated interface

    * Runs a gRPC server to handle incoming requests

4. Client Side

    * Uses the stub to call remote methods

    * Stub handles serialization (Protobuf), network calls (HTTP/2), and deserialization
  
