package com.example.demo.controller;

import com.example.demo.model.travel.TmapTravelDto;
import com.example.demo.model.travel.TravelDto;
import com.example.demo.service.MagazineService;
import com.example.demo.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/travel")
public class TravelController {
    private TravelService travelService;
    private MagazineService magazineService;
    public TravelController(TravelService travelService, MagazineService magazineService) {
        this.travelService = travelService;
        this.magazineService = magazineService;
    }

    @PostMapping("/regist")
    public ResponseEntity<?> regist(@RequestBody List<TmapTravelDto> tmapTravelDtos, @RequestParam("travelName") String travelName, @RequestParam("travelDay") int travelDay) throws Exception{
        try{
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            Boolean isOwner = true;
            travelService.registTravel(tmapTravelDtos, travelName, userId, travelDay, isOwner);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/view/{travelId}")
    public ResponseEntity<?> view(@PathVariable("travelId") String travelId) throws Exception{
        try{
            String userId =  SecurityContextHolder.getContext().getAuthentication().getName();
            TravelDto travelDto = travelService.viewTravel(travelId, userId);
            return new ResponseEntity<TravelDto>(travelDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() throws Exception{
        try{
            List<TravelDto> travelDtoList = travelService.listTravel();
            return new ResponseEntity<List<TravelDto>>(travelDtoList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/check/{travelId}")
    public ResponseEntity<?> duplicateCheck(@PathVariable("travelId") String travelId) throws Exception{
        try{
            String userId =  SecurityContextHolder.getContext().getAuthentication().getName();

            List<String> travelIds = travelService.checkTravelId(userId, travelId);
            return new ResponseEntity<List<String>>(travelIds, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    // 여행 복사
    @GetMapping("/duplicate/{travelId}")
    public ResponseEntity<?> duplicateTravel(@PathVariable("travelId") String travelId, @RequestParam("magazineId") String MagazineId ) throws Exception{
        try{
            String userId =  SecurityContextHolder.getContext().getAuthentication().getName();
            travelService.duplicateTravel(travelId, userId);
            // hit 올리기
             magazineService.updateMagazineHit(MagazineId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/update/{travelId}")
    public ResponseEntity<?> updateTravel(@PathVariable("travelId") String travelId,
                                          @RequestBody List<TmapTravelDto> tmapTravelDtos, @RequestParam("travelName") String travelName, @RequestParam("travelDay") int travelDay){
        try{
            String userId =  SecurityContextHolder.getContext().getAuthentication().getName();
            travelService.deleteTravel(travelId, userId);
            Boolean isOwner = true;
            travelService.registTravel(tmapTravelDtos, travelName, userId, travelDay, isOwner);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/delete/{travelId}")
    public ResponseEntity<?> deleteTravel(@PathVariable("travelId") String travelId){
        try{
            String userId =  SecurityContextHolder.getContext().getAuthentication().getName();
            List<String> magazineIds = magazineService.checkMagazine(travelId);
            for (String magazineId:magazineIds){
                magazineService.deleteCommentAll(magazineId);
            }
            for(String magazineId : magazineIds){
                magazineService.deleteMagazineDetail(magazineId);
            }
            for(String magazineId : magazineIds){
                magazineService.deleteMagazine(magazineId);
            }
            travelService.deleteTravel(travelId, userId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
