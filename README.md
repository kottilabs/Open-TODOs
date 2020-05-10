# Open-TODOs
* Open-TODOs is an Open Source API to manage and share tasks in a secure manner

## Repository on Dockerhub
> https://hub.docker.com/repository/docker/kottilabs/open-todos

## Configuration
All options inside the docker container have to be set via the Environment Variable ``TODO_OPTS``.

If you're running your application outside Docker just use the normal ``JAVA_OPTS``. For example:
> ``java -Dspring.profiles.active=swagger -jar app.jar``   

##### Override default admin user details
> ``-Dadmin.username=myAdmin -Dadmin.password=myPassword -Dadmin.displayname=MyAdmin``

##### Enable Swagger-UI
> ``-Dspring.profiles.active=swagger``

## Documentation

##### Build Documentation
Latest documentation can be found under:  
[Documentation](https://raw.githack.com/kottilabs/Open-TODOs/master/docs/index.html)

> Download latest Swagger Code Gen version:  
> https://swagger.io/tools/swagger-codegen/download/

> Run backend locally or inside docker with port ``8080`` exposed and then run:  
> ``java -jar swagger-codegen-cli-2.4.13.jar generate -i "http://localhost:8080/v2/api-docs" -l html2 -o .``