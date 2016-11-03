package com.cnnp.social.supervision.manager;

import com.cnnp.social.base.BaseSetting;
import com.cnnp.social.base.SocialResponse;
import com.cnnp.social.cache.repository.dao.DBCacheDataDao;
import com.cnnp.social.cache.repository.entity.TDicData;
import com.cnnp.social.supervision.manager.dto.SupervisionDto;
import com.cnnp.social.supervision.manager.dto.SupervisionSearch;
import com.cnnp.social.supervision.manager.dto.SupervisionTraceDto;
import com.cnnp.social.supervision.manager.dto.SupervisionUpdateStatusDto;
import com.cnnp.social.supervision.repository.dao.SupervisionDao;
import com.cnnp.social.supervision.repository.dao.SupervisionTraceDao;
import com.cnnp.social.supervision.repository.dao.SupervisionUpdateStatusDao;
import com.cnnp.social.supervision.repository.entity.TSupervision;
import com.cnnp.social.supervision.repository.entity.TSupervisionTrace;
import com.cnnp.social.supervision.repository.entity.TSupervisionUpdatestatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableTransactionManagement
@Component

public class SupervisionManager {
	@Autowired
	private SupervisionDao supervisionDao;
	@Autowired
	private SupervisionUpdateStatusDao supervisionUpdateStatusDao;
	@Autowired
	private SupervisionTraceDao supervisionTraceDao;
	@Autowired
	private DBCacheDataDao dbCacheDataDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BaseSetting setting;
	
	private DozerBeanMapper mapper = new DozerBeanMapper();

	@Transactional
	public long save(SupervisionDto supervision) {
		if (supervision == null) {
			return -1;
		}
		String code_sq="SELECT SQ_SUPERVISION_CODE.NEXTVAL FROM DUAL";
		// 保存督办主表
		TSupervision supervisionEntry = new TSupervision();
		mapper.map(supervision, supervisionEntry);

		if(StringUtils.isBlank(supervisionEntry.getCode())){
			int seq=jdbcTemplate.queryForObject(code_sq, Integer.class);
			supervisionEntry.setCode(new SimpleDateFormat("yyyy").format(new Date())+"-"+supervisionEntry.getArea()+"-"+
			StringUtils.leftPad(""+seq,6,'0'));
		}
		supervisionEntry.setUpdatetime(new Date());
		supervisionDao.save(supervisionEntry);
		return supervisionEntry.getId();

	}
	
	public SocialResponse trace(SupervisionTraceDto traceDto){
		TSupervisionTrace trace=null;
		if (traceDto != null) {
			TSupervisionTrace supervisionTraceEntry = new TSupervisionTrace();
			mapper.map(traceDto, supervisionTraceEntry);
			supervisionTraceEntry.setUpdatetime(new Date());
			trace=supervisionTraceDao.save(supervisionTraceEntry);
		}
		SocialResponse response=new SocialResponse();
		if(trace!=null){
			response.setMessagecode(200);
			response.setMessage(new String[]{""+trace.getId()});
		}else{
			response.setMessagecode(500);
			response.setMessage(new String[]{"update the supervision trace error."});
		}
		return response;
		
	}
	
