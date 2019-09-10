package com.bcauction.application.impl;

import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DigitalWorkService implements IDigitalWorkService
{
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkService.class);

	private IDigitalWorkRepository digitalWorkRepository;
	private IFabricService fabricService;

	@Autowired
	public DigitalWorkService(IFabricService fabricService,
	                          IDigitalWorkRepository digitalWorkRepository) {
		this.fabricService = fabricService;
		this.digitalWorkRepository = digitalWorkRepository;
	}

	@Override
	public List<DigitalWork> 목록조회()
	{
		return this.digitalWorkRepository.목록조회();
	}

	@Override
	public List<DigitalWork> 사용자작품목록조회(final long id)
	{
		return this.digitalWorkRepository.사용자작품목록조회(id);
	}

	@Override
	public DigitalWork 조회(final long id)
	{
		return this.digitalWorkRepository.조회(id);
	}

	/**
	 * 작품 등록 시 작품 정보를 저장하고
	 * 패브릭에 작품 소유권을 등록한다.
	 * @param 작품
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork 작품등록(final DigitalWork 작품) {
		// TODO
		long l = this.digitalWorkRepository.추가(작품);
		if (l != 0 ) {
			this.fabricService.소유권등록(작품.getArt_mem(), l);
//			this.fabricService.소유권이전(1, 4, 198);
		}
		return 작품;
	}

	/**
	 * 작품 삭제 시, 작품의 상태를 업데이트하고
	 * 패브릭에 작품 소유권 소멸 이력을 추가한다.
	 * @param id 작품id
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork 작품삭제(final long id)
	{
		DigitalWork digitalWork = this.digitalWorkRepository.조회(id);
		long own = digitalWork.getArt_mem();
		long l = this.digitalWorkRepository.삭제(id);
		if (l != 0) {
			this.fabricService.소유권소멸(own, id);
			this.digitalWorkRepository.조회(id);
		}
		return digitalWork;
	}

	@Override
	public DigitalWork 작품정보수정(final DigitalWork 작품) {
		DigitalWork workStored = this.digitalWorkRepository.조회(작품.getArt_id());
		
		if (workStored == null)
			throw new ApplicationException("해당 작품을 찾을 수 없습니다.");

		if (작품.getArt_mem() != 0 && 작품.getArt_mem() != workStored.getArt_mem())
			throw new ApplicationException("잘못된 접근입니다.");

		if(작품.getArt_name() == null || "".equals(작품.getArt_name()))
			작품.setArt_name(workStored.getArt_name());
		if(작품.getArt_detail() == null || "".equals(작품.getArt_detail()))
			작품.setArt_detail(workStored.getArt_detail());
		if(작품.getArt_isopen() == null || "".equals(작품.getArt_isopen()))
			작품.setArt_isopen(workStored.getArt_isopen());
		if(작품.getArt_status() == null || "".equals(작품.getArt_status()))
			작품.setArt_status(workStored.getArt_status());
		if(작품.getArt_mem() == 0)
			작품.setArt_mem(workStored.getArt_mem());

		int affected = this.digitalWorkRepository.수정(작품);
		if(affected == 0)
			throw new ApplicationException("작품정보수정 처리가 반영되지 않았습니다.");

		return 작품;
	}


}
