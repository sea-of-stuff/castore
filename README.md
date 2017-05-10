[![CircleCI](https://circleci.com/gh/stacs-srg/castore.svg?style=svg&circle-token=619302303a40d36f6e718006d26a804711dcbc91)](https://circleci.com/gh/stacs-srg/castore) [![codecov](https://codecov.io/gh/stacs-srg/castore/branch/master/graph/badge.svg?token=cvFOJtw4wS)](https://codecov.io/gh/stacs-srg/castore)


# castore = cast + store
*A very minimal and simple library to interact with different storage systems*

File systems and storage systems all expose very similar, but somewhat different, interfaces to interact with files and directories.
Here we provide a very simple and basic interface that abstract various storage systems.

To use **castore** you can add the following maven dependency:

```
<dependency>
    <groupId>uk.ac.standrews.cs</groupId>
    <artifactId>castore</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

This project is not available via a maven repository yet, so you will have to install this yourself by running `mvn install`.

Alternatively you can produce a `.jar` for the project, `mvn package -am -DskipTests`.

---

## Available implementations

### File Based

This implementation is based on the local file system.

**Settings**: type, root path

### AWS.s3

In order to work with an AWS S3 storage, you need to provide an access_key_id and a secret_access_key.
This can be done explicitly or by setting the environment variables:
```
export AWS_ACCESS_KEY_ID=<KEY>
export AWS_SECRET_KEY=<KEY>
```

**Settings**: type, bucket name, credentials (optional)


### Redis

Redis is an in-memory data structure store, which is networked and very fast.
A Redis-based storage can be used to store small amount of data with good performance.

Before using the Redis-based storage, you need [Redis](https://redis.io) installed. Then run a server instance: `redis-server`

**Settings**: type, host, port (optional)


### Dropbox

First you need to create a Dropbox app and get an App Token.

**Settings**: type, path to dropbox folder, token


## Examples

Examples are available [here](src/main/java/uk/ac/standrews/cs/storage/examples).

---

## Future Plans

- More implementations:
    - In-memory
    - GUID-based
    - Google Drive
    - OneDrive
    - WebDAV
    - FTP
- Make a storage instance available via a REST API
- CLI

---

## Continous Integration

CircleCI automatically builds and runs the tests for the project.

Codecov, with JaCoCo, is used to measure the test coverage for the project. Info on how to read the Codecov reports: https://docs.codecov.io/v4.3.6/docs/viewing-source-code

---

## Credits

This project is inspired by the `asa/filesystem` project developed by *Alan Dearle*, *Graham Kirby*, and *Stuart Norcross* at the University of St Andrews.

The **castore** project has been developed, and is currently maintained, by *Simone I. Conte* at the University of St Andrews.

Suggestions and Pull Requests are more than welcome.

