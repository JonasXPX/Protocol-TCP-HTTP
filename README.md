## Protocol Academy Test Project

### Getting Started

Start cloning the project in your projects directory

```
git clone https://github.com/JonasXPX/Protocol-TCP-HTTP.git protocol
cd project/
```

Running the project
```
mvn install exec:java
```

*Obs: Port by default is 80*

### Usage
Default page is ``http://localhost/`` will return ``200 OK``

Any page, exept ``/``, will return ``404 Not found``

### Infos

HTTP is a message send by TPC protocol.
The contents of HTTP is basic a Headers, and the body of page.
The first line of HTTP message is the Method and the Response send by the server:
![Protocol](https://mdn.mozillademos.org/files/13687/HTTP_Request.png)

