## Setup project

Run `mvn clean install` first.

#### Generating JWT public & private key

Use the set of following commands below to generate the keys.

```
cd src\main\resources
mkdir certs
cd certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
```

#### Database config (Optional)

Database source can be configured in `src\main\resources\application.properties`.

## Start server

Run `mvn spring-boot:run` to start the server.

The main class `ClassReserver` can also be run to start the server directly.