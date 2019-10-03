package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Node;
import br.ufpa.labes.spm.repository.NodeRepository;
import br.ufpa.labes.spm.service.dto.NodeDTO;
import br.ufpa.labes.spm.service.mapper.NodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Node}.
 */
@Service
@Transactional
public class NodeService {

    private final Logger log = LoggerFactory.getLogger(NodeService.class);

    private final NodeRepository nodeRepository;

    private final NodeMapper nodeMapper;

    public NodeService(NodeRepository nodeRepository, NodeMapper nodeMapper) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
    }

    /**
     * Save a node.
     *
     * @param nodeDTO the entity to save.
     * @return the persisted entity.
     */
    public NodeDTO save(NodeDTO nodeDTO) {
        log.debug("Request to save Node : {}", nodeDTO);
        Node node = nodeMapper.toEntity(nodeDTO);
        node = nodeRepository.save(node);
        return nodeMapper.toDto(node);
    }

    /**
     * Get all the nodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NodeDTO> findAll() {
        log.debug("Request to get all Nodes");
        return nodeRepository.findAll().stream()
            .map(nodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the nodes where TheStructure is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<NodeDTO> findAllWhereTheStructureIsNull() {
        log.debug("Request to get all nodes where TheStructure is null");
        return StreamSupport
            .stream(nodeRepository.findAll().spliterator(), false)
            .filter(node -> node.getTheStructure() == null)
            .map(nodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one node by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NodeDTO> findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        return nodeRepository.findById(id)
            .map(nodeMapper::toDto);
    }

    /**
     * Delete the node by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.deleteById(id);
    }
}
