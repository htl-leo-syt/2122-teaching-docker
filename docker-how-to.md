# How To Docker

## Dockerfile - Image bauen

```cd``` in das Quellcode-Verzeichnis (Root-Level, dort wo das Dockerfile liegt ...)

- ```-f``` wo liegt das Dockerfile aus Sicht des ```build context```
- ```-t``` definiere Image Tag (image name)
- ```.``` Build-Context (aktuelles Verzeichnis)

### Build-Kommando
```docker build -f ./perhapsanotherfolder/Dockerfile -t my-demo-image .```

Mit ```docker image ls``` neu erstelltes Image überprüfen

### Container starten
- falls das Image ein Server ist, der auf Port 80 horcht ...
    ```docker run -p 80:80 -d my-demo-image```

### Test
http://localhost (Default-Port 80)


## docker-compose

Dient der Verwaltung mehrerer Container in zumindest einem gemeinsamen docker Netzwerks.

### Grundstruktur ```docker-compose.yml```
```
version: "3.8" // Version der yml-Syntax
services:
    // jeder service wird ein container
volumes:
  // Besonders für DB wichtig, 
  // dass Daten die Laufzeit von containern überdauern
networks:
  // Optional können die Eigenschaften des docker networks, das beim Starten dieses docker-compose.yml files erzeugt werden wird, speziell konfiguriert werden
```

- ```docker-compose up -d```: Starten aller containerer
    - ```docker-compose up -d --build```: Vor dem Starten wird ein Erneuern der Docker images erzwungen (=> docker build).
- ```docker-compose down```: Stoppen und remove aller Container, auch das von ```docker-compose up``` erzeugte Network wird wieder gelöscht.

### Service: Asp.Net Backend-Container
```
version: "3.8"
services:
  asp-backend:
    image: asp-demo-with-compose
    build:
      context: ./AspRestBackend
      dockerfile: ./AspRestBackend/Dockerfile
    ports:
      - 5000:80
    container_name: my-asp
```

- ```docker-compose build asp-backend```: Nur das Image für asp-backend wird gebaut (basierend auf dem angegebenen Dockerfile)
- ```docker-compose up -d asp-backend```: Nur der container ```my-asp``` wird gestartet
- Test: http://localhost:5000

### Service: Mariadb (mit adminer)

```
version: "3.8"
services:
  maria-db:
    image: mariadb
    restart: always
    container_name: maria-db
    environment:
      MARIADB_ROOT_PASSWORD: secret
    volumes: 
      - mariadb-volume:/var/lib/mysql
  adminer:
    image: adminer
    restart: always
    ports:
      - 8080:8080
volumes:
  mariadb-volume:
```

- Datenbank-Container wird ohne Port-Mapping gestartet, damit die DB nur innerhalb des docker networks erreichbar ist (z.B. vom backend-Container)
- Der ```adminer```-Container läuft im selben docker-network wie maria-db, daher kann er die DB ansprechen. Da adminer ein Port-Mapping hat, kann die DB nun über diesen Web-Service von außen erreicht werden (während der Entwicklungszeit hilfreich)
- ```mariadb-volume``` sorgt dafür, dass Daten, die container-intern im Pfad ```/var/lib/mysql``` von mariadb abgelegt werden, auch nach Stoppen/Remove/Neustarten des mariadb-containers erhalten bleiben.
- Test: http://localhost:8080
    - server: ```maria-db``` (=> siehe container_name!) 
    - user: ```root```
    - passwort: ```secret```

### Service: node-js-backend

```
version: "3.8"
services:
  nodejs-hello-world:
    image: nodejs-demo-with-compose
    build:
      context: ./Nodejs-hello-world
      dockerfile: ./Dockerfile
    ports:
      - 3030:3000
    container_name: my-nodejs
```

- Test: http://localhost:3030
