package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.PreRequest;
import mit.iwrcore.IWRCore.entity.Request;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.PreRequestRepository;
import mit.iwrcore.IWRCore.repository.RequestRepository;
import mit.iwrcore.IWRCore.security.dto.PreRequestDTO;
import mit.iwrcore.IWRCore.security.dto.RequestDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PreRequestSturctureDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final RequestRepository requestRepository;
    private final PreRequestRepository preRequestRepository;
    private final MaterialService materialService;
    private final PreRequestService preRequestService;
    private final StructureService structureService;

    // 저장, 삭제
    @Override
    public RequestDTO saveRequest(RequestDTO requestDTO) {
        Request request = dtoToEntity(requestDTO);
        Request savedRequest = requestRepository.save(request);
        return entityToDTO(savedRequest);
    }
    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
    @Override
    public void updateReqCheck(Long requestCode){
        LocalDateTime inputDate=LocalDateTime.now();
        requestRepository.updateReqCheck(inputDate, requestCode);

        RequestDTO requestDTO=getRequestById(requestCode);
        if(requestDTO!=null){
            Long preCode=requestDTO.getPreRequestDTO().getPreReqCode();
            Long allReqCount=requestRepository.getAllRequestCount(preCode);
            Long finReqCount=requestRepository.getFinRequestCount(preCode);
            if(finReqCount.equals(allReqCount)) preRequestService.updateAllCheck(preCode);
        }
    }


    // 변환
    @Override
    public Request dtoToEntity(RequestDTO dto) {
        return Request.builder()
                .requstCode(dto.getRequestCode())
                .requestNum(dto.getRequestNum())
                .eventDate(dto.getEventDate())
                .releaseDate(dto.getReleaseDate())
                .material(dto.getMaterialDTO() != null ? materialService.dtoToEntity(dto.getMaterialDTO()) : null)
                .preRequest(preRequestService.dtoToEntity(dto.getPreRequestDTO()))
                .build();
    }
    @Override
    public RequestDTO entityToDTO(Request entity) {
        return RequestDTO.builder()
                .requestCode(entity.getRequstCode())
                .requestNum(entity.getRequestNum())
                .eventDate(entity.getEventDate())
                .releaseDate(entity.getReleaseDate())
                .materialDTO(entity.getMaterial() != null ? materialService.entityToDto(entity.getMaterial()) : null) // Material을 MaterialDTO로 변환
                .preRequestDTO(preRequestService.entityToDTO(entity.getPreRequest()))
                .build();
    }

    // 조회
    @Override
    public RequestDTO getRequestById(Long id) {
        return entityToDTO(requestRepository.findById(id).get());
    }
    @Override
    public List<RequestDTO> getRequestByPreRequest(Long preCode){
        List<Request> entityList=requestRepository.getRequestByPreRequest(preCode);
        return entityList.stream().map(x->entityToDTO(x)).toList();
    }


    @Override
    public RequestDTO updateRequest(Long id, RequestDTO requestDTO) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("ID가 " + id + "인 RequestDTO를 찾을 수 없습니다.");
        }
        Request request = dtoToEntity(requestDTO);
        request.setRequstCode(id); // 수정할 때 ID를 설정합니다.
        Request updatedRequest = requestRepository.save(request);
        return entityToDTO(updatedRequest);
    }



    // 출하요청> 출하요청 수정시 목록
    @Override
    public List<PreRequestSturctureDTO> getStructureStock(Long preReqCode){
        List<Object[]> list = preRequestRepository.getStructureStock(preReqCode);
        return list.stream().map(this::PreRequestSturctureDTOToDTO).toList();
    }
    private PreRequestSturctureDTO PreRequestSturctureDTOToDTO(Object[] objects) {
        PreRequest preRequest = (PreRequest) objects[0];
        Structure structure = (Structure) objects[1];
        Request request = (Request) objects[2];
        Long tempSumShip = (Long) objects[3];
        Long tempSumRequest = (Long) objects[4];

        PreRequestDTO preRequestDTO = (preRequest != null) ? preRequestService.entityToDTO(preRequest) : null;
        StructureDTO structureDTO = (structure != null) ? structureService.entityToDto(structure) : null;
        RequestDTO requestDTO = (request !=null) ? entityToDTO(request) : null;
        Long sumShip = (tempSumShip != null) ? tempSumShip : 0L;
        Long sumRequest = (tempSumRequest != null) ? tempSumRequest : 0L;

        return new PreRequestSturctureDTO(preRequestDTO, structureDTO, requestDTO, sumRequest, sumShip);
    }
    // 메인화면> 출하갯수
    @Override
    public Long mainRequestCount(){
        Long aa=requestRepository.mainRequestCount();
        return (aa!=null)?aa:0L;
    }
}