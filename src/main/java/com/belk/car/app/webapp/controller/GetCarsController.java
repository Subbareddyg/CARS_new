package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.util.DateUtils;

public class GetCarsController extends BaseController {
	
	private static final String CAR_LISTING = "carListing";
	private static final String ENABLE_DELETE = "enableDelete";
	private static final String ENABLE_ARCHIVE = "enableArchive";
	private static final String USER_TYPE_CD = "userTypeCd";
	private static final String NO_OF_CARS_DISPLAYED = "totalResultSize";
	private static final String DASH_BOARD_DISPLAY_CARS = "DashBoard";
	private static final String SELECTED_PAGE_NUMBER = "pageno"; 
	private static final String DISPLAY_TABLE_PAGE_NUMBER = "d-1335586-p";
	private static final String DESCENDING_ORDER = "11";
	private static final String DISPLAY_TABLE_SORT_ORDER = "d-1335586-o";
	private static final String DASHBOARD_DISPLAY_TABLE_ID = "d-1335586-s";
	private static final String DASHBOARD_SORTED_ORDER = "dashboardSortedOrder";
	private static final String DASHBOARD_SORTED_ON = "dashboardSortedOn";
	private static final String CAR_IDS = "ids";
	private static final String ENABLE_BUTTON_Y = "Y";
	//Adding the parameter for Archive/ Unarchive button functionality
	private static final String ENABLE_ALL_BUTTONS = "enableBoth";
	private transient final Log log = LogFactory.getLog(GetCarsController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<CarsDTO> cars = null ;
		CarSearchCriteria searchCriteria = new CarSearchCriteria();
		boolean enableBothButtons = false; //archive and unarchive buttons
		String operation = request.getParameter("oper"); //To check if any operation(archive,unarchive,delete) is being performed.
		Integer pageNo = 1; 
		String dashboardSortedOn = DESCENDING_ORDER;
		String dashboardSortedOrder = "ascending";
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		long dataStartTime = System.currentTimeMillis();
		
			try {
			User user = getLoggedInUser();

			String filter = request.getParameter("filter");

			User currentUser = this.getLoggedInUser();

			searchCriteria.setIgnoreUser(false);
			searchCriteria.setStatusCd(Status.ACTIVE);
			searchCriteria.setUser(currentUser);
			searchCriteria.setIgnoreClosedCars(true);
			searchCriteria.setVendorStyleStatusCd(Status.ACTIVE);
			
			//By default unarchived CARS are being searched
			searchCriteria.setArchive(false);
			
			//By default unarchived CARS are displayed on dashboard. So only "Archive" & "Delete" button will be enabled.
			//Unarchive button will be disabled
			searchCriteria.setEnableArchive(ENABLE_BUTTON_Y);
			searchCriteria.setEnableDelete(ENABLE_BUTTON_Y);

			if (StringUtils.isNotEmpty(operation)) {
				carManager.updateCarStatus(operation, request
						.getParameter(CAR_IDS));
			}
			String flag = "false";
			if(null != request.getSession(true).getAttribute("fromSearchPage"))
			 flag = request.getSession(true).getAttribute("fromSearchPage").toString();
			
			if( flag.equals("true")){
				filter="last_search";
				request.getSession().removeAttribute("fromSearchPage");
			}
			if (StringUtils.isNotEmpty(filter)) {
				if ("assigned_to_me".equals(filter)) {
					String userTypeCd = currentUser.getUserType()
							.getUserTypeCd();
					/*
					 * if (userTypeCd.equals(UserType.ART_DIRECTOR)) {
					 * userTypeCd = UserType.SAMPLE_COORDINATOR ; } else
					 */
					if (userTypeCd.equals(UserType.CONTENT_WRITER)) {
						userTypeCd = UserType.CONTENT_MANAGER;
					}
					if(userTypeCd.equals(UserType.BUYER)){
						searchCriteria.setCurrentUserType(userTypeCd);
					}
					if(userTypeCd.equals(UserType.VENDOR_PROVIDED_IMAGE)){
					    searchCriteria.setCurrentUserType(userTypeCd);
					}
				} else if ("over_due".equals(filter)) {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, -1);
					Date toDate = c.getTime();
					searchCriteria.setDueDateTo(DateUtils.formatDate(toDate));
				} else if ("copy_incomplete".equals(filter)) {
					searchCriteria.setContentStatus(ContentStatus.IN_PROGRESS);
				} else if ("created_today".equals(filter)) {
					searchCriteria.setCreateDate(DateUtils.formatDate(Calendar
							.getInstance().getTime()));
				} else if ("requested_images".equals(filter)) {
					searchCriteria.setRequestedImages(true);
				} else if ("recieved_images".equals(filter)) {
					searchCriteria.setRecievedImages(true);
				} else if ("last_search".equals(filter)) {
					searchCriteria = (CarSearchCriteria) request.getSession(
							true).getAttribute("lastSearchCriteria");
				} else if ("no_updates".equals(filter)) {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, -10);
					Date toDate = c.getTime();
					searchCriteria.setNotUpdatedSince(true);
					searchCriteria.setUpdateDate(DateUtils.formatDate(toDate));
				}else if("outfit_cars".equals(filter)){
					searchCriteria.setSourceTypeCd(SourceType.OUTFIT);
				}else if("promo_pyg_cars".equals(filter)){
					searchCriteria.setSourceTypeCd(SourceType.PYG);
				}
				
				//Setting flag to true to include outfit cars in search results.
				
				
				//Setting flag to true to include outfit cars in search results.
				if(null != session.getAttribute(DASHBOARD_SORTED_ON)) {
					dashboardSortedOn = (String) session.getAttribute(DASHBOARD_SORTED_ON);
					dashboardSortedOrder = (String) session.getAttribute(DASHBOARD_SORTED_ORDER);
				}
				if(null != request.getParameter(DASHBOARD_DISPLAY_TABLE_ID)) {
					Integer colno = new Integer(request.getParameter(DASHBOARD_DISPLAY_TABLE_ID));
					colno++;
					dashboardSortedOn = colno.toString();
					
					if(request.getParameter(DISPLAY_TABLE_SORT_ORDER).equals("2")) {
						dashboardSortedOrder = "ascending";
					}else{
						dashboardSortedOrder = "descending";
					} 
				}		
				if(null != request.getParameter(DISPLAY_TABLE_PAGE_NUMBER)) {
					pageNo = new Integer(request.getParameter(DISPLAY_TABLE_PAGE_NUMBER)); 			
				}
			
				if (searchCriteria == null) {
					cars = new ArrayList<CarsDTO>();
				} else {
					searchCriteria.setIncludeOutFitCars(true);
					searchCriteria.setCurrentPage(pageNo.toString());
					searchCriteria.setSortColumnNm(dashboardSortedOn);
					searchCriteria.setIncludePYGCars(true);
					searchCriteria.setSortOrder(dashboardSortedOrder);
					searchCriteria.setIsFromDashBoardAndSearchCar(true);
					if ("view_all".equals(filter)) {
						cars = carManager.getCarsForUser(searchCriteria,user);
					} else {
						cars = carManager.searchCarsForNewDashBoard(
								searchCriteria, user);
					}
					enableBothButtons = true;
				}
			}
			if (log.isDebugEnabled())
				log
						.debug("*********************Dashboard Data Retrieval Processing: "
								+ (System.currentTimeMillis() - dataStartTime)
								+ " ms");

		} catch (Exception ex) {
			log.error("Exception raised!!!!!!", ex);
		}
		/**---Maintaining the sorted column on dashboard-------------*/
		//commented for displaytag1.2 up gradation, since displaytag1.2 library is not supporting defaultPage attribute
		/*if(null != session.getAttribute(SELECTED_PAGE_NUMBER) && StringUtils.isNotEmpty(operation)) {
			pageNo = (Integer) session.getAttribute(SELECTED_PAGE_NUMBER); 			
		}*/
		
		model.put(SELECTED_PAGE_NUMBER, pageNo);
		session.setAttribute(DASHBOARD_SORTED_ON, dashboardSortedOn);
		session.setAttribute(DASHBOARD_SORTED_ORDER, dashboardSortedOrder);
		session.setAttribute(SELECTED_PAGE_NUMBER, pageNo);			
		
		model.put(DASHBOARD_SORTED_ON, dashboardSortedOn);
		model.put(DASHBOARD_SORTED_ORDER, dashboardSortedOrder);
		/**--------------------------------------------------------*/
		User user = this.getLoggedInUser();
		//Below method added to get to CARS count for pagination .
		Integer total_Count=0;
		if(searchCriteria !=null)
			total_Count =  carManager.searchCarsForNewDashBoardCount(searchCriteria, user);
		model.put(DASH_BOARD_DISPLAY_CARS, cars);
		if(null != cars)
			model.put(NO_OF_CARS_DISPLAYED, total_Count);
		if(null != user)
			model.put(USER_TYPE_CD, user.getUserType().getUserTypeCd());
		if(null != searchCriteria) {
			model.put(ENABLE_ARCHIVE, searchCriteria.getEnableArchive());
			model.put(ENABLE_DELETE, searchCriteria.getEnableDelete());
		}
		if(searchCriteria != null && null == searchCriteria.getArchive()){
			model.put(ENABLE_ALL_BUTTONS, enableBothButtons);
		}
		return new ModelAndView(CAR_LISTING,model);
	}
}
