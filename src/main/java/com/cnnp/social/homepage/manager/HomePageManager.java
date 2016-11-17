package com.cnnp.social.homepage.manager;

import com.cnnp.social.homepage.exception.NoAuthenticationException;
import com.cnnp.social.homepage.manager.dto.*;
import com.cnnp.social.homepage.repository.dao.*;
import com.cnnp.social.homepage.repository.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@EnableTransactionManagement
@Component

public class HomePageManager {
	@Autowired
	private HomePageInfoDao homepageInfoDao;
	@Autowired
	private HomePageArticlecatDao homepageArticlecatDao;
	@Autowired
	private HomePageFormDao homepageFormDao;
	@Autowired
	private HomePageFormInDao homepageForminDao;
	@Autowired
	private HomePageStyleDao homepageStyleDao;
	@Autowired
	private HomePageStyleOrderDao homepagestyleorderDao;

	private DozerBeanMapper mapper = new DozerBeanMapper();

	@Transactional
	public void saveHomePage(HomePageInfoDto homepage) {
		if (homepage == null) {
			return;
		}
		long hpid;
		if (homepageArticlecatDao.findid()==0){
			hpid=1;
		}else{
			hpid = homepageArticlecatDao.findmaxid()+1;
		}
		//long hpid = homepageInfoDao.findmaxid()+1;
		//long id = homepageAdminDao.findmaxid()+1;
		Date now = new Date();
		homepage.setid(hpid);
		homepage.setUpdatetime(now);
		// set the default value of priority as 0
		if("".equals(""+homepage.getPriority())){
			homepage.setPriority(0);
		}
		THomePageInfo hpEntry = new THomePageInfo();
		mapper.map(homepage, hpEntry);

		THomePageArticlecat articlecatEntry = new THomePageArticlecat();
		articlecatEntry.setid(hpid);
		articlecatEntry.setIs_build("1");
		articlecatEntry.setName(hpEntry.getName());
		articlecatEntry.setParent_id(1);
		articlecatEntry.setPre_view_name(hpEntry.getName());
		articlecatEntry.setSeq("99");
		articlecatEntry.setStatus("2");
		articlecatEntry.setVersion("1");
		articlecatEntry.setAuditor_ids(hpEntry.getCreateuserid());
		articlecatEntry.setAuditor_names(hpEntry.getCreateusername());
		homepageInfoDao.save(hpEntry);
		homepageArticlecatDao.save(articlecatEntry);
		return;
	}
	public void editHomePage(HomePageInfoDto homepage) {
		if (homepage == null) {
			return;
		}

		Date now = new Date();
		homepage.setUpdatetime(now);
		THomePageInfo hpEntry = new THomePageInfo();
		mapper.map(homepage, hpEntry);
		homepageInfoDao.save(hpEntry);
		return;
	}

	public void editHomePage(long hpid,String type) {
		THomePageInfo homepageaEntry =  homepageInfoDao.findOne(hpid);
		if(homepageaEntry==null){
			return;
		}

		if(type.equals("del")){
			homepageInfoDao.delete(homepageaEntry);
			return;
		}
		if(type.equals("start")){
			//if (homepageaEntry.getParentid()==0){
			//	homepageInfoDao.updataStatus();
			//}
			homepageaEntry.setStatus("1");
		}
		if(type.equals("stop")){
			homepageaEntry.setStatus("0");
		}
		Date now = new Date();
		homepageaEntry.setUpdatetime(now);
		homepageInfoDao.save(homepageaEntry);
		return;
	}


	/**
	 * 查询所有启用的门户
	 * @return
	 */
	public List<HomePageInfoDto> findActivePortals(){
		List<THomePageInfo> activePortals=homepageInfoDao.loadAllActivePortal();
		if(CollectionUtils.isEmpty(activePortals)){
			return null;
		}

		List<HomePageInfoDto> portals=new ArrayList<HomePageInfoDto>();
		for(THomePageInfo activePortal : activePortals){
			HomePageInfoDto portal = new HomePageInfoDto();
			mapper.map(activePortal, portal);
			portals.add(portal);

		}
		return portals;
	}




