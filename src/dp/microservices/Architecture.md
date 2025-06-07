
![image](https://github.com/user-attachments/assets/fc321b4b-10bf-4f2f-9057-e909d29fbae6)  

  
#### Step-by-Step Request Flow
1. Initial Request
* User sends HTTP request to the API Gateway  
* Request targets a specific service (e.g., /api/service-a/resource)  

2. Authentication Check
* Gateway checks if request has valid authentication  
* If not authenticated, redirects to Keycloak  
* User provides credentials to Keycloak  

3. Token Issuance
* Keycloak validates credentials  
* Keycloak issues JWT tokens (access token, refresh token)  
* Keycloak redirects back to gateway with tokens
* Gateway stores tokens in user session

4. Token Validation
* Gateway validates the JWT token with Keycloak
* Confirms token is not expired, properly signed, and valid

5. Service Discovery
* Gateway queries Kubernetes API to discover available services
* Kubernetes returns list of available service instances (pods)
* Gateway gets endpoints for the requested service (e.g., Service A)

6. Load Balancing
* Gateway selects a specific pod instance using load balancing algorithm
* Typically round-robin, but can be weighted or other strategies
* Selection is made from available healthy pods

7. Token Relay & Request Forwarding
* Gateway adds the user's access token to outgoing request as Authorization header
* Gateway forwards the request to selected pod instance
* Original request path is modified according to routing rules

8. Service Processing
* Service pod receives the request with the token
* Service validates the token and extracts user information
* Service applies authorization rules based on token claims
* Service processes the business logic and prepares response

9. Response to Gateway
* Service pod sends response back to the API Gateway
* Response includes status code, headers, and body

10. Response to User
* Gateway may apply response filters or transformations
* Gateway forwards the final response to the original user
* User receives the response
