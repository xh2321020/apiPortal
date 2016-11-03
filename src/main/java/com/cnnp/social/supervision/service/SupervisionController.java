package com.cnnp.social.supervision.service;

import com.cnnp.social.base.BaseSetting;
import com.cnnp.social.base.SocialResponse;
import com.cnnp.social.supervision.manager.SupervisionManager;
import com.cnnp.social.supervision.manager.dto.SupervisionDto;
import com.cnnp.social.supervision.manager.dto.SupervisionSearch;
import com.cnnp.social.supervision.manager.dto.SupervisionTraceDto;
import com.cnnp.social.supervision.manager.dto.SupervisionUpdateStatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
@Api(value="Supervision",description="督办管理")
@RestController
@RequestMapping("/api/v1.0/supervision")
public class SupervisionController {
	@Autowired
	private SupervisionManager supervisionManger;
	@Autowired
	private BaseSetting setting;
	
	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public @ResponseBody SupervisionDto view(@PathVariable("id") long id) {
		//SupervisionDto supervisionDto=supervisionManger.findOne(id)
		return supervisionManger.findAllStatusOne(id);
	}
	@RequestMapping(value = "/completedRateStatistics/{uid}", method = RequestMethod.GET)
	public @ResponseBody int[] statisticsByYear(@PathVariable("uid") String uid){
		return supervisionManger.statisticsByYear(uid);
	}
	
	@RequestMapping(value = "/findchildren/{pid}", method = RequestMethod.GET)
	public @ResponseBody List<SupervisionDto> findChildren(@PathVariable("pid") long pid) {
		return supervisionManger.findChildren(pid);
	}
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public long save(@RequestBody SupervisionDto supervision) {
		return supervisionManger.save(supervision);

	}
	@RequestMapping(value = "/findtrace/{supervisionid}", method = RequestMethod.GET)
	public List<SupervisionTraceDto> findTrace(@PathVariable("supervisionid") long supervisionid){
		return supervisionManger.findTraces(supervisionid);
	}
	
	@RequestMapping(value = "/trace/{id}", method = RequestMethod.POST)
	public @ResponseBody SocialResponse trace(@RequestBody SupervisionTraceDto supervisionTraceDto) {
		return supervisionManger.trace(supervisionTraceDto);
	}
	
	@RequestMapping(value = "/postpone/{id}", method = RequestMethod.POST)
	public @ResponseBody SocialResponse postpone(@PathVariable("id") long id, @RequestParam String newDateStr,
			@RequestBody SupervisionUpdateStatusDto supervisionUpdateStatus) {
		Date newDate=null;
		try {
			newDate = DateUtils.parseDate(newDateStr, setting.getDateFormatter());
		} catch (ParseException e) {
			SocialResponse response=new SocialResponse();
			response.setMessagecode(500);
			response.setMessage(new String[]{e.getMessage()});
			return response;
		}
		return supervisionManger.postpone(id, newDate, supervisionUpdateStatus);

	}
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody SocialResponse delete(@PathVariable("id") long id,
			@RequestBody SupervisionUpdateStatusDto supervisionUpdateStatus) {
		return supervisionManger.delete(id,supervisionUpdateStatus);

	}
	@RequestMapping(value = "/close/{id}", method = RequestMethod.POST)
	public @ResponseBody SocialResponse close(@PathVariable("id") long id,
			@RequestBody SupervisionUpdateStatusDto supervisionUpdateStatus) {
		return supervisionManger.close(id,supervisionUpdateStatus);

	}
	@ApiOperation(value = "分页查询督办列表", notes = "调用者传入当前页和每页显示数目返回督办列表")
	
	@ApiImplicitParams({
			@ApiImplicitParam(name = "search", value = "查询条件", required = true, dataType = "SupervisionSearch"),
			@ApiImplicitParam(name = "page", value = "当前页码", required = true,paramType="query",dataType = "long"),
			@ApiImplicitParam(name = "size", value = "每页显示数目", required = true,paramType="query",dataType = "long") })
	@RequestMapping(value = "/search", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody List<SupervisionDto> search(@RequestBody SupervisionSearch search, @RequestParam int page,
			@RequestParam int size) {
		return supervisionManger.search(search, page, size);
	}
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public long getTotalElements(@RequestBody SupervisionSearch search) {
		return supervisionManger.getTotalElements(search);
	}
}
