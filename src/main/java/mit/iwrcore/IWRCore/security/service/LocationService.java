package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.Location;
import mit.iwrcore.IWRCore.repository.LocationRepository;
import mit.iwrcore.IWRCore.security.dto.LocationDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    // 저장, 삭제
    public void saveLocation(LocationDTO dto){
        LocalDateTime date=LocalDateTime.now();
        dto.setUseTime(date);
        locationRepository.save(dtoToEntity(dto));
    }
    public void deleteLocation(Long lno){
        locationRepository.deleteById(lno);
    }

    // 페이지
    public PageResultDTO<LocationDTO, Location> getLocationList(PageRequestDTO2 requestDTO){
        Page<Location> entityPage=locationRepository.findLocationByCustomQuery(requestDTO);
        Function<Location, LocationDTO> fn=(entity->entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }

    // 변환
    public LocationDTO entityToDto(Location entity){
        if(entity==null) return null;
        else{
            LocationDTO dto=LocationDTO.builder()
                    .lno(entity.getLno()).location(entity.getLocation()).useTime(entity.getUseTime()).build();
            return dto;
        }
    }
    public Location dtoToEntity(LocationDTO dto){
        if(dto==null) return null;
        else{
            Location entity=Location.builder()
                    .lno(dto.getLno()).location(dto.getLocation()).useTime(dto.getUseTime()).build();
            return entity;
        }
    }
}