	public List<HomePageFormDto> findForm(long hpid){
		List<THomePageForm> homepagefromEntries =  homepageFormDao.find(hpid);
		if(homepagefromEntries==null){
			return new ArrayList<HomePageFormDto>();
		}
		List<HomePageFormDto> homepagefromDtos=new ArrayList<HomePageFormDto>();

		for(THomePageForm Form : homepagefromEntries){
			HomePageFormDto dto=new HomePageFormDto();
			mapper.map(Form, dto);
			List<THomePageFormIn> homepagefrominEntries =  homepageForminDao.find(dto.getid());
			dto.setFormin(homepagefrominEntries);
			homepagefromDtos.add(dto);
		}
		return homepagefromDtos;
	}

	public void saveForm(HomePageFormDto form) {
		if (form == null) {
			return;
		}
		long formid;
		if (homepageFormDao.findid()==0){
			formid=1;
		}else{
			formid = homepageFormDao.findmaxid()+1;
		}
		long forminid;
		if (homepageForminDao.findid()==0){
			forminid=1;
		}else{
			forminid = homepageForminDao.findmaxid()+1;
		}
		//long formid = homepageFormDao.findmaxid()+1;
		//long forminid = homepageForminDao.findmaxid()+1;
		Date now = new Date();
		form.setid(formid);
		form.setUpdatetime(now);
		THomePageForm formEntry = new THomePageForm();
		mapper.map(form, formEntry);
		List<THomePageFormIn> homepageforminEntries = new ArrayList<THomePageFormIn>();

		for(THomePageFormIn formin : formEntry.getFormin()){
			formin.setFormid(formid);
			formin.setid(forminid);
			formin.setUpdatetime(now);
			homepageforminEntries.add(formin);
			forminid = forminid+1;

		}
		formEntry.setFormin(homepageforminEntries);
		homepageFormDao.save(formEntry);
		return;
	}

	public void editForm(HomePageFormDto form) {
		if (form == null) {
			return;
		}
		long forminid;
		if (homepageForminDao.findid()==0){
			forminid=1;
		}else{
			forminid = homepageForminDao.findmaxid()+1;
		}
		//long forminid = homepageForminDao.findmaxid()+1;
		Date now = new Date();
		form.setUpdatetime(now);
		THomePageForm formEntry = new THomePageForm();
		mapper.map(form, formEntry);
		List<THomePageFormIn> forminEntries =homepageForminDao.find(form.getid());
		if (forminEntries != null) {
			homepageForminDao.delete(forminEntries);
		}
		List<THomePageFormIn> homepageforminEntries = new ArrayList<THomePageFormIn>();

		for(THomePageFormIn formin : formEntry.getFormin()){
			formin.setid(forminid);
			formin.setUpdatetime(now);
			formin.setFormid(form.getid());
			homepageforminEntries.add(formin);
			forminid = forminid+1;
		}
		formEntry.setFormin(homepageforminEntries);
		homepageFormDao.save(formEntry);
		return;
	}
	public void delForm(long formid) {

		THomePageForm formEntry = homepageFormDao.findOne(formid);

		if (formEntry != null) {
			homepageFormDao.delete(formEntry);
		}

		return;
	}
	public List<HomePageStyleDto> findStyles(long hpid){
		List<THomePageStyle> homepagestyleEntries =  homepageStyleDao.find(hpid);
		if(homepagestyleEntries==null){
			return new ArrayList<HomePageStyleDto>();
		}
		List<HomePageStyleDto> homepagestyleDtos=new ArrayList<HomePageStyleDto>();

		for(THomePageStyle Style : homepagestyleEntries){
			HomePageStyleDto dto=new HomePageStyleDto();
			mapper.map(Style, dto);
			homepagestyleDtos.add(dto);
		}
		return homepagestyleDtos;
	}
	public List<HomePageStyleDto> findStyle(long hpid){
		List<THomePageStyle> homepagestyleEntries =  homepageStyleDao.find(hpid);
		if(homepagestyleEntries==null){
			return new ArrayList<HomePageStyleDto>();
		}
		List<HomePageStyleDto> homepagestyleDtos=new ArrayList<HomePageStyleDto>();

		for(THomePageStyle Style : homepagestyleEntries){
			List<THomePageStyleOrder> homepagestyleorderEntries =  homepagestyleorderDao.find(Style.getid());
			Style.setOrder(homepagestyleorderEntries);
			HomePageStyleDto dto=new HomePageStyleDto();
			mapper.map(Style, dto);
			homepagestyleDtos.add(dto);
		}
		return homepagestyleDtos;
	}

