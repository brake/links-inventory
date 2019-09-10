# Links inventory

## How to run

### Preface

* Server currently works only with Redis instance available at `localhost:6379`. 
* Server currently configured to listen to port 8080
* Everything have been tested on MacOS with Java 8 (Oracle JDK) and latest Redis Docker image.

### Run Redis as a Docker image <sup>optional</sup>

Run a command

```bash
docker run -d --name myredis --restart=always -p 6379:6379 redis redis-server --protected-mode no
```

After command complete Redis is ready.

### Run web server

Download a fat jar file [from github](https://github.com/brake/links-inventory/releases/download/1.1-SNAPSHOT/links-inventory-1.1-SNAPSHOT-all.jar)

Run 

```bash
java -jar links-inventory-<version>-all.jar 
```

If Redis is available at `localhost:6379` then server is ready.
