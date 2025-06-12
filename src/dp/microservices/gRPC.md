### What is gRPC?

gRPC (Google Remote Procedure Call) is a high-performance, open-source RPC (Remote Procedure Call) framework developed by Google.  

It allows client and server applications to communicate directly by calling methods on remote objects as if they were local.  
It is built on:  

    HTTP/2 for transport
    Protocol Buffers (Protobuf) for serialization
    Auto-generated client/server code for multiple languages

### Key Components of gRPC
| Component                       | Description                                                                        |  
| ------------------------------- | ---------------------------------------------------------------------------------- |  
| **Client**                      | Makes remote procedure calls (RPC) to the server.                                  |  
| **Server**                      | Implements the service, listens to RPC requests.                                   |  
| **Service Definition (.proto)** | Declares the RPC services and message types using Protocol Buffers.                |  
| **Protocol Buffers**            | A compact binary format used for defining and serializing structured data.         |  
| **gRPC Stub**                   | Auto-generated client/server code based on `.proto` file, handles network details. |  

### What makes grpc is faster than http? 
* Uses HTTP/2 (multiplexing - Multiple calls over a single TCP connection, compression)
* Uses binary Protobuf (compact, fast) Avoids manual serialization/parsing using jackson.
* Supports streaming

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

### Types of RPCs in gRPC

![1_BFcscGPLpeCW6xNmE6xTMQ](https://github.com/user-attachments/assets/4ff4740a-b94c-42fb-9782-fc4360a8c8d9)  


### gRPC Transport: HTTP/2 Features

gRPC uses HTTP/2, which offers:

    Multiplexing: Multiple calls over a single TCP connection

    Streaming: Full-duplex communication

    Header compression: Reduces size of metadata

    Binary framing: More efficient than text-based HTTP/1.1

### gRPC vs REST – Internal Mechanics  
| Feature             | gRPC                        | REST                            |
| ------------------- | --------------------------- | ------------------------------- |
| **Protocol**        | HTTP/2                      | HTTP/1.1                        |
| **Serialization**   | Protobuf (binary)           | JSON (text)                     |
| **Streaming**       | Built-in support            | Not native                      |
| **Code Generation** | Yes (strong typing)         | Optional (via Swagger/OpenAPI)  |
| **Performance**     | Faster                      | Slower (due to JSON, no HTTP/2) |
| **Browser Support** | Limited (requires gRPC-web) | Full                            |

### Advantages of gRPC
| Benefit                    | Why It Matters                       |
| -------------------------- | ------------------------------------ |
| **High Performance**       | Protobuf + HTTP/2 = fast and compact |
| **Strong Typing**          | Contracts are enforced via `.proto`  |
| **Streaming Support**      | For real-time communication          |
| **Multi-language Support** | Java, Python, Go, C#, Node.js, etc.  |
| **Code Generation**        | Reduces boilerplate                  |
| **Backward Compatibility** | Proto3 supports safe evolution       |


### Limitations of gRPC
| Limitation            | Details                                                                    |
| --------------------- | -------------------------------------------------------------------------- |
| **Browser support**   | Native browsers don’t support HTTP/2 framing – need gRPC-web or REST proxy |
| **Complex debugging** | Binary messages are hard to read compared to JSON                          |
| **Learning curve**    | Requires understanding Protobuf, stubs, gRPC lifecycle                     |
| **Verbose setup**     | More initial setup than REST                                               |

### Common Use Cases

* Microservices communication (internal services)
* Mobile-to-backend communication (low bandwidth)
* Real-time apps (chat, gaming, streaming)
* IoT systems with lightweight messaging

### Life Cycle of a Unary gRPC Call

1. Client calls stub.sayHello(name)
2. Stub:
    * Serializes HelloRequest using Protobuf
    * Sends over HTTP/2
3. Server receives and deserializes message
4. Server calls implementation of sayHello
5. Server serializes HelloReply and returns
6. Client stub receives, deserializes, and returns the result

### Summary

* gRPC is a high-performance RPC framework ideal for microservices, streaming, and cross-language communication.
* It uses Protocol Buffers and HTTP/2 for speed and efficiency.
* Great for internal services, but REST may be better for public APIs and browser clients.

### What makes grpc is faster than http? 

1. Uses HTTP/2 Instead of HTTP/1.1  
HTTP/2 Advantages:

| Feature                | HTTP/2 (gRPC)                           | HTTP/1.1 (REST)                    |
| ---------------------- | --------------------------------------- | ---------------------------------- |
| **Multiplexing**       | ✅ Multiple streams over 1 connection    | ❌ One request per connection       |
| **Binary framing**     | ✅ Efficient frame-based binary protocol | ❌ Text-based                       |
| **Header compression** | ✅ (HPACK) reduces overhead              | ❌ Headers sent as-is               |
| **Connection reuse**   | ✅ Persistent + concurrent streams       | ❌ Often needs multiple connections |

Result: Lower latency, less overhead, and better resource usage

2. Binary Protocol (Protocol Buffers) Instead of JSON  
Protobuf (used in gRPC) vs JSON:

| Feature    | Protobuf (gRPC)               | JSON (REST)             |
| ---------- | ----------------------------- | ----------------------- |
| **Size**   | Compact, binary format        | Verbose, text format    |
| **Speed**  | Fast to serialize/deserialize | Slower, more CPU        |
| **Schema** | Strongly typed via `.proto`   | Schema-less or optional |

Result: Smaller messages, faster parsing = faster communication

3. Streaming Support  
* Client streaming
* Server streaming
* Bidirectional streaming
Enables real-time communication with low latency, unlike REST which requires multiple round-trips.

5. Code Generation for Stubs  
* gRPC generates client/server code from .proto definitions.
* No runtime parsing or reflection is needed.
Less runtime logic = better performance  
gRPC can be 5–10x faster in real-world benchmarks.

### Conclusion

* gRPC is faster than HTTP/REST because it:
* Uses HTTP/2 (multiplexing, compression)
* Uses binary Protobuf (compact, fast)
* Supports streaming
* Avoids manual serialization/parsing