	public void saveStyle(HomePageStyleDto style) {
		if (style == null) {
			return;
		}
		long styleid;
		if (homepageStyleDao.findid()==0){
			styleid=1;
		}else{
			styleid = homepageStyleDao.findmaxid()+1;
		}

		long orderid;
		if (homepagestyleorderDao.findid()==0){
			orderid=1;
		}else{
			orderid = homepagestyleorderDao.findmaxid()+1;
		}
		Date now = new Date();
		style.setid(styleid);
		style.setUpdatetime(now);
		THomePageStyle styleEntry = new THomePageStyle();
		mapper.map(style, styleEntry);
		List<THomePageStyleOrder> homepagestyleorderEntries = new ArrayList<THomePageStyleOrder>();
		for(THomePageStyleOrder order : styleEntry.getOrder()){
			order.setid(orderid);
			order.setStyleid(styleid);
			homepagestyleorderEntries.add(order);
			orderid = orderid+1;
		}

		homepageStyleDao.save(styleEntry);
		return;
	}
	public void editHomePageStyle(HomePageStyleDto style) {
		if (style == null) {
			return;
		}

		long orderid;
		if (homepagestyleorderDao.findid()==0){
			orderid=1;
		}else{
			orderid = homepagestyleorderDao.findmaxid()+1;
		}
		//long imgid = homepageimgDao.findmaxid()+1;
		//long orderid = homepagestyleorderDao.findmaxid()+1;
		Date now = new Date();
		style.setUpdatetime(now);
		THomePageStyle styleEntry = new THomePageStyle();
		mapper.map(style, styleEntry);

		List<THomePageStyleOrder> orderEntries = homepagestyleorderDao.find(style.getid());
		if (orderEntries!=null){
			homepagestyleorderDao.delete(orderEntries);
		}
		List<THomePageStyleOrder> homepagestyleorderEntries = new ArrayList<THomePageStyleOrder>();
		for(THomePageStyleOrder order : styleEntry.getOrder()){
			order.setid(orderid);
			order.setStyleid(style.getid());
			homepagestyleorderEntries.add(order);
			orderid = orderid+1;
		}
		styleEntry.setOrder(homepagestyleorderEntries);
		homepageStyleDao.save(styleEntry);
		return;
	}

	public void enableStyle(long styleid){
		THomePageStyle style=homepageStyleDao.findOne(styleid);
		List<THomePageStyle> styleEntries =  homepageStyleDao.find(style.getHpid());
		for(THomePageStyle  styleEntry : styleEntries){
			if(styleEntry.getid()==styleid){
				styleEntry.setStatus("1");
				homepageStyleDao.save(styleEntry);
			}else{
				styleEntry.setStatus("0");
				homepageStyleDao.save(styleEntry);
			}
		}
	}

	public void editHomePageStyle(long styleid,String type){
		THomePageStyle styleEntry =  homepageStyleDao.findOne(styleid);
		if(styleEntry==null){
			throw new NoAuthenticationException(110,""+styleid);
		}

		if(type.equals("del")){
			homepageStyleDao.delete(styleEntry);
			return;
		}
		if(type.equals("start")){
			styleEntry.setStatus("0");
		}
		if(type.equals("stop")){
			styleEntry.setStatus("1");
		}
		Date now = new Date();
		styleEntry.setUpdatetime(now);
		homepageStyleDao.save(styleEntry);
		return;
	}
	public List<HomePageIDNameDto> findHomePageSector(){
		List<HomePageIDNameDto> homepageaidnameDtos=new ArrayList<HomePageIDNameDto>();

		//1.group by priority get priority list; 2.get list by priority order by updatetime; 3.add all list
		List<Long> priorityList = homepageInfoDao.findpriority();
		if(CollectionUtils.isEmpty(priorityList)){
			return null;
		}

		List<THomePageInfo> homepageEntry = new ArrayList<THomePageInfo>();

		for(int i=0;i<priorityList.size();i++){
			List<THomePageInfo> templist = new ArrayList<THomePageInfo>();
			templist = homepageInfoDao.findparentid(priorityList.get(i));
			homepageEntry.addAll(templist);
		}


		if(CollectionUtils.isEmpty(homepageEntry)){
			return null;
		}


		for(THomePageInfo hp : homepageEntry){
			HomePageIDNameDto iddto=new HomePageIDNameDto();
			iddto.setid(hp.getid());
			iddto.setName(hp.getName());
			iddto.setHptype(hp.getHptype());
			iddto.setWebtype(hp.getWebtype());
			iddto.setUrl(hp.getUrl());
			homepageaidnameDtos.add(iddto);
		}
		return homepageaidnameDtos;
	}

