## Makes it possible to use nginx' auth_request module with Microsoft azures active directory and oauth2

![circleci](https://circleci.com/gh/Gontrum/azureadtonginx.svg?style=shield&circle-token=596e6c783e661fb1468c59d781b975f84b32b036)

### Example Configuration:

#### docker-compose.yml
```yaml
version: '3'

services:
  reverseproxy:
    image: nginx:1.15.7-alpine
    ports:
      - "443:443"
    volumes:
      - "./nginx/nginx.conf:/etc/nginx/nginx.conf"
      - "./nginx/certs:/etc/nginx/certs/"
      - "./niceapp:/var/www/nicewebsite.gontrum.io"
  authentication:
    image: davidgontrum/azureadtonginx:0.1.4
    environment:
      - AD_ID=22222222-2222-2222-2222-222222222222
      - CLIENT_ID=33333333-3333-3333-3333-333333333333
      - CLIENT_SECRET=topSecret
      - GROUP=Users
```


```json
events {}

http {
  server {
    listen              443 ssl;
    server_name         nicewebsite.gontrum.io;
    ssl_certificate     certs/nicewebsite.gontrum.io.crt;
    ssl_certificate_key certs/nicewebsite.gontrum.io.key;
    ssl_protocols       TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers         HIGH:!aNULL:!MD5;

    root /var/www/nicewebsite.gontrum.io/;

    error_page 401 = @error401;
    location @error401 {
      return 302 /login;
    }

    # that's the location which has restricted access
    # user will be redirected to microsoft login-form
    # if she is not already authenticated
    location = /restricted {
        proxy_pass http://somewhereelse.inthe.world;
        auth_request /auth;
    }

    location = /login {
      proxy_pass http://authentication:8080;
    }

    location = /oauth2/authorization/azure {
      proxy_set_header X-Forwarded-Proto  $scheme;
      proxy_set_header Host            $host;
      #proxy_set_header X-Forwarded-For $remote_addr;
      proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_pass http://authentication:8080;
    }

    location = /login/oauth2/code/azure {
      proxy_set_header X-Forwarded-Proto  $scheme;
      proxy_set_header Host            $host;
      #proxy_set_header X-Forwarded-For $remote_addr;
      proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_pass http://authentication:8080;
    }

    location = /auth {
      proxy_pass http://authentication:8080;

      proxy_pass_request_body     off;

      proxy_set_header Content-Length "";
      proxy_set_header X-Original-URI $request_uri;
      proxy_set_header Host $http_host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Proto $scheme;
    }
  }
}
```
