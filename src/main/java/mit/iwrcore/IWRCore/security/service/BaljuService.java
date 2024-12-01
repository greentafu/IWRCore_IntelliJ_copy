package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Balju;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ContractBaljuDTO;

import java.util.List;

public interface BaljuService {
    // 저장, 삭제
    BaljuDTO saveBalju(BaljuDTO baljuDTO);
    void deleteBalju(Long id);
    BaljuDTO modifyBalju(BaljuDTO baljuDTO);

    // 변환
    Balju dtoToEntity(BaljuDTO dto);
    BaljuDTO entityToDTO(Balju entity);

    // 조회
    BaljuDTO getBalju(Long id);
    BaljuDTO getRecentBaljuByMaterial(Long materCode);
    BaljuDTO getBaljuByContract(Long conNo);
    Long getBaljuCountByContract(Long conNo);

    // 계약서> 계약 완료 목록
    PageResultDTO<ContractBaljuDTO, Object[]> finishedContract(PageRequestDTO2 requestDTO);
    // 발주서> 발주 해야하는 계약 목록
    PageResultDTO<ContractBaljuDTO, Object[]> couldBalju(PageRequestDTO requestDTO);
    // 발주서> 발주완료한 목록
    PageResultDTO<ContractBaljuDTO, Object[]> finBaljuPage(PageRequestDTO2 requestDTO);
    // 발주서> 협력회사별 발주 가능한 목록
    List<BaljuDTO> partListBalju(Long pno);
    // 협력회사> 협력회사용 발주서 목록
    PageResultDTO<BaljuDTO, Balju> partnerBaljuList(PageRequestDTO requestDTO);
    // 메인화면> 발주 중인 제품 목록
    List<ProductDTO> baljuProduct();
}