	public List<HomePageIDNameDto> findHomePageCompanySector(){
		List<HomePageIDNameDto> homepageaidnameDtos=new ArrayList<HomePageIDNameDto>();

		//1.group by priority get priority list; 2.get list by priority order by updatetime; 3.add all list
		List<Long> priorityList = homepageInfoDao.findpriority();
		if(priorityList==null||priorityList.size()==0){
			return null;
		}

		List<THomePageInfo> homepageEntry = new ArrayList<THomePageInfo>();

		for(int i=0;i<priorityList.size();i++){
			List<THomePageInfo> templist = new ArrayList<THomePageInfo>();
			templist = homepageInfoDao.findcompany(priorityList.get(i));
			homepageEntry.addAll(templist);
		}


		if(homepageEntry==null||homepageEntry.size()==0){
			return null;
		}


		for(THomePageInfo hp : homepageEntry){
			HomePageIDNameDto iddto=new HomePageIDNameDto();
			iddto.setid(hp.getid());
			iddto.setName(hp.getName());
			iddto.setHptype(hp.getHptype());
			iddto.setWebtype(hp.getWebtype());
			iddto.setUrl(hp.getUrl());
			homepageaidnameDtos.add(iddto);
		}
		return homepageaidnameDtos;
	}

	public List<HomePageInfoAllDto> findHomePageForPreview(long styleid){
		THomePageStyle homepagestyleEntry =  homepageStyleDao.findOne(styleid);
		long hpid=homepagestyleEntry.getHpid();
		List<HomePageInfoAllDto> homepageallDtos=new ArrayList<HomePageInfoAllDto>();
		THomePageInfo homepageEntry = new THomePageInfo();
		if (hpid==0){
			homepageEntry = homepageInfoDao.findStartHP();
		}else{
			homepageEntry = homepageInfoDao.findOne(hpid);
		}

		if(homepageEntry==null){
			return new ArrayList<HomePageInfoAllDto>();
		}
		List<THomePageForm> homepagefromEntries =  homepageFormDao.find(homepageEntry.getid());
	//	THomePageStyle homepagestyleEntry =  homepageStyleDao.findOne(styleid);
		List<THomePageStyleOrder> orderEntries =  homepagestyleorderDao.find(homepagestyleEntry.getid());
		for(THomePageForm Form : homepagefromEntries){
			HomePageInfoAllDto hpDto=new HomePageInfoAllDto();
			List<THomePageFormIn> Formins = Form.getFormin();
			for(THomePageStyleOrder order : orderEntries){
				if((Form.getid()==order.getFormid())){

					hpDto.setCARD_TOP_COLOR(Form.getTop_color());
					hpDto.setCARD_WIDTH(Form.getWidth());
					hpDto.setCARD_INDEX(order.getOrderid());
					List<HomePageFormInAllDto> forminall = new ArrayList<HomePageFormInAllDto>();

					for(THomePageFormIn Formin : Formins){
						HomePageFormInAllDto forminone = new HomePageFormInAllDto();

						forminone.setCONTENT_TYPE(Formin.getContent_type());
						forminone.setMETHOD(Formin.getMethod());
						forminone.setPAYLOAD(Formin.getPayload());
						forminone.setQueryString(Formin.getQuerystring());
						forminone.setSUBCARD_INDEX(Formin.getForm_inid());
						forminone.setSUBCARD_ISMORE(Formin.getIsmore());
						forminone.setSUBCARD_MORE_URL(Formin.getMore_url());
						forminone.setSUBCARD_TYPE(Formin.getStyleid());
						forminone.setSUBCARD_ZH(Formin.getName());
						forminone.setURL(Formin.getUrl());
						forminall.add(forminone);
					}
					hpDto.setSUBCARDS(forminall);

					homepageallDtos.add(hpDto);

				}
			}

		}

		return homepageallDtos;

	}

