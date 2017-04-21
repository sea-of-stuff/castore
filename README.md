# storage-sta

This project provides a set of storage abstractions and implementations.

This project is independent of the SOS project and can be used in any other Java project.
All you need to do is to add the following to your maven dependencies:

```
<dependency>
    <groupId>uk.ac.standrews.cs</groupId>
    <artifactId>storage-sta</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

This project is not available via a maven repository yet, so you will have to install this yourself `mvn install`.

Alternatively you can produce a `.jar` for the project and add it as a dependency for your project.

## The interface

File systems all expose very similar, but somewhat different, interfaces to interact with files and directories.
Here we provide a very simple and basic interface that abstract various storage systems.
The interface here provided is not complete.


### Storage

### File

### Folder


## Mutable vs Immutable


## Implementations

### File Based

This implementation is based on the local file system.

### Network Based

You need to mount the network drive yourself.

### AWS.s3

- describe behaviour
- give examples


In order to work with an AWS S3 storage, you need to provide an access_key_id and a secret_access_key.
This can be done explicitly or by setting the environment variables:
```
export AWS_ACCESS_KEY_ID=<KEY>
export AWS_SECRET_KEY=<KEY>
```

### Dropbox

WIP

## Limitations

## Credits

This project initially originates from the `asa/filesystem` project developed by *Alan Dearle*, *Graham Kirby*, and *Stuart Norcross* at the University of St Andrews.

This project is currently maintained by *Simone I. Conte* at the University of St Andrews.