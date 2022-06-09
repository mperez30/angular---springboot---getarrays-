package io.getarrays.server.service.implementation;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repository.ServerRepository;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

import static io.getarrays.server.enumeration.Status.SERVER_DOWN;
import static io.getarrays.server.enumeration.Status.SERVER_UP;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    /**
     * @param server
     * @return
     */
    @Override
    public Server create(Server server) {
        log.info("Saving new server:{}",server.getName());
        server.setImageUrl(setServerImageUrl());

        return serverRepository.save(server);
    }



    /**
     * @param ipAddress 
     * @return
     */
    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP:{}",ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    /**
     * @param limit
     * @return
     */
    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Server get(Long id) {
        log.info("Pinging server by id:{}",id);

        return serverRepository.findById(id).get();
    }

    /**
     * @param server
     * @return
     */
    @Override
    public Server update(Server server) {
        log.info("Saving  server:{}",server.getName());
        return serverRepository.save(server);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Boolean delete(Long id) {
        log.info("Deleting  server by ID:{}",id);
        serverRepository.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageUrl() {
        String imageNames = "server1.png" ;
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/"+imageNames).toUriString();
    }
}