	@Transactional
	public SocialResponse postpone(long supervisionid,Date newDate,SupervisionUpdateStatusDto statusDto){
		TSupervision supervisionEntry = supervisionDao.findOne(supervisionid);
		SocialResponse response=new SocialResponse();
		if(supervisionEntry==null){
			response.setMessagecode(500);
			response.setMessage(new String[]{"no record the supervision<"+supervisionid+"> return."});
			return response;
		}
		supervisionEntry.setEstimatedcompletetiontime(newDate);
		if (statusDto != null) {
			TSupervisionUpdatestatus supervisionUpdatestatusEntry = new TSupervisionUpdatestatus();
			mapper.map(statusDto, supervisionUpdatestatusEntry);
			
			supervisionUpdatestatusEntry.setSupervisionId(supervisionid);
			supervisionUpdatestatusEntry.setOperatetype(2);//延期
			supervisionUpdateStatusDao.save(supervisionUpdatestatusEntry);
		}
		supervisionEntry.setUpdatetime(new Date());
		TSupervision supervision=supervisionDao.save(supervisionEntry);
		
		if(supervision!=null){
			response.setMessagecode(200);
			response.setMessage(new String[]{""+supervision.getId()});
		}else{
			response.setMessagecode(500);
			response.setMessage(new String[]{"postpone the supervision<"+supervisionid+"> error."});
		}
		return response;
	}
	@Transactional
	public SocialResponse delete(long supervisionid,SupervisionUpdateStatusDto statusDto){
		SocialResponse response=new SocialResponse();
		TSupervision supervisionEntry = supervisionDao.findOne(supervisionid);
		if(supervisionEntry==null){
			response.setMessagecode(500);
			response.setMessage(new String[]{"no record the supervision<"+supervisionid+"> return."});
			return response;
		}
		supervisionEntry.setStatus(2);//撤销
		
		if (statusDto != null) {
			TSupervisionUpdatestatus supervisionUpdatestatusEntry = new TSupervisionUpdatestatus();
			mapper.map(statusDto, supervisionUpdatestatusEntry);
			supervisionUpdatestatusEntry.setOperatetype(3);//关闭
			supervisionUpdatestatusEntry.setSupervisionId(supervisionid);
			supervisionUpdateStatusDao.save(supervisionUpdatestatusEntry);
		}
		supervisionEntry.setUpdatetime(new Date());
		TSupervision supervision=supervisionDao.save(supervisionEntry);

		List<TSupervision> children=supervisionDao.findChildren(supervision.getId());
        for(TSupervision child : children){
			child.setStatus(2);////撤销
			supervisionDao.save(child);
		}

		if(supervision!=null){
			response.setMessagecode(200);
			response.setMessage(new String[]{""+supervision.getId()});
		}else{
			response.setMessagecode(500);
			response.setMessage(new String[]{"delete the supervision<" + supervisionid + "> error."});
		}
		return response;
	}
	@Transactional
	public SocialResponse close(long supervisionid,SupervisionUpdateStatusDto statusDto){
		SocialResponse response=new SocialResponse();
		TSupervision supervisionEntry = supervisionDao.findOne(supervisionid);
		if(supervisionEntry==null){
			response.setMessagecode(500);
			response.setMessage(new String[]{"no record the supervision<"+supervisionid+"> return."});
			return response;
		}
		supervisionEntry.setStatus(1);//任务项关闭
		
		if (statusDto != null) {
			TSupervisionUpdatestatus supervisionUpdatestatusEntry = new TSupervisionUpdatestatus();
			mapper.map(statusDto, supervisionUpdatestatusEntry);
			supervisionUpdatestatusEntry.setOperatetype(4);//关闭
			supervisionUpdatestatusEntry.setSupervisionId(supervisionid);
			supervisionUpdateStatusDao.save(supervisionUpdatestatusEntry);
			//supervisionEntry.getUpdateStatus().add(supervisionUpdatestatusEntry);
		}
		supervisionEntry.setUpdatetime(new Date());
		TSupervision supervision=supervisionDao.save(supervisionEntry);
		
		if(supervision!=null){
			response.setMessagecode(200);
			response.setMessage(new String[]{""+supervision.getId()});
		}else{
			response.setMessagecode(500);
			response.setMessage(new String[]{"close the supervision<" + supervisionid + "> error."});
		}
		return response;
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */

	public SupervisionDto findOne(long id) {
		TSupervision supervisionEntry = supervisionDao.findOne(id);
		if (supervisionEntry == null) {
			return null;
		}
		return convertSupervisionEntrytoDto(supervisionEntry);

	}
	
	public int[] statisticsByYear(String uid){
		Object[] objs=new Object[]{uid,uid};
		
		int uncompleted=jdbcTemplate.queryForObject("select count(*) as total from t_supervision t where "
				+ "(t.accountablesn=? or t.responsiblesn=?) and to_char(t.estimatedcompletetiontime,'yyyy')=to_char(sysdate,'yyyy') and t.status='0'", Integer.class,objs);
		int completed=jdbcTemplate.queryForObject("select count(*) as total from t_supervision t where "
				+ "(t.accountablesn=? or t.responsiblesn=?) and to_char(t.estimatedcompletetiontime,'yyyy')=to_char(sysdate,'yyyy') and t.status='1'",Integer.class,objs);
		return new int[]{uncompleted,completed};
	}
	
	public SupervisionDto findAllStatusOne(long id){
		TSupervision supervisionEntry = supervisionDao.findAllStatusOne(id);
		if (supervisionEntry == null) {
			return new SupervisionDto();
		}
		return convertSupervisionEntrytoDto(supervisionEntry);
	}

	private SupervisionDto convertSupervisionEntrytoDto(TSupervision supervisionEntry) {
		SupervisionDto supervisionDto = new SupervisionDto();
		mapper.map(supervisionEntry, supervisionDto);
		
		TDicData dic=dbCacheDataDao.findByCode(supervisionDto.getSource());
		supervisionDto.setSource_name(dic.getDicname());
		
		dic=dbCacheDataDao.findByCode(supervisionDto.getArea());
		supervisionDto.setArea_name(dic.getDicname());
		
		List<TSupervisionTrace> traces = supervisionTraceDao.find(supervisionEntry.getId());
		if (traces == null || traces.size() < 1) {
			return supervisionDto;
		}
		TSupervisionTrace trace = traces.get(0);
		SupervisionTraceDto supervisionTraceDto = new SupervisionTraceDto();
		mapper.map(trace, supervisionTraceDto);
		supervisionDto.setLatestTrace(supervisionTraceDto);
		return supervisionDto;
	}
	public List<SupervisionTraceDto> findTraces(long supervisionid){
		List<TSupervisionTrace> traces=supervisionTraceDao.find(supervisionid);
		List<SupervisionTraceDto> traceDtos=new ArrayList<SupervisionTraceDto>();
		for(TSupervisionTrace trace : traces){
			SupervisionTraceDto supervisionTraceDto = new SupervisionTraceDto();
			mapper.map(trace, supervisionTraceDto);
			traceDtos.add(supervisionTraceDto);
		}
		return traceDtos;
	}
	
	public List<SupervisionDto> findChildren(long pid) {
		List<TSupervision> supervisionEntries=supervisionDao.findChildren(pid);
		if(supervisionEntries==null){
			return new ArrayList<SupervisionDto>();
		}
		List<SupervisionDto> rsList = new ArrayList<SupervisionDto>();
		for(TSupervision supervisionEntry : supervisionEntries ){
			rsList.add(convertSupervisionEntrytoDto(supervisionEntry));
		}
		return rsList;
	}

	public List<SupervisionDto> search(final SupervisionSearch search, int page, int size) {
		Sort sort = new Sort(Direction.DESC, "estimatedcompletetiontime");
		Pageable pageable = new PageRequest(page, size, sort);
		Page<TSupervision> pageSet = supervisionDao.findAll(new Specification<TSupervision>() {
			@Override
			public Predicate toPredicate(Root<TSupervision> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> areaPath = root.get("area");
				Path<Date> estimateDate = root.get("estimatedcompletetiontime");
				Path<String> accountableSN = root.get("accountablesn");
				Path<String> responsibleSN = root.get("responsiblesn");
				Path<String> source = root.get("source");
				Path<String> scopePath = root.get("scope");
				Path<Integer> statusPath = root.get("status");
				List<Predicate> predicateList = new ArrayList<Predicate>();

				if (StringUtils.isNoneBlank(search.getAreaCode())) {
					String[] areacodes=search.getAreaCode().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String areacode : areacodes){
						orPredicateList.add(cb.like(areaPath, areacode));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));

				}
				predicateList.add(cb.notEqual(statusPath,2)); //过滤撤销的督办
				if (StringUtils.isNoneBlank(search.getSearchBeginDate())
						&& StringUtils.isNotBlank(search.getSearchEndDate())) {
					try {
						predicateList.add(
								cb.between(estimateDate, DateUtils.parseDate(search.getSearchBeginDate(), "yyyy-MM-dd"),
										DateUtils.parseDate(search.getSearchEndDate(), "yyyy-MM-dd")));
					} catch (Exception err) {

					}
				}
				if (StringUtils.isNoneBlank(search.getAccountableSN())&& !StringUtils.isNoneBlank(search.getResponsibleSN())) {
					predicateList.add(cb.equal(accountableSN, search.getAccountableSN()));
				}
				if (StringUtils.isNoneBlank(search.getResponsibleSN())&& !StringUtils.isNoneBlank(search.getAccountableSN())) {
					predicateList.add(cb.equal(responsibleSN, search.getResponsibleSN()));
				}
				if (StringUtils.isNoneBlank(search.getResponsibleSN())&& StringUtils.isNoneBlank(search.getAccountableSN())) {
					predicateList.add(cb.or(cb.equal(responsibleSN, search.getResponsibleSN()),
							cb.equal(accountableSN, search.getAccountableSN())));
				}
				if (StringUtils.isNoneBlank(search.getSource())) {
					String[] sourcecodes=search.getSource().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String sourcecode : sourcecodes){
						orPredicateList.add(cb.equal(source, sourcecode));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));
				}
				if (StringUtils.isNoneBlank(search.getScope())) {
					String[] scopes=search.getScope().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String scope : scopes){
						orPredicateList.add(cb.equal(scopePath, scope));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));
				}
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
		}, pageable);
		// pageSet.
		final List<SupervisionDto> rsList = new ArrayList<SupervisionDto>();
		List<TSupervision> pageSetList = pageSet.getContent();
		for (TSupervision supervison : pageSetList) {
			SupervisionDto dto = new SupervisionDto();
			mapper.map(supervison, dto);
			List<TSupervisionTrace> traces=supervisionTraceDao.find(supervison.getId());
			if (traces.size() > 0) {
				TSupervisionTrace trace = traces.get(0);
				SupervisionTraceDto traceDto = new SupervisionTraceDto();
				mapper.map(trace, traceDto);
				dto.setLatestTrace(traceDto);
			}
			rsList.add(dto);
		}
		return rsList;
	}
	public long getTotalElements(final SupervisionSearch search) {
		 return supervisionDao.count(new Specification<TSupervision>() {
			@Override
			public Predicate toPredicate(Root<TSupervision> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> areaPath = root.get("area");
				Path<Date> estimateDate = root.get("estimatedcompletetiontime");
				Path<String> accountableSN = root.get("accountablesn");
				Path<String> responsibleSN = root.get("responsiblesn");
				Path<String> source = root.get("source");
				Path<String> scopePath = root.get("scope");
				List<Predicate> predicateList = new ArrayList<Predicate>();

				if (StringUtils.isNoneBlank(search.getAreaCode())) {
					String[] areacodes=search.getAreaCode().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String areacode : areacodes){
						orPredicateList.add(cb.like(areaPath, areacode));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));

				}
				if (StringUtils.isNoneBlank(search.getSearchBeginDate())
						&& StringUtils.isNotBlank(search.getSearchEndDate())) {
					try {
						predicateList.add(
								cb.between(estimateDate, DateUtils.parseDate(search.getSearchBeginDate(), "yyyy-MM-dd"),
										DateUtils.parseDate(search.getSearchEndDate(), "yyyy-MM-dd")));
					} catch (Exception err) {

					}
				}
				if (StringUtils.isNoneBlank(search.getAccountableSN())&& !StringUtils.isNoneBlank(search.getResponsibleSN())) {
					predicateList.add(cb.equal(accountableSN, search.getAccountableSN()));
				}
				if (StringUtils.isNoneBlank(search.getResponsibleSN())&& !StringUtils.isNoneBlank(search.getAccountableSN())) {
					predicateList.add(cb.equal(responsibleSN, search.getResponsibleSN()));
				}
				if (StringUtils.isNoneBlank(search.getResponsibleSN())&& StringUtils.isNoneBlank(search.getAccountableSN())) {
					predicateList.add(cb.or(cb.equal(responsibleSN, search.getResponsibleSN()),
							cb.equal(accountableSN, search.getAccountableSN())));
				}
				if (StringUtils.isNoneBlank(search.getSource())) {
					String[] sourcecodes=search.getSource().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String sourcecode : sourcecodes){
						orPredicateList.add(cb.equal(source, sourcecode));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));
				}
				if (StringUtils.isNoneBlank(search.getScope())) {
					String[] scopes=search.getScope().split(setting.getSplitchar());
					List<Predicate> orPredicateList = new ArrayList<Predicate>();
					for(String scope : scopes){
						orPredicateList.add(cb.equal(scopePath, scope));
					}
					Predicate[] orPredicates = new Predicate[orPredicateList.size()];
					orPredicateList.toArray(orPredicates);
					predicateList.add(cb.or(orPredicates));
				}
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
		});

	}
}
