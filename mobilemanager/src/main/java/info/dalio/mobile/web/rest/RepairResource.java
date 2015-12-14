package info.dalio.mobile.web.rest;

import com.codahale.metrics.annotation.Timed;
import info.dalio.mobile.domain.Repair;
import info.dalio.mobile.repository.RepairRepository;
import info.dalio.mobile.repository.search.RepairSearchRepository;
import info.dalio.mobile.web.rest.util.HeaderUtil;
import info.dalio.mobile.web.rest.util.PaginationUtil;
import info.dalio.mobile.web.rest.dto.RepairDTO;
import info.dalio.mobile.web.rest.mapper.RepairMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Repair.
 */
@RestController
@RequestMapping("/api")
public class RepairResource {

    private final Logger log = LoggerFactory.getLogger(RepairResource.class);

    @Inject
    private RepairRepository repairRepository;

    @Inject
    private RepairMapper repairMapper;

    @Inject
    private RepairSearchRepository repairSearchRepository;

    /**
     * POST  /repairs -> Create a new repair.
     */
    @RequestMapping(value = "/repairs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RepairDTO> createRepair(@Valid @RequestBody RepairDTO repairDTO) throws URISyntaxException {
        log.debug("REST request to save Repair : {}", repairDTO);
        if (repairDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new repair cannot already have an ID").body(null);
        }
        Repair repair = repairMapper.repairDTOToRepair(repairDTO);
        Repair result = repairRepository.save(repair);
        repairSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/repairs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("repair", result.getId().toString()))
                .body(repairMapper.repairToRepairDTO(result));
    }

    /**
     * PUT  /repairs -> Updates an existing repair.
     */
    @RequestMapping(value = "/repairs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RepairDTO> updateRepair(@Valid @RequestBody RepairDTO repairDTO) throws URISyntaxException {
        log.debug("REST request to update Repair : {}", repairDTO);
        if (repairDTO.getId() == null) {
            return createRepair(repairDTO);
        }
        Repair repair = repairMapper.repairDTOToRepair(repairDTO);
        Repair result = repairRepository.save(repair);
        repairSearchRepository.save(repair);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("repair", repairDTO.getId().toString()))
                .body(repairMapper.repairToRepairDTO(result));
    }

    /**
     * GET  /repairs -> get all the repairs.
     */
    @RequestMapping(value = "/repairs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RepairDTO>> getAllRepairs(Pageable pageable)
        throws URISyntaxException {
        Page<Repair> page = repairRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repairs");
        return new ResponseEntity<>(page.getContent().stream()
            .map(repairMapper::repairToRepairDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /repairs/:id -> get the "id" repair.
     */
    @RequestMapping(value = "/repairs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RepairDTO> getRepair(@PathVariable Long id) {
        log.debug("REST request to get Repair : {}", id);
        return Optional.ofNullable(repairRepository.findOne(id))
            .map(repairMapper::repairToRepairDTO)
            .map(repairDTO -> new ResponseEntity<>(
                repairDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /repairs/:id -> delete the "id" repair.
     */
    @RequestMapping(value = "/repairs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRepair(@PathVariable Long id) {
        log.debug("REST request to delete Repair : {}", id);
        repairRepository.delete(id);
        repairSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("repair", id.toString())).build();
    }

    /**
     * SEARCH  /_search/repairs/:query -> search for the repair corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/repairs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Repair> searchRepairs(@PathVariable String query) {
        return StreamSupport
            .stream(repairSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