	public List<HomePageInfoAllDto> findHomePageAll(long hpid){
		List<HomePageInfoAllDto> homepageallDtos=new ArrayList<HomePageInfoAllDto>();
		THomePageInfo homepageEntry = new THomePageInfo();
		if (hpid==0){
			homepageEntry = homepageInfoDao.findStartHP();
		}else{
			homepageEntry = homepageInfoDao.findOne(hpid);
		}

		if(homepageEntry==null){
			return new ArrayList<HomePageInfoAllDto>();
		}
		List<THomePageForm> homepagefromEntries =  homepageFormDao.find(homepageEntry.getid());
		THomePageStyle homepagestyleEntry =  homepageStyleDao.findstart(homepageEntry.getid());
		List<THomePageStyleOrder> orderEntries =  homepagestyleorderDao.find(homepagestyleEntry.getid());
		for(THomePageForm Form : homepagefromEntries){
			HomePageInfoAllDto hpDto=new HomePageInfoAllDto();
			List<THomePageFormIn> Formins = Form.getFormin();
			for(THomePageStyleOrder order : orderEntries){
				if((Form.getid()==order.getFormid())){

					hpDto.setCARD_TOP_COLOR(Form.getTop_color());
					hpDto.setCARD_WIDTH(Form.getWidth());
					hpDto.setCARD_INDEX(order.getOrderid());
					List<HomePageFormInAllDto> forminall = new ArrayList<HomePageFormInAllDto>();

					for(THomePageFormIn Formin : Formins){
						HomePageFormInAllDto forminone = new HomePageFormInAllDto();

						forminone.setCONTENT_TYPE(Formin.getContent_type());
						forminone.setMETHOD(Formin.getMethod());
						forminone.setPAYLOAD(Formin.getPayload());
						forminone.setQueryString(Formin.getQuerystring());
						forminone.setSUBCARD_INDEX(Formin.getForm_inid());
						forminone.setSUBCARD_ISMORE(Formin.getIsmore());
						forminone.setSUBCARD_MORE_URL(Formin.getMore_url());
						forminone.setSUBCARD_TYPE(Formin.getStyleid());
						forminone.setSUBCARD_ZH(Formin.getName());
						forminone.setURL(Formin.getUrl());
						forminall.add(forminone);
					}
					hpDto.setSUBCARDS(forminall);
					hpDto.setPortalName(homepageEntry.getName());
					homepageallDtos.add(hpDto);

				}
			}

		}

		return homepageallDtos;

	}
	public List<HomePageArticlecatDto> findArticlecat(long parent_id){
		List<THomePageArticlecat> articlecatEntries =  homepageArticlecatDao.find(parent_id);
		if(articlecatEntries==null){
			return new ArrayList<HomePageArticlecatDto>();
		}
		List<HomePageArticlecatDto> homepagecolumnDtos=new ArrayList<HomePageArticlecatDto>();

		for(THomePageArticlecat column : articlecatEntries){
			HomePageArticlecatDto dto=new HomePageArticlecatDto();
			mapper.map(column, dto);
			homepagecolumnDtos.add(dto);
		}
		return homepagecolumnDtos;
	}
	public long saveArticlecat(HomePageArticlecatDto articlecat) {
		if (articlecat == null) {
			return 0;
		}
		long id;
		if (homepageArticlecatDao.findid()==0){
			id=1;
		}else{
			id = homepageArticlecatDao.findmaxid()+1;
		}
		//long id = homepageArticlecatDao.findmaxid()+1;
		articlecat.setid(id);
		articlecat.setVersion("1");
		articlecat.setSeq("99");
		articlecat.setIs_build("1");
		articlecat.setPre_view_name(articlecat.getName());
		articlecat.setStatus("2");
		THomePageArticlecat articlecatEntry = new THomePageArticlecat();
		mapper.map(articlecat, articlecatEntry);
		homepageArticlecatDao.save(articlecatEntry);
		return id;
	}
	public void editArticlecat(HomePageArticlecatDto articlecat) {
		if (articlecat == null) {
			return;
		}
		THomePageArticlecat articlecatEntry = new THomePageArticlecat();
		mapper.map(articlecat, articlecatEntry);
		homepageArticlecatDao.save(articlecatEntry);
		return;
	}
	public void editArticlecat(long articlecatid,String type) {
		THomePageArticlecat articlecatEntry =  homepageArticlecatDao.findone(articlecatid);
		if(articlecatEntry==null){
			return;
		}

		if(type.equals("del")){
			homepageArticlecatDao.delete(articlecatEntry);
			return;
		}
		if(type.equals("start")){
			articlecatEntry.setStatus("2");
		}
		if(type.equals("stop")){
			articlecatEntry.setStatus("0");
		}

		homepageArticlecatDao.save(articlecatEntry);
		return;
	}

}
