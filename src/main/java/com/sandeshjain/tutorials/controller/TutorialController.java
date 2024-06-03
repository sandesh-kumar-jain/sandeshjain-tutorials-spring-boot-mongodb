package com.sandeshjain.tutorials.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandeshjain.tutorials.exception.ResourceNotFoundException;
import com.sandeshjain.tutorials.exception.UserException;
import com.sandeshjain.tutorials.model.Tutorial;
import com.sandeshjain.tutorials.repository.TutorialRepository;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@Slf4j
public class TutorialController {

  @Autowired
  TutorialRepository tutorialRepository;

  @GetMapping("/tutorials")
  public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
      @RequestParam(required = false) String title,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size) {
    log.info("TutorialController :: getAllTutorialsPage :: Started ");
    try {
      List<Tutorial> tutorials = new ArrayList<Tutorial>();
      Pageable paging = PageRequest.of(page, size);
      
      Page<Tutorial> pageTuts;
      if (title == null)
        pageTuts = tutorialRepository.findAll(paging);
      else
        pageTuts = tutorialRepository.findByTitleContainingIgnoreCase(title, paging);

      tutorials = pageTuts.getContent();

      Map<String, Object> response = new HashMap<>();
      response.put("tutorials", tutorials);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());
      log.info("TutorialController :: getAllTutorialsPage :: Completed : {} ",response.toString());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
//      return new ResponseEntity<>(throw new Exception, HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("TutorialController :: getAllTutorialsPage :: Exception : {} ",e.getMessage());
      throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
  }

  @GetMapping("/tutorials/published")
  public ResponseEntity<Map<String, Object>> findByPublished(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size) {
    log.info("TutorialController :: findByPublished :: Started ");
    try {      
      List<Tutorial> tutorials = new ArrayList<Tutorial>();
      Pageable paging = PageRequest.of(page, size);
      
      Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true, paging);
      tutorials = pageTuts.getContent();
      
      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("tutorials", tutorials);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());
      log.info("TutorialController :: findByPublished :: Completed : {} ",response.toString());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("TutorialController :: findByPublished :: Exception : {} ",e.getMessage());
        throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
    	
    	
  }

  @GetMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") String id) {
    log.info("TutorialController :: getTutorialById :: Started : {} ",id);
    Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

    if (tutorialData.isPresent()) {
      log.info("TutorialController :: getTutorialById :: Completed : {} ",id);
      return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	throw new ResourceNotFoundException("Not found Tutorial with id = " + id);
    }
  }

  @PostMapping("/tutorials")
  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
    try {
      log.info("TutorialController :: createTutorial :: Started ");
      Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
      log.info("TutorialController :: createTutorial :: Completed : {}",_tutorial.getId());
      return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    } catch (Exception e) {
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("TutorialController :: createTutorial :: Exception : {} ",e.getMessage());
      throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
  }

  @PutMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial) {
    log.info("TutorialController :: updateTutorial :: Started : {} ",id);
    Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

    if (tutorialData.isPresent()) {
      Tutorial _tutorial = tutorialData.get();
      _tutorial.setTitle(tutorial.getTitle());
      _tutorial.setDescription(tutorial.getDescription());
      _tutorial.setPublished(tutorial.isPublished());
      log.info("TutorialController :: updateTutorial :: Completed : {} ",id);
      return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
    } else {
    	throw new ResourceNotFoundException("Not found Tutorial with id = " + id);
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {
    try {
      log.info("TutorialController :: deleteTutorial :: Started : {} ",id);
      tutorialRepository.deleteById(id);
      log.info("TutorialController :: deleteTutorial :: Completed : {} ",id);
      return new ResponseEntity<>(HttpStatus.OK);
//      throw new ResourceNotFoundException("Not found Tutorial with id = " + id);
    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("TutorialController :: deleteTutorial :: Exception : {} ",e.getMessage());
        throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
  }

  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    try {
      log.info("TutorialController :: deleteAllTutorials :: Started ");
      tutorialRepository.deleteAll();
      log.info("TutorialController :: deleteAllTutorials :: Completed ");
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("TutorialController :: deleteAllTutorials :: Exception : {} ",e.getMessage());
        throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

  }
}
