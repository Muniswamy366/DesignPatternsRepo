### OAuth 2.0 (Authorization Framework)
OAuth 2.0 is an authorization framework that allows third-party applications to obtain limited access to a user's resources without exposing credentials (like passwords).  
**Key Use Case:**  

    "Let an app access my Facebook/Google data without giving it my password."

### Key Roles:
| Role                     | Description                                               |
| ------------------------ | --------------------------------------------------------- |
| **Resource Owner**       | The user who authorizes access to their account.          |
| **Client**               | The app requesting access (e.g., a mobile/web app).       |
| **Resource Server**      | Where the data resides (e.g., API like Google Drive API). |
| **Authorization Server** | Issues tokens after successful authentication.            |

### OAuth2 Grant Types (Flows):
| Grant Type              | Use Case                                           |
| ----------------------- | -------------------------------------------------- |
| Authorization Code      | Web apps (most secure, uses redirection).          |
| Client Credentials      | Server-to-server communication.                    |
| Resource Owner Password | Deprecated; username/password given to the client. |
| Implicit                | Now discouraged; used by SPAs (single page apps).  |
| Device Code             | Devices with limited UI (e.g., smart TVs).         |

### Token Types:

    Access Token: Short-lived, used to access protected resources.

    Refresh Token: Used to obtain a new access token when the old one expires.

## OpenID Connect (OIDC)
OIDC is an authentication layer built on top of OAuth2. It lets you verify the user's identity and obtain their profile info.  
### Key Use Case:

    "Log in to my app using Google/Facebook."

### OIDC Adds to OAuth2:
| Feature                | Description                                             |
| ---------------------- | ------------------------------------------------------- |
| **ID Token**           | A JWT that contains user identity (name, email, etc.).  |
| **UserInfo Endpoint**  | API to fetch more user profile details.                 |
| **Discovery Endpoint** | Metadata endpoint (`.well-known/openid-configuration`). |

### OIDC Flow:

    Client sends user to the authorization server.

    User logs in and consents.

    Client gets an ID Token and Access Token.

    ID Token is used to authenticate the user.

    Access Token is used to call protected APIs.

### JWT (JSON Web Token)
JWT is a compact, self-contained token format often used to represent claims between two parties (e.g., identity, roles).

### Structure:
```
HEADER.PAYLOAD.SIGNATURE
```

```
Header:   { "alg": "RS256", "typ": "JWT" }
Payload:  { "sub": "123456", "name": "John", "exp": 1717954800 }
Signature: HMACSHA256(base64url(Header) + "." + base64url(Payload), secret)
```

```
{
  "iss": "https://accounts.google.com",
  "sub": "1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "iat": 1717950000,
  "exp": 1717953600,
  "aud": "my-client-id"
}
```

### Common Claims in Payload:
| Claim | Meaning           |
| ----- | ----------------- |
| `sub` | Subject (user ID) |
| `exp` | Expiration time   |
| `iat` | Issued at         |
| `aud` | Audience          |
| `iss` | Issuer            |


### When to Use What?
| Scenario                                  | Use            |
| ----------------------------------------- | -------------- |
| Let users log in using Google or Facebook | OpenID Connect |
| Give an app limited access to APIs        | OAuth 2.0      |
| Stateless authentication with user roles  | JWT            |    




https://www.youtube.com/watch?v=ZDuRmhLSLOY
