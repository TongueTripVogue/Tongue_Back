package com.example.demo.model.mapper;

import com.example.demo.model.magazine.MagazineDetailDto;
import com.example.demo.model.magazine.MagazineDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MagazineMapper {
    void createMagazine(MagazineDto magazineDto) throws Exception;
    MagazineDto viewMagazine(String magazine_id) throws Exception;
    List<String> getMagazineIdList(String userId) throws Exception;
    List<MagazineDto> getMagazineList(String userId) throws Exception;
    void deleteMagazine(String magazineId) throws Exception;
    List<String> checkMagazine(String travelId) throws Exception;
    void updateHit(String magazineId) throws Exception;
    List<MagazineDto> getAllMagazine() throws Exception;
    List<MagazineDto> getHotMagazine() throws Exception;
    List<MagazineDto> getNewMagazine() throws Exception;
}
