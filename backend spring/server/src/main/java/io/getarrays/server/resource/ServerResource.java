package io.getarrays.server.resource;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Response;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImpl serverservice;

    @GetMapping("/list")
    @CrossOrigin(origins = "http://localhost", maxAge = 3600)
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(Response.builder().timeStamp(LocalDateTime.now()).data(Map.of("servers",serverservice.list(30)))
                .message("servers retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }



    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverservice.ping(ipAddress);

        return ResponseEntity.ok(Response.builder().timeStamp(LocalDateTime.now()).data(Map.of("servers",serverservice.list(30)))
                .message(server.getStatus()== Status.SERVER_UP ? "Ping success" : "Ping failed")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }


    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) throws IOException {
        return ResponseEntity.ok(Response.builder().timeStamp(LocalDateTime.now()).data(Map.of("servers",serverservice.create(server)))
                .message("Server Created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(Response.builder().timeStamp(LocalDateTime.now()).data(Map.of("servers",serverservice.get(id)))
                .message("Server retrieved")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(Response.builder().timeStamp(LocalDateTime.now()).data(Map.of("deleted",serverservice.delete(id)))
                .message("Server deleted")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    @GetMapping(path = "/image/{fileName}",produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Descargas/" + fileName));
    }

}